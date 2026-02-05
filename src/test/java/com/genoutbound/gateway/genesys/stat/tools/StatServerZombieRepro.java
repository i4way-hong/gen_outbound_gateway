package com.genoutbound.gateway.genesys.stat.tools;

import com.genesyslab.platform.commons.connection.Connection;
import com.genesyslab.platform.commons.connection.configuration.PropertyConfiguration;
import com.genesyslab.platform.commons.protocol.ChannelState;
import com.genesyslab.platform.commons.protocol.Endpoint;
import com.genesyslab.platform.commons.protocol.Message;
import com.genesyslab.platform.commons.protocol.ProtocolException;
import com.genesyslab.platform.reporting.protocol.StatServerProtocol;
import com.genesyslab.platform.reporting.protocol.statserver.AgentGroup;
import com.genesyslab.platform.reporting.protocol.statserver.AgentStatus;
import com.genesyslab.platform.reporting.protocol.statserver.AgentStatusesCollection;
import com.genesyslab.platform.reporting.protocol.statserver.Notification;
import com.genesyslab.platform.reporting.protocol.statserver.NotificationMode;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticMetric;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObject;
import com.genesyslab.platform.reporting.protocol.statserver.StatisticObjectType;
import com.genesyslab.platform.reporting.protocol.statserver.events.EventInfo;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestCloseStatistic;
import com.genesyslab.platform.reporting.protocol.statserver.requests.RequestOpenStatistic;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Stat Server 좀비 커넥션 재현용 유틸리티.
 *
 * - unsafe 모드: 예외 발생 시 close를 의도적으로 생략해 레거시 문제를 재현합니다.
 * - safe 모드: try/finally로 close를 보장합니다.
 */
public final class StatServerZombieRepro {

    private StatServerZombieRepro() {
    }

    public static void main(String[] args) throws Exception {
        ReproOptions options = ReproOptions.fromArgs(args);
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        PrintStream err = new PrintStream(System.err, true, StandardCharsets.UTF_8);
        if (options.threads <= 1) {
            runSingleThread(options, out, err);
        } else {
            runMultiThread(options, out, err);
        }

        if (options.forceExit) {
            out.println("작업 완료 - 강제 종료합니다.");
            System.exit(0);
        }
    }

    private static void runOnce(ReproOptions options, int index, PrintStream out, PrintStream err) throws Exception {
        StatServerProtocol protocol = null;
        AtomicReference<EventInfo> eventInfoRef = new AtomicReference<>();
        int referenceId = ThreadLocalRandom.current().nextInt(100000, 999999);
        RequestCloseStatistic closeRequest = buildClose(referenceId);
        String threadLabel = threadLabel();
        try {
            protocol = openProtocol(options, eventInfoRef);
            if (protocol.getState() != ChannelState.Opened) {
                throw new IllegalStateException(threadLabel + " Stat Server 연결 실패");
            }
            RequestOpenStatistic request = buildOpen(options, referenceId);
            Message response = protocol.request(request);
            EventInfo eventInfo = response instanceof EventInfo info ? info : null;
            if (eventInfo == null && options.delayMs > 0) {
                Thread.sleep(options.delayMs);
            }
            if (eventInfo == null) {
                eventInfo = eventInfoRef.get();
            }
            logAgentStatuses(eventInfo, out, err, threadLabel);
            if (options.mode == ReproMode.UNSAFE) {
                throw new IllegalStateException(threadLabel + " 강제 예외(unsafe 모드)");
            }

            protocol.send(closeRequest);
            out.println("[" + index + "] " + threadLabel + " 정상 처리");
        } finally {
            if (options.mode == ReproMode.SAFE) {
                safeSendClose(protocol, closeRequest);
                safeClose(protocol);
            }
        }
    }

    private static void runSingleThread(ReproOptions options, PrintStream out, PrintStream err) throws Exception {
        for (int i = 1; i <= options.iterations; i++) {
            try {
                runOnce(options, i, out, err);
            } catch (Exception ex) {
                err.println("[" + i + "] " + threadLabel() + " 오류: " + ex.getMessage());
            }
            if (options.delayMs > 0) {
                Thread.sleep(options.delayMs);
            }
        }
    }

    private static void runMultiThread(ReproOptions options, PrintStream out, PrintStream err) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(options.threads);
        try {
            List<Future<Void>> futures = new ArrayList<>();
            for (int i = 1; i <= options.iterations; i++) {
                int index = i;
                futures.add(executor.submit((Callable<Void>) () -> {
                    try {
                        runOnce(options, index, out, err);
                    } catch (Exception ex) {
                        err.println("[" + index + "] " + threadLabel() + " 오류: " + ex.getMessage());
                    }
                    if (options.delayMs > 0) {
                        Thread.sleep(options.delayMs);
                    }
                    return null;
                }));
            }
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (ExecutionException ex) {
                    err.println(threadLabel() + " 멀티스레드 실행 오류: " + ex.getCause().getMessage());
                }
            }
        } finally {
            executor.shutdownNow();
        }
    }

    private static void logAgentStatuses(EventInfo eventInfo, PrintStream out, PrintStream err, String threadLabel) {
        if (eventInfo == null) {
            err.println(threadLabel + " EventInfo is null");
            return;
        }
        Object stateValue = eventInfo.getStateValue();
        if (!(stateValue instanceof AgentGroup group)) {
            err.println(threadLabel + " EventInfo stateValue is not AgentGroup");
            return;
        }
        AgentStatusesCollection agents = group.getAgents();
        if (agents == null || group.getAgentCount() <= 0) {
            err.println(threadLabel + " No agents found");
            return;
        }
        List<Map<String, Object>> results = new ArrayList<>();
        for (int i = 0; i < agents.getCount(); i++) {   
            AgentStatus status = (AgentStatus) agents.getItem(i);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("EMPLOYEE_ID", status.getAgentId());
            item.put("STAT", String.valueOf(status.getStatus()));
            results.add(item);
        }
        if (results.isEmpty()) {
            out.println(threadLabel + " Results is empty");
        } else {
            out.println(threadLabel + " Results found: " + results.size());
        }
    }

    private static String threadLabel() {
        Thread thread = Thread.currentThread();
        return "[" + thread.getName() + "-" + thread.getId() + "]";
    }

    private static StatServerProtocol openProtocol(ReproOptions options,
                                                  AtomicReference<EventInfo> eventInfoRef)
        throws ProtocolException, InterruptedException {
        PropertyConfiguration config = new PropertyConfiguration();
        config.setUseAddp(options.addpEnabled);
        config.setAddpClientTimeout(options.addpClientTimeout);
        config.setAddpServerTimeout(options.addpServerTimeout);
        config.setOption(Connection.STR_ATTR_ENCODING_NAME_KEY, options.charset);

        Endpoint endpoint = new Endpoint(options.endpoint, options.ip, options.port, config);
        StatServerProtocol protocol = new StatServerProtocol(endpoint);
        protocol.setClientName(options.clientName);
        protocol.setTimeout(options.timeoutMs);
        protocol.setMessageHandler(message -> {
            if (message instanceof EventInfo info) {
                eventInfoRef.set(info);
            }
        });
        protocol.open();
        return protocol;
    }

    private static RequestOpenStatistic buildOpen(ReproOptions options, int referenceId) {
        RequestOpenStatistic request = RequestOpenStatistic.create();
        StatisticObject object = StatisticObject.create();
        object.setObjectId(options.groupName);
        object.setObjectType(StatisticObjectType.GroupAgents);
        object.setTenantName(options.tenantName);
        object.setTenantPassword("");

        StatisticMetric metric = StatisticMetric.create();
        metric.setStatisticType(options.defaultStatistic);
        metric.setTimeProfile("Default");
        metric.setTimeRange("");
        metric.setFilter("");

        Notification notification = Notification.create();
        notification.setMode(NotificationMode.Periodical);
        notification.setFrequency(10);

        request.setStatisticObject(object);
        request.setStatisticMetric(metric);
        request.setNotification(notification);
        request.setReferenceId(referenceId);
        return request;
    }

    private static RequestCloseStatistic buildClose(int referenceId) {
        RequestCloseStatistic request = RequestCloseStatistic.create();
        request.setReferenceId(referenceId);
        request.setStatisticId(referenceId);
        return request;
    }

    private static void safeSendClose(StatServerProtocol protocol, RequestCloseStatistic closeRequest) {
        if (protocol == null || protocol.getState() != ChannelState.Opened) {
            return;
        }
        try {
            protocol.send(closeRequest);
        } catch (ProtocolException | IllegalStateException ex) {
            System.err.println("통계 종료 요청 실패: " + ex.getMessage());
        }
    }

    private static void safeClose(StatServerProtocol protocol) {
        if (protocol == null) {
            return;
        }
        try {
            protocol.close();
        } catch (ProtocolException | IllegalStateException | InterruptedException ex) {
            System.err.println("프로토콜 종료 실패: " + ex.getMessage());
        }
    }

    private enum ReproMode {
        SAFE,
        UNSAFE
    }

    private static final class ReproOptions {
        private final ReproMode mode;
        private final int iterations;
        private final long delayMs;
        private final String endpoint;
        private final String ip;
        private final int port;
        private final String clientName;
        private final String charset;
        private final boolean addpEnabled;
        private final int addpClientTimeout;
        private final int addpServerTimeout;
        private final int timeoutMs;
        private final String tenantName;
        private final String defaultStatistic;
        private final String groupName;
        private final boolean forceExit;
        private final int threads;

        private ReproOptions(ReproMode mode,
                             int iterations,
                             long delayMs,
                             String endpoint,
                             String ip,
                             int port,
                             String clientName,
                             String charset,
                             boolean addpEnabled,
                             int addpClientTimeout,
                             int addpServerTimeout,
                             int timeoutMs,
                             String tenantName,
                             String defaultStatistic,
                             String groupName,
                             boolean forceExit,
                             int threads) {
            this.mode = mode;
            this.iterations = iterations;
            this.delayMs = delayMs;
            this.endpoint = endpoint;
            this.ip = ip;
            this.port = port;
            this.clientName = clientName;
            this.charset = charset;
            this.addpEnabled = addpEnabled;
            this.addpClientTimeout = addpClientTimeout;
            this.addpServerTimeout = addpServerTimeout;
            this.timeoutMs = timeoutMs;
            this.tenantName = tenantName;
            this.defaultStatistic = defaultStatistic;
            this.groupName = groupName;
            this.forceExit = forceExit;
            this.threads = threads;
        }

        private static ReproOptions fromArgs(String[] args) {
            String modeRaw = getArg(args, "--mode", "safe");
            ReproMode mode = "unsafe".equalsIgnoreCase(modeRaw) ? ReproMode.UNSAFE : ReproMode.SAFE;

            int iterations = Integer.parseInt(getArg(args, "--iterations", "20"));
            long delayMs = Long.parseLong(getArg(args, "--delay-ms", "200"));

            String endpoint = envOrDefault("GENESYS_STAT_ENDPOINT_P", "Stat_EP_P");
            String ip = envOrDefault("GENESYS_STAT_IP_P", "172.168.30.2");
            int port = Integer.parseInt(envOrDefault("GENESYS_STAT_PORT_P", "7002"));
            String clientName = envOrDefault("GENESYS_STAT_CLIENT_NAME", "stat-zombie-repro");
            String charset = envOrDefault("GENESYS_STAT_CHARSET", "MS949");
            boolean addpEnabled = Boolean.parseBoolean(envOrDefault("GENESYS_STAT_ADDP_ENABLED", "true"));
            int addpClientTimeout = Integer.parseInt(envOrDefault("GENESYS_STAT_ADDP_CLIENT_TIMEOUT", "5"));
            int addpServerTimeout = Integer.parseInt(envOrDefault("GENESYS_STAT_ADDP_SERVER_TIMEOUT", "5"));
            int timeoutMs = Integer.parseInt(envOrDefault("GENESYS_STAT_TIMEOUT_MS", "5000"));
            String tenantName = envOrDefault("GENESYS_STAT_TENANT_NAME", "Resources");
            String defaultStatistic = envOrDefault("GENESYS_STAT_DEFAULT_STATISTIC", "CurrentAllAgentState");
            String groupName = envOrDefault("GENESYS_STAT_GROUP_NAME", "상담그룹1");
            boolean forceExit = Boolean.parseBoolean(getArg(args, "--force-exit", "true"));
            int threads = Integer.parseInt(getArg(args, "--threads", "1"));

            return new ReproOptions(mode, iterations, delayMs, endpoint, ip, port, clientName, charset,
                addpEnabled, addpClientTimeout, addpServerTimeout, timeoutMs, tenantName,
                defaultStatistic, groupName, forceExit, threads);
        }

        private static String envOrDefault(String key, String defaultValue) {
            String value = System.getenv(key);
            return value == null || value.isBlank() ? defaultValue : value;
        }

        private static String getArg(String[] args, String key, String defaultValue) {
            for (int i = 0; i < args.length - 1; i++) {
                if (key.equalsIgnoreCase(args[i])) {
                    return args[i + 1];
                }
            }
            return defaultValue;
        }
    }
}
