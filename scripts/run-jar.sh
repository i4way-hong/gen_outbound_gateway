#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
BASE_DIR="${SCRIPT_DIR}"
LOCAL_LIB_DIR="${BASE_DIR}/lib"
PARENT_LIB_DIR="${BASE_DIR}/../lib"

export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-prod}

export LOGBACK_CONFIG_PATH=${LOGBACK_CONFIG_PATH:-./scripts/config/logback-spring.xml}
export LOG_DIR=${LOG_DIR:-./logs}
export RUN_AS_DAEMON=${RUN_AS_DAEMON:-false}
export PID_FILE=${PID_FILE:-${LOG_DIR}/gen-outbound-gateway.pid}
export CONSOLE_LOG_FILE=${CONSOLE_LOG_FILE:-${LOG_DIR}/gen-outbound-gateway.console.log}

if [[ -z "${SPRING_CONFIG_ADDITIONAL_LOCATION:-}" && -d "${BASE_DIR}/config" ]]; then
  export SPRING_CONFIG_ADDITIONAL_LOCATION="${BASE_DIR}/config/"
fi

if [[ -z "${ENV_FILE:-}" ]]; then
  if [[ -f "${BASE_DIR}/config/.env.prod" ]]; then
    ENV_FILE="${BASE_DIR}/config/.env.prod"
  elif [[ -f "${BASE_DIR}/config/.env" ]]; then
    ENV_FILE="${BASE_DIR}/config/.env"
  fi
fi

if [[ -n "${ENV_FILE:-}" && -f "${ENV_FILE}" ]]; then
  set -a
  source "${ENV_FILE}"
  set +a
fi

: "${DB_URL:=jdbc:sqlserver://172.168.30.61:1433;databaseName=RND_TEST;encrypt=true;trustServerCertificate=true}"
: "${DB_USERNAME:=RND_USER}"
# DB_PASSWORD는 환경변수로 반드시 주입하세요.

: "${ADMIN_USERNAME:=admin}"
# ADMIN_PASSWORD는 환경변수로 반드시 주입하세요.

: "${JWT_ENABLED:=false}"

: "${GENESYS_CFG_USERNAME:=default}"
# GENESYS_CFG_PASSWORD는 환경변수로 반드시 주입하세요.

: "${CCC_SERVICE_ENC_ENABLED:=false}"
: "${CCC_SERVICE_ENC_KEY:=12345678901234567890123456789012}"
: "${CCC_SERVICE_ENC_IV:=1234567890123456}"

: "${JWT_SECRET:=CHANGE_ME_32_BYTE_SECRET_FOR_PROD}"

if [[ -z "${JAR_PATH:-}" ]]; then
  if [[ -f "${BASE_DIR}/gen-outbound-gateway-0.0.1-SNAPSHOT.jar" ]]; then
    JAR_PATH="${BASE_DIR}/gen-outbound-gateway-0.0.1-SNAPSHOT.jar"
  fi
fi

if [[ -z "${JAR_PATH:-}" || ! -f "${JAR_PATH}" ]]; then
  echo "gen-outbound-gateway-0.0.1-SNAPSHOT.jar를 찾을 수 없습니다. 스크립트와 같은 폴더에 두거나 JAR_PATH를 지정하세요."
  exit 1
fi

LOADER_ARG=""
if [[ -d "${LOCAL_LIB_DIR}" ]]; then
  LOADER_ARG="-Dloader.path=${LOCAL_LIB_DIR}"
elif [[ -d "${PARENT_LIB_DIR}" ]]; then
  LOADER_ARG="-Dloader.path=${PARENT_LIB_DIR}"
fi

JAVA_EXE="java"
if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
  JAVA_EXE="${JAVA_HOME}/bin/java"
fi

export JAVA_OPTS=${JAVA_OPTS:-"-Xms512m -Xmx1024m"}

JAVA_VERSION=$(${JAVA_EXE} -version 2>&1 | awk -F '"' '/version/ {print $2}')
if [[ "${JAVA_VERSION}" != 17* ]]; then
  echo "Java 17이 필요합니다. 현재 버전: ${JAVA_VERSION}"
fi

missing=()
for var in DB_URL DB_USERNAME DB_PASSWORD ADMIN_USERNAME ADMIN_PASSWORD GENESYS_CFG_USERNAME GENESYS_CFG_PASSWORD; do
  if [[ -z "${!var:-}" ]]; then
    missing+=("${var}")
  fi
done

if [[ ${#missing[@]} -gt 0 ]]; then
  echo "필수 환경변수가 없습니다: ${missing[*]}"
  echo "config/.env.prod 또는 시스템 환경변수를 설정하세요."
  exit 1
fi

if [[ "${SPRING_PROFILES_ACTIVE}" == "prod" ]]; then
  if [[ -z "${JWT_SECRET:-}" ]]; then
    echo "prod 프로파일에서는 JWT_SECRET 환경변수가 필요합니다."
    exit 1
  fi
fi

if [[ "${CCC_SERVICE_ENC_ENABLED:-}" == "true" ]]; then
  if [[ -z "${CCC_SERVICE_ENC_KEY:-}" || -z "${CCC_SERVICE_ENC_IV:-}" ]]; then
    echo "암호화가 활성화되었지만 CCC_SERVICE_ENC_KEY/IV가 없습니다."
    exit 1
  fi
fi

echo "프로파일 ${SPRING_PROFILES_ACTIVE}"
echo "JAR 실행: ${JAR_PATH}"

is_running() {
  if [[ -f "${PID_FILE}" ]]; then
    local pid
    pid=$(cat "${PID_FILE}" 2>/dev/null || true)
    if [[ -n "${pid}" ]] && kill -0 "${pid}" 2>/dev/null; then
      return 0
    fi
  fi
  return 1
}

get_pid() {
  cat "${PID_FILE}" 2>/dev/null || true
}

start_app() {
  mkdir -p "${LOG_DIR}"

  if is_running; then
    echo "이미 실행 중입니다. PID=$(get_pid)"
    return 0
  fi

  nohup ${JAVA_EXE} ${JAVA_OPTS:-} ${LOADER_ARG} -jar "${JAR_PATH}" >> "${CONSOLE_LOG_FILE}" 2>&1 < /dev/null &
  local app_pid=$!
  echo "${app_pid}" > "${PID_FILE}"
  disown "${app_pid}" 2>/dev/null || true

  echo "백그라운드 실행 시작: PID=${app_pid}"
  echo "PID 파일: ${PID_FILE}"
  echo "콘솔 로그: ${CONSOLE_LOG_FILE}"
}

stop_app() {
  if ! is_running; then
    echo "실행 중이 아닙니다."
    rm -f "${PID_FILE}"
    return 0
  fi

  local pid
  pid=$(get_pid)
  echo "종료 중... PID=${pid}"
  kill "${pid}" 2>/dev/null || true

  for _ in {1..10}; do
    if ! kill -0 "${pid}" 2>/dev/null; then
      rm -f "${PID_FILE}"
      echo "정상 종료되었습니다."
      return 0
    fi
    sleep 1
  done

  echo "강제 종료(SIGKILL) 수행..."
  kill -9 "${pid}" 2>/dev/null || true
  rm -f "${PID_FILE}"
  echo "강제 종료되었습니다."
}

status_app() {
  if is_running; then
    local pid
    pid=$(get_pid)
    echo "RUNNING (PID=${pid})"
    return 0
  fi

  echo "STOPPED"
  return 1
}

usage() {
  cat <<EOF
사용법: $(basename "$0") [start|stop|atop|restart|status|run|--daemon]

  start     백그라운드 시작
  stop      중지
  atop      stop 별칭
  restart   재시작
  status    상태 확인
  run       포그라운드 실행 (콘솔 종속)
  --daemon  start와 동일

기본값: 인자 미지정 시 start
EOF
}

command="${1:-start}"

if [[ "${RUN_AS_DAEMON,,}" == "true" && "${command}" == "run" ]]; then
  command="start"
fi

case "${command}" in
  start|--daemon)
    start_app
    ;;
  stop|atop)
    stop_app
    ;;
  restart)
    stop_app
    start_app
    ;;
  status)
    status_app
    ;;
  run)
    ${JAVA_EXE} ${JAVA_OPTS:-} ${LOADER_ARG} -jar "${JAR_PATH}"
    ;;
  -h|--help|help)
    usage
    ;;
  *)
    echo "알 수 없는 인자: ${command}"
    usage
    exit 1
    ;;
esac
