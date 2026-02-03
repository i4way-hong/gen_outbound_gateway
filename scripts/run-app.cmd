@echo off
REM Gen Outbound Gateway 실행 스크립트 (CMD)
REM 필요 환경변수는 README.md 참고

setlocal EnableDelayedExpansion
chcp 65001 >nul

if "%SPRING_PROFILES_ACTIVE%"=="" set SPRING_PROFILES_ACTIVE=prod

if "%DB_URL%"=="" set DB_URL=jdbc:sqlserver://172.168.30.61:1433;databaseName=RND_TEST;encrypt=true;trustServerCertificate=true
if "%DB_USERNAME%"=="" set DB_USERNAME=RND_USER
if "%DB_PASSWORD%"=="" set DB_PASSWORD=RND_USER

if "%ADMIN_USERNAME%"=="" set ADMIN_USERNAME=admin
if "%ADMIN_PASSWORD%"=="" set ADMIN_PASSWORD=admin123

if "%JWT_ENABLED%"=="" set JWT_ENABLED=false

if "%GENESYS_CFG_USERNAME%"=="" set GENESYS_CFG_USERNAME=default
if "%GENESYS_CFG_PASSWORD%"=="" set GENESYS_CFG_PASSWORD=password

if "%CCC_SERVICE_ENC_ENABLED%"=="" set CCC_SERVICE_ENC_ENABLED=false
if "%CCC_SERVICE_ENC_KEY%"=="" set CCC_SERVICE_ENC_KEY=12345678901234567890123456789012
if "%CCC_SERVICE_ENC_IV%"=="" set CCC_SERVICE_ENC_IV=1234567890123456

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
echo 앱을 시작합니다..

mvn spring-boot:run
endlocal
