@echo off
REM Gen Outbound Gateway 디버깅 실행 스크립트 (CMD)
REM 필요 환경변수는 README.md 참고

setlocal EnableDelayedExpansion
chcp 65001 >nul

if "%SPRING_PROFILES_ACTIVE%"=="" set SPRING_PROFILES_ACTIVE=prod

if "%LOGBACK_CONFIG_PATH%"=="" set LOGBACK_CONFIG_PATH=./scripts/config/logback-spring.xml
if "%LOG_DIR%"=="" set LOG_DIR=./logs

if "%DB_URL%"=="" set DB_URL=jdbc:sqlserver://172.168.30.61:1433;databaseName=RND_TEST;encrypt=true;trustServerCertificate=true
if "%DB_USERNAME%"=="" set DB_USERNAME=RND_USER
if "%DB_PASSWORD%"=="" set DB_PASSWORD=RND_USER
REM DB_PASSWORD는 환경변수로 반드시 주입하세요.

if "%ADMIN_USERNAME%"=="" set ADMIN_USERNAME=admin
if "%ADMIN_PASSWORD%"=="" set ADMIN_PASSWORD=admin123
REM ADMIN_PASSWORD는 환경변수로 반드시 주입하세요.

if "%JWT_ENABLED%"=="" set JWT_ENABLED=true
if "%AUTH_ENABLED%"=="" set AUTH_ENABLED=true
if "%ALLOW_INSECURE%"=="" set ALLOW_INSECURE=false
REM JWT인증 필요시 JWT_ENABLED=true, AUTH_ENABLED=true, ALLOW_INSECURE=false로 설정하세요.

if "%ALLOW_SWAGGER%"=="" set ALLOW_SWAGGER=true
REM Swagger UI는 개발 편의를 위해 기본적으로 활성화되어 있습니다. 보안이 필요한 환경에서는 ALLOW_SWAGGER=false로 설정하세요.
if "%ALLOW_ADMIN_UI%"=="" set ALLOW_ADMIN_UI=false
REM ADMIN UI는 기본적으로 비활성화되어 있습니다. 필요시 ALLOW_ADMIN_UI=true로 설정하세요.

if "%GENESYS_CFG_USERNAME%"=="" set GENESYS_CFG_USERNAME=default
if "%GENESYS_CFG_PASSWORD%"=="" set GENESYS_CFG_PASSWORD=password
REM GENESYS_CFG_PASSWORD는 환경변수로 반드시 주입하세요.

if "%CCC_SERVICE_ENC_ENABLED%"=="" set CCC_SERVICE_ENC_ENABLED=false
if "%CCC_SERVICE_ENC_KEY%"=="" set CCC_SERVICE_ENC_KEY=12345678901234567890123456789012
if "%CCC_SERVICE_ENC_IV%"=="" set CCC_SERVICE_ENC_IV=1234567890123456
REM 암호화가 필요한 경우 CCC_SERVICE_ENC_ENABLED=true로 설정하고 CCC_SERVICE_ENC_KEY, CCC_SERVICE_ENC_IV를 지정하세요.

if "%JWT_SECRET%"=="" set JWT_SECRET=CHANGE_ME_32_BYTE_SECRET_FOR_PROD
if "%JWT_ACCESS_TOKEN_MINUTES%"=="" set JWT_ACCESS_TOKEN_MINUTES=1
if "%JWT_REFRESH_TOKEN_DAYS%"=="" set JWT_REFRESH_TOKEN_DAYS=1
REM JWT_SECRET는 prod 프로파일에서 필수입니다. prod 프로파일에서는 JWT_SECRET을 반드시 설정하세요.
REM JWT_ACCESS_TOKEN_MINUTES는 액세스 토큰의 유효 기간(분)입니다. 기본값은 2분입니다.
REM JWT_REFRESH_TOKEN_DAYS는 리프레시 토큰의 유효 기간(일)입니다. 기본값은 7일입니다.

set MISSING=
for %%V in (DB_URL DB_USERNAME DB_PASSWORD ADMIN_USERNAME ADMIN_PASSWORD GENESYS_CFG_USERNAME GENESYS_CFG_PASSWORD) do (
  if "!%%V!"=="" set MISSING=!MISSING! %%V
)

if not "%MISSING%"=="" (
  echo 필수 환경변수가 없습니다:%MISSING%
  echo 실행 전에 환경변수를 설정하세요.
  endlocal
  exit /b 1
)

if /i "%SPRING_PROFILES_ACTIVE%"=="prod" (
  if "%JWT_SECRET%"=="" (
    echo prod 프로파일에서는 JWT_SECRET 환경변수가 필요합니다.
    endlocal
    exit /b 1
  )
)

if /i "%CCC_SERVICE_ENC_ENABLED%"=="true" (
  if "%CCC_SERVICE_ENC_KEY%"=="" (
    echo 암호화가 활성화 되었지만 CCC_SERVICE_ENC_KEY가 없습니다.
    endlocal
    exit /b 1
  )
  if "%CCC_SERVICE_ENC_IV%"=="" (
    echo 암호화가 활성화 되었지만 CCC_SERVICE_ENC_IV가 없습니다.
    endlocal
    exit /b 1
  )
)

echo 프로파일 %SPRING_PROFILES_ACTIVE%
echo 앱을 디버그 모드로 시작합니다..

echo JDWP 포트: 5005
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
endlocal
