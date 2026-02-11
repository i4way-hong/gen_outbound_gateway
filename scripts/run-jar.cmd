@echo off
REM Gen Outbound Gateway JAR 실행 스크립트 (CMD)
REM Java 17 필요, JAR 이름: gen-outbound-gateway-0.0.1-SNAPSHOT.jar

setlocal EnableDelayedExpansion
chcp 65001 >nul

set "SCRIPT_DIR=%~dp0"
set "BASE_DIR=%SCRIPT_DIR%"
set "PARENT_LIB_DIR=%BASE_DIR%..\lib"
set "LOCAL_LIB_DIR=%BASE_DIR%lib"
set "LOADER_PATH="

if "%SPRING_PROFILES_ACTIVE%"=="" set SPRING_PROFILES_ACTIVE=prod

if "!LOGBACK_CONFIG_PATH!"=="" set "LOGBACK_CONFIG_PATH=./scripts/config/logback-spring.xml"
if "!LOG_DIR!"=="" set "LOG_DIR=./logs"

if "!SPRING_CONFIG_ADDITIONAL_LOCATION!"=="" (
  if exist "!BASE_DIR!\config" set "SPRING_CONFIG_ADDITIONAL_LOCATION=!BASE_DIR!\config\"
)

if "!ENV_FILE!"=="" (
  if exist "!BASE_DIR!\config\.env.prod" set "ENV_FILE=!BASE_DIR!\config\.env.prod"
  if "!ENV_FILE!"=="" if exist "!BASE_DIR!\config\.env" set "ENV_FILE=!BASE_DIR!\config\.env"
)

if not "!ENV_FILE!"=="" (
  for /f "usebackq tokens=1* delims== eol=#" %%A in ("!ENV_FILE!") do (
    if not "%%A"=="" set "%%A=%%B"
  )
)

if "!DB_URL!"=="" set "DB_URL=jdbc:sqlserver://172.168.30.61:1433;databaseName=RND_TEST;encrypt=true;trustServerCertificate=true"
if "!DB_USERNAME!"=="" set "DB_USERNAME=RND_USER"
if "!DB_PASSWORD!"=="" set "DB_PASSWORD=RND_USER"

if "!ADMIN_USERNAME!"=="" set "ADMIN_USERNAME=admin"
if "!ADMIN_PASSWORD!"=="" set "ADMIN_PASSWORD=admin123"

if "!JWT_ENABLED!"=="" set "JWT_ENABLED=false"

if "!GENESYS_CFG_USERNAME!"=="" set "GENESYS_CFG_USERNAME=default"
if "!GENESYS_CFG_PASSWORD!"=="" set "GENESYS_CFG_PASSWORD=password"

if "!CCC_SERVICE_ENC_ENABLED!"=="" set "CCC_SERVICE_ENC_ENABLED=false"
if "!CCC_SERVICE_ENC_KEY!"=="" set "CCC_SERVICE_ENC_KEY=12345678901234567890123456789012"
if "!CCC_SERVICE_ENC_IV!"=="" set "CCC_SERVICE_ENC_IV=1234567890123456"

if "!JAR_PATH!"=="" (
  if exist "!BASE_DIR!\gen-outbound-gateway-0.0.1-SNAPSHOT.jar" set "JAR_PATH=!BASE_DIR!\gen-outbound-gateway-0.0.1-SNAPSHOT.jar"
)

if "!JAR_PATH!"=="" (
  echo gen-outbound-gateway-0.0.1-SNAPSHOT.jar를 찾을 수 없습니다. 스크립트와 같은 폴더에 두거나 JAR_PATH를 지정하세요.
  endlocal
  exit /b 1
)

if exist "!LOCAL_LIB_DIR!" (
  set "LOADER_PATH=!LOCAL_LIB_DIR!"
) else if exist "!PARENT_LIB_DIR!" (
  set "LOADER_PATH=!PARENT_LIB_DIR!"
)

set "LOADER_ARG="
if not "!LOADER_PATH!"=="" set "LOADER_ARG=-Dloader.path=!LOADER_PATH!"

set "JAVA_EXE=java"
if defined JAVA_HOME set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
echo Java 버전 확인 중...
"%JAVA_EXE%" -version

set MISSING=
for %%V in (DB_URL DB_USERNAME DB_PASSWORD GENESYS_CFG_USERNAME GENESYS_CFG_PASSWORD) do (
  if "!%%V!"=="" set MISSING=!MISSING! %%V
)

if not "%MISSING%"=="" (
  echo 필수 환경변수가 없습니다:%MISSING%
  echo config\.env.prod 또는 시스템 환경변수를 설정하세요.
  endlocal
  exit /b 1
)

if /i "!CCC_SERVICE_ENC_ENABLED!"=="true" (
  if "!CCC_SERVICE_ENC_KEY!"=="" (
    echo 암호화가 활성화 되었지만 CCC_SERVICE_ENC_KEY가 없습니다.
    endlocal
    exit /b 1
  )
  if "!CCC_SERVICE_ENC_IV!"=="" (
    echo 암호화가 활성화 되었지만 CCC_SERVICE_ENC_IV가 없습니다.
    endlocal
    exit /b 1
  )
)

echo 프로파일 !SPRING_PROFILES_ACTIVE!
echo JAR 실행: !JAR_PATH!

if not defined JAVA_OPTS (
  "%JAVA_EXE%" !LOADER_ARG! -jar "!JAR_PATH!"
) else (
  "%JAVA_EXE%" !JAVA_OPTS! !LOADER_ARG! -jar "!JAR_PATH!"
)

endlocal
