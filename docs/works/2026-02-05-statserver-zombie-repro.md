# Stat Server 좀비 커넥션 재현 및 대응 (2026-02-05)

## 목적
- Stat Server 요청 처리 중 예외 발생 시 연결이 정상 종료되지 않고 좀비처럼 남는 문제를 재현하고 검증합니다.

## 재현 유틸리티
- 위치: `src/test/java/com/genoutbound/gateway/genesys/stat/tools/StatServerZombieRepro.java`
- 모드:
  - `safe`: try/finally로 close 보장 (정상 처리 기대)
  - `unsafe`: 예외 발생 시 close 생략 (좀비 커넥션 재현용)

## 실행 파라미터
- `--mode` : `safe` | `unsafe`
- `--iterations` : 반복 횟수 (기본 20)
- `--delay-ms` : 요청 간격 (기본 200)
- `--force-exit` : 실행 후 강제 종료 (기본 true)
- `--threads` : 동시 실행 스레드 수 (기본 1)

## 환경 변수
- `GENESYS_STAT_ENDPOINT_P`
- `GENESYS_STAT_IP_P`
- `GENESYS_STAT_PORT_P`
- `GENESYS_STAT_CLIENT_NAME`
- `GENESYS_STAT_CHARSET`
- `GENESYS_STAT_ADDP_ENABLED`
- `GENESYS_STAT_ADDP_CLIENT_TIMEOUT`
- `GENESYS_STAT_ADDP_SERVER_TIMEOUT`
- `GENESYS_STAT_TIMEOUT_MS`
- `GENESYS_STAT_TENANT_NAME`
- `GENESYS_STAT_DEFAULT_STATISTIC`
- `GENESYS_STAT_GROUP_NAME`

## 적용된 개선 사항
- Stat Server 연결 실패(backup 포함) 시 프로토콜을 명시적으로 닫도록 보완.
- 예외 발생 시에도 `RequestCloseStatistic`을 시도한 뒤 프로토콜 종료하도록 보완.

## 확인 방법
- `unsafe` 모드로 실행 후 Stat Server에서 커넥션이 누수되는지 확인합니다.
- `safe` 모드로 실행하면 동일 부하에서 커넥션 누수가 발생하지 않는지 확인합니다.
> `unsafe` 모드는 의도적으로 예외를 발생시키므로 오류 로그가 반복 출력됩니다. 필요 시 `--iterations` 값을 줄여주세요.

## 실행 명령 (PowerShell)
> PowerShell에서 `-Dexec.mainClass`가 잘못 파싱될 경우 `--%`로 파워셸 파싱을 중지합니다.
> 콘솔 한글 깨짐 방지를 위해 `chcp 65001`로 코드페이지를 UTF-8로 변경하세요.
```powershell
chcp 65001
mvn --% -DskipTests -Dexec.classpathScope=test -Dexec.mainClass=com.genoutbound.gateway.genesys.stat.tools.StatServerZombieRepro -Dexec.args="--mode unsafe --iterations 50 --delay-ms 200 --force-exit true" -Dexec.cleanupDaemonThreads=false exec:java
```

```powershell
chcp 65001
mvn --% -DskipTests -Dexec.classpathScope=test -Dexec.mainClass=com.genoutbound.gateway.genesys.stat.tools.StatServerZombieRepro -Dexec.args="--mode safe --iterations 50 --delay-ms 200 --force-exit true" -Dexec.cleanupDaemonThreads=false exec:java
```

```powershell
chcp 65001
mvn --% -DskipTests -Dexec.classpathScope=test -Dexec.mainClass=com.genoutbound.gateway.genesys.stat.tools.StatServerZombieRepro -Dexec.args="--mode safe --iterations 200 --delay-ms 50 --threads 5 --force-exit true" -Dexec.cleanupDaemonThreads=false exec:java
```
