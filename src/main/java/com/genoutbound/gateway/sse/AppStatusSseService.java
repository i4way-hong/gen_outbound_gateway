package com.genoutbound.gateway.sse;

import com.genoutbound.gateway.genesys.scs.ScsProperties;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import org.springframework.web.context.request.async.AsyncRequestNotUsableException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class AppStatusSseService {

    private static final Logger log = LoggerFactory.getLogger(AppStatusSseService.class);

    private final ScsProperties properties;
    private final Set<SseEmitter> emitters = new CopyOnWriteArraySet<>();
    private final Map<Integer, AppStatusEvent> latestEvents = new ConcurrentHashMap<>();
    private final Map<SseEmitter, EmitterInfo> emitterInfoMap = new ConcurrentHashMap<>();
    private final AtomicLong totalConnections = new AtomicLong();
    private final AtomicLong activeConnections = new AtomicLong();
    private final AtomicLong broadcastCount = new AtomicLong();
    private final Map<String, AtomicLong> disconnectReasons = new ConcurrentHashMap<>();

    public AppStatusSseService(ScsProperties properties) {
        this.properties = properties;
    }

    public SseEmitter subscribe(String clientIp, String sessionId) {
        long timeout = resolveTimeout();
        SseEmitter emitter = new SseEmitter(timeout);
        emitters.add(emitter);
        EmitterInfo info = new EmitterInfo(UUID.randomUUID().toString(), clientIp, sessionId, OffsetDateTime.now());
        emitterInfoMap.put(emitter, info);
        totalConnections.incrementAndGet();
        activeConnections.incrementAndGet();
    emitter.onCompletion(() -> removeEmitter(emitter, "completed", null));
    emitter.onTimeout(() -> removeEmitter(emitter, "timeout", new AsyncRequestTimeoutException()));
    emitter.onError(ex -> removeEmitter(emitter, classifyReason(ex, "error"), ex));
        sendInitialSnapshot(emitter);
        log.info("SSE 연결 등록: id={}, ip={}, sessionId={}, 현재 {}건",
            info.connectionId(), info.clientIp(), info.sessionId(), emitters.size());
        return emitter;
    }

    public void broadcast(AppStatusEvent event) {
        if (event == null || event.appDbid() == null) {
            return;
        }
        latestEvents.put(event.appDbid(), event);
        if (emitters.isEmpty()) {
            log.debug("SSE 브로드캐스트 대상 없음: appDbid={}", event.appDbid());
            return;
        }
        SseEmitter.SseEventBuilder payload = SseEmitter.event()
            .name("app-status")
            .data(event);
        emitters.forEach(emitter -> send(emitter, payload));
        broadcastCount.incrementAndGet();
    }

    public void updateSnapshot(AppStatusEvent event) {
        if (event == null || event.appDbid() == null) {
            return;
        }
        latestEvents.put(event.appDbid(), event);
    }

    @Scheduled(fixedDelayString = "${app.scs.sse.heartbeat-ms:25000}")
    public void heartbeat() {
        if (emitters.isEmpty()) {
            return;
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("time", OffsetDateTime.now());
        SseEmitter.SseEventBuilder event = SseEmitter.event()
            .name("heartbeat")
            .data(payload);
        emitters.forEach(emitter -> send(emitter, event));
        log.debug("SSE heartbeat 전송: 대상 {}건", emitters.size());
    }

    private void sendInitialSnapshot(SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().name("connected").data(Map.of("time", OffsetDateTime.now())));
        } catch (Exception ex) {
            emitters.remove(emitter);
            return;
        }
        latestEvents.values().forEach(event -> send(emitter, SseEmitter.event()
            .name("app-status")
            .data(event)));
    }

    private void send(SseEmitter emitter, SseEmitter.SseEventBuilder event) {
        try {
            emitter.send(event);
        } catch (Exception ex) {
            removeEmitter(emitter, classifyReason(ex, "send-failed"), ex);
        }
    }

    private void removeEmitter(SseEmitter emitter, String reason, Throwable ex) {
        boolean removed = emitters.remove(emitter);
        EmitterInfo info = emitterInfoMap.remove(emitter);
        if (removed) {
            activeConnections.updateAndGet(value -> value > 0 ? value - 1 : 0);
            disconnectReasons.computeIfAbsent(reason, key -> new AtomicLong()).incrementAndGet();
            if (ex == null || isExpectedDisconnect(ex)) {
                log.info("SSE 연결 해제: id={}, reason={}, 현재 {}건",
                    info == null ? null : info.connectionId(), reason, emitters.size());
            } else {
                log.warn("SSE 연결 해제: id={}, reason={}, 현재 {}건",
                    info == null ? null : info.connectionId(), reason, emitters.size(), ex);
            }
        }
    }

    private static String classifyReason(Throwable ex, String fallback) {
        if (ex == null) {
            return fallback;
        }
        if (ex instanceof AsyncRequestNotUsableException) {
            return "client-disconnect";
        }
        Throwable cause = ex.getCause();
        if (cause instanceof IOException) {
            return "client-disconnect";
        }
        return fallback;
    }

    private static boolean isExpectedDisconnect(Throwable ex) {
        if (ex instanceof AsyncRequestNotUsableException) {
            return true;
        }
        Throwable cause = ex.getCause();
        return cause instanceof IOException;
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("totalConnections", totalConnections.get());
        payload.put("activeConnections", activeConnections.get());
        payload.put("broadcastCount", broadcastCount.get());
        payload.put("disconnectReasons", snapshotReasons());
        payload.put("connections", emitterInfoMap.values().stream()
            .map(info -> Map.of(
                "id", info.connectionId(),
                "ip", info.clientIp(),
                "sessionId", info.sessionId(),
                "connectedAt", info.connectedAt()))
            .toList());
        return payload;
    }

    private Map<String, Long> snapshotReasons() {
        Map<String, Long> result = new LinkedHashMap<>();
        disconnectReasons.forEach((key, value) -> result.put(key, value.get()));
        return result;
    }

    private long resolveTimeout() {
        long timeoutMs = properties.getSse().getEmitterTimeoutMs();
        if (timeoutMs <= 0) {
            return 0L;
        }
        return timeoutMs;
    }

    public static String resolveClientIp(HttpHeaders headers, String fallback) {
        String forwarded = headers.getFirst("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        String realIp = headers.getFirst("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return fallback;
    }

    private record EmitterInfo(String connectionId, String clientIp, String sessionId,
                               OffsetDateTime connectedAt) {
    }
}
