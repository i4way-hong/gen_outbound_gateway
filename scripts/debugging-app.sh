#!/usr/bin/env bash
# Gen Outbound Gateway 디버깅 실행 스크립트 (Linux/macOS)
# 필요 환경변수는 README.md 참고

set -euo pipefail

SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
BASE_DIR="${SCRIPT_DIR}"

export SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-prod}

export LOGBACK_CONFIG_PATH=${LOGBACK_CONFIG_PATH:-./scripts/config/logback-spring.xml}
export LOG_DIR=${LOG_DIR:-./logs}

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

export DB_URL=${DB_URL:-"jdbc:sqlserver://172.168.30.61:1433;databaseName=RND_TEST;encrypt=true;trustServerCertificate=true"}
export DB_USERNAME=${DB_USERNAME:-RND_USER}

# DB_PASSWORD는 환경변수로 반드시 주입하세요.
# ADMIN_PASSWORD는 환경변수로 반드시 주입하세요.
# GENESYS_CFG_PASSWORD는 환경변수로 반드시 주입하세요.

export ADMIN_USERNAME=${ADMIN_USERNAME:-admin}
export ADMIN_PASSWORD=${ADMIN_PASSWORD:-admin123}
export JWT_ENABLED=${JWT_ENABLED:-true}
export AUTH_ENABLED=${AUTH_ENABLED:-true}
export ALLOW_INSECURE=${ALLOW_INSECURE:-false}
export ALLOW_SWAGGER=${ALLOW_SWAGGER:-true}
export ALLOW_ADMIN_UI=${ALLOW_ADMIN_UI:-false}

export GENESYS_CFG_USERNAME=${GENESYS_CFG_USERNAME:-default}
export GENESYS_CFG_PASSWORD=${GENESYS_CFG_PASSWORD:-password}

export CCC_SERVICE_ENC_ENABLED=${CCC_SERVICE_ENC_ENABLED:-false}
export CCC_SERVICE_ENC_KEY=${CCC_SERVICE_ENC_KEY:-12345678901234567890123456789012}
export CCC_SERVICE_ENC_IV=${CCC_SERVICE_ENC_IV:-1234567890123456}

export JWT_SECRET=${JWT_SECRET:-CHANGE_ME_32_BYTE_SECRET_FOR_PROD}
export JWT_ACCESS_TOKEN_MINUTES=${JWT_ACCESS_TOKEN_MINUTES:-1}
export JWT_REFRESH_TOKEN_DAYS=${JWT_REFRESH_TOKEN_DAYS:-1}

MISSING=""
for var in DB_URL DB_USERNAME DB_PASSWORD ADMIN_USERNAME ADMIN_PASSWORD GENESYS_CFG_USERNAME GENESYS_CFG_PASSWORD; do
  if [[ -z "${!var:-}" ]]; then
    MISSING+=" ${var}"
  fi
done

if [[ -n "$MISSING" ]]; then
  echo "필수 환경변수가 없습니다:${MISSING}"
  echo "실행 전에 환경변수를 설정하세요."
  exit 1
fi

if [[ "${SPRING_PROFILES_ACTIVE}" == "prod" ]]; then
  if [[ -z "${JWT_SECRET:-}" ]]; then
    echo "prod 프로파일에서는 JWT_SECRET 환경변수가 필요합니다."
    exit 1
  fi
fi

if [[ "${CCC_SERVICE_ENC_ENABLED}" == "true" ]]; then
  if [[ -z "${CCC_SERVICE_ENC_KEY:-}" ]]; then
    echo "암호화가 활성화 되었지만 CCC_SERVICE_ENC_KEY가 없습니다."
    exit 1
  fi
  if [[ -z "${CCC_SERVICE_ENC_IV:-}" ]]; then
    echo "암호화가 활성화 되었지만 CCC_SERVICE_ENC_IV가 없습니다."
    exit 1
  fi
fi

echo "프로파일 ${SPRING_PROFILES_ACTIVE}"
echo "앱을 디버그 모드로 시작합니다.."

echo "JDWP 포트: 5005"
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
