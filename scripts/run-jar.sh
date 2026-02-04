#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
BASE_DIR="${SCRIPT_DIR}"
LOCAL_LIB_DIR="${BASE_DIR}/lib"
PARENT_LIB_DIR="${BASE_DIR}/../lib"

export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-prod}

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
: "${DB_PASSWORD:=RND_USER}"

: "${ADMIN_USERNAME:=admin}"
: "${ADMIN_PASSWORD:=admin123}"

: "${JWT_ENABLED:=false}"

: "${GENESYS_CFG_USERNAME:=default}"
: "${GENESYS_CFG_PASSWORD:=password}"

: "${CCC_SERVICE_ENC_ENABLED:=false}"
: "${CCC_SERVICE_ENC_KEY:=12345678901234567890123456789012}"
: "${CCC_SERVICE_ENC_IV:=1234567890123456}"

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

JAVA_VERSION=$(${JAVA_EXE} -version 2>&1 | awk -F '"' '/version/ {print $2}')
if [[ "${JAVA_VERSION}" != 17* ]]; then
  echo "Java 17이 필요합니다. 현재 버전: ${JAVA_VERSION}"
fi

missing=()
for var in DB_URL DB_USERNAME DB_PASSWORD GENESYS_CFG_USERNAME GENESYS_CFG_PASSWORD; do
  if [[ -z "${!var:-}" ]]; then
    missing+=("${var}")
  fi
done

if [[ ${#missing[@]} -gt 0 ]]; then
  echo "필수 환경변수가 없습니다: ${missing[*]}"
  echo "config/.env.prod 또는 시스템 환경변수를 설정하세요."
  exit 1
fi

if [[ "${CCC_SERVICE_ENC_ENABLED:-}" == "true" ]]; then
  if [[ -z "${CCC_SERVICE_ENC_KEY:-}" || -z "${CCC_SERVICE_ENC_IV:-}" ]]; then
    echo "암호화가 활성화되었지만 CCC_SERVICE_ENC_KEY/IV가 없습니다."
    exit 1
  fi
fi

echo "프로파일 ${SPRING_PROFILES_ACTIVE}"
echo "JAR 실행: ${JAR_PATH}"

${JAVA_EXE} ${JAVA_OPTS:-} ${LOADER_ARG} -jar "${JAR_PATH}"
