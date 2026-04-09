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
if "!PID_FILE!"=="" set "PID_FILE=!LOG_DIR!\gen-outbound-gateway.pid"
if "!CONSOLE_LOG_FILE!"=="" set "CONSOLE_LOG_FILE=!LOG_DIR!\gen-outbound-gateway.console.log"

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
REM DB_PASSWORD는 환경변수로 반드시 주입하세요.

if "!ADMIN_USERNAME!"=="" set "ADMIN_USERNAME=admin"
REM ADMIN_PASSWORD는 환경변수로 반드시 주입하세요.

if "!JWT_ENABLED!"=="" set "JWT_ENABLED=false"

if "!GENESYS_CFG_USERNAME!"=="" set "GENESYS_CFG_USERNAME=default"
REM GENESYS_CFG_PASSWORD는 환경변수로 반드시 주입하세요.

if "!CCC_SERVICE_ENC_ENABLED!"=="" set "CCC_SERVICE_ENC_ENABLED=false"
if "!CCC_SERVICE_ENC_KEY!"=="" set "CCC_SERVICE_ENC_KEY=12345678901234567890123456789012"
if "!CCC_SERVICE_ENC_IV!"=="" set "CCC_SERVICE_ENC_IV=1234567890123456"

if "!JWT_SECRET!"=="" set "JWT_SECRET=CHANGE_ME_32_BYTE_SECRET_FOR_PROD"

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
for %%V in (DB_URL DB_USERNAME DB_PASSWORD ADMIN_USERNAME ADMIN_PASSWORD GENESYS_CFG_USERNAME GENESYS_CFG_PASSWORD) do (
  if "!%%V!"=="" set MISSING=!MISSING! %%V
)

if not "%MISSING%"=="" (
  echo 필수 환경변수가 없습니다:%MISSING%
  echo config\.env.prod 또는 시스템 환경변수를 설정하세요.
  endlocal
  exit /b 1
)

if /i "!SPRING_PROFILES_ACTIVE!"=="prod" (
  if "!JWT_SECRET!"=="" (
    echo prod 프로파일에서는 JWT_SECRET 환경변수가 필요합니다.
    endlocal
    exit /b 1
  )
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

set "COMMAND=%~1"
if "%COMMAND%"=="" set "COMMAND=start"

if /i "%COMMAND%"=="start" goto :start
if /i "%COMMAND%"=="stop" goto :stop
if /i "%COMMAND%"=="atop" goto :stop
if /i "%COMMAND%"=="restart" goto :restart
if /i "%COMMAND%"=="status" goto :status
if /i "%COMMAND%"=="run" goto :run
if /i "%COMMAND%"=="--daemon" goto :start
if /i "%COMMAND%"=="help" goto :usage
if /i "%COMMAND%"=="-h" goto :usage
if /i "%COMMAND%"=="--help" goto :usage

echo 알 수 없는 인자: %COMMAND%
goto :usage

:isRunning
if not exist "!PID_FILE!" exit /b 1
set "RUN_PID="
for /f "usebackq delims=" %%P in ("!PID_FILE!") do set "RUN_PID=%%P"
if "!RUN_PID!"=="" exit /b 1
powershell -NoProfile -Command "if (Get-Process -Id !RUN_PID! -ErrorAction SilentlyContinue) { exit 0 } else { exit 1 }"
exit /b %errorlevel%

:start
if not exist "!LOG_DIR!" mkdir "!LOG_DIR!"
call :isRunning
if %errorlevel%==0 (
  echo 이미 실행 중입니다. PID=!RUN_PID!
  endlocal
  exit /b 0
)

set "ENV_JAVA_EXE=!JAVA_EXE!"
set "ENV_JAVA_OPTS=!JAVA_OPTS!"
set "ENV_LOADER_ARG=!LOADER_ARG!"
set "ENV_JAR_PATH=!JAR_PATH!"
set "ENV_CONSOLE_LOG_FILE=!CONSOLE_LOG_FILE!"

set "APP_PID="
for /f "usebackq delims=" %%P in (`powershell -NoProfile -Command "$argsList = @(); if ($env:ENV_JAVA_OPTS) { $argsList += ($env:ENV_JAVA_OPTS -split '\s+') }; if ($env:ENV_LOADER_ARG) { $argsList += $env:ENV_LOADER_ARG }; $argsList += '-jar'; $argsList += $env:ENV_JAR_PATH; $p = Start-Process -FilePath $env:ENV_JAVA_EXE -ArgumentList $argsList -RedirectStandardOutput $env:ENV_CONSOLE_LOG_FILE -RedirectStandardError $env:ENV_CONSOLE_LOG_FILE -PassThru; $p.Id"`) do set "APP_PID=%%P"

if "!APP_PID!"=="" (
  echo 백그라운드 시작에 실패했습니다.
  endlocal
  exit /b 1
)

> "!PID_FILE!" echo !APP_PID!
echo 백그라운드 실행 시작: PID=!APP_PID!
echo PID 파일: !PID_FILE!
echo 콘솔 로그: !CONSOLE_LOG_FILE!
endlocal
exit /b 0

:stop
call :isRunning
if not %errorlevel%==0 (
  echo 실행 중이 아닙니다.
  if exist "!PID_FILE!" del /q "!PID_FILE!"
  endlocal
  exit /b 0
)

echo 종료 중... PID=!RUN_PID!
powershell -NoProfile -Command "try { Stop-Process -Id !RUN_PID! -ErrorAction Stop } catch {}"
powershell -NoProfile -Command "Start-Sleep -Seconds 2; if (Get-Process -Id !RUN_PID! -ErrorAction SilentlyContinue) { try { Stop-Process -Id !RUN_PID! -Force -ErrorAction Stop } catch {} }"

if exist "!PID_FILE!" del /q "!PID_FILE!"
echo 종료되었습니다.
endlocal
exit /b 0

:status
call :isRunning
if %errorlevel%==0 (
  echo RUNNING (PID=!RUN_PID!)
  endlocal
  exit /b 0
)
echo STOPPED
endlocal
exit /b 1

:restart
call :stop
setlocal EnableDelayedExpansion
goto :start

:run
if not defined JAVA_OPTS (
  "%JAVA_EXE%" !LOADER_ARG! -jar "!JAR_PATH!"
) else (
  "%JAVA_EXE%" !JAVA_OPTS! !LOADER_ARG! -jar "!JAR_PATH!"
)

endlocal
exit /b %errorlevel%

:usage
echo 사용법: %~nx0 [start^|stop^|atop^|restart^|status^|run^|--daemon]
echo.
echo   start    백그라운드 시작
echo   stop     중지
echo   atop     stop 별칭
echo   restart  재시작
echo   status   상태 확인
echo   run      포그라운드 실행
echo.
echo 기본값: 인자 미지정 시 start
endlocal
exit /b 1

