# Gen Outbound Gateway 실행 스크립트 (PowerShell)
# 필요한 환경변수는 README.md 참고

# 콘솔 출력 설정
chcp 65001 | Out-Null
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

if (-not $env:SPRING_PROFILES_ACTIVE) {
    $env:SPRING_PROFILES_ACTIVE = "prod"
}

$env:DB_URL = "jdbc:sqlserver://172.168.30.61:1433;databaseName=RND_TEST;encrypt=true;trustServerCertificate=true"
$env:DB_USERNAME = "RND_USER"
$env:DB_PASSWORD = "RND_USER"

$env:ADMIN_USERNAME = "admin"
$env:ADMIN_PASSWORD = "admin123"

$env:JWT_ENABLED = "false"

$env:GENESYS_CFG_USERNAME = "default"
$env:GENESYS_CFG_PASSWORD = "password"

# 암호화 사용 시 true 아니면 false
$env:CCC_SERVICE_ENC_ENABLED = "false"
# 암호화 사용이 true 일때 아래값 설정
$env:CCC_SERVICE_ENC_KEY = "12345678901234567890123456789012"
$env:CCC_SERVICE_ENC_IV = "1234567890123456"

$required = @(
    "DB_URL",
    "DB_USERNAME",
    "DB_PASSWORD",
    "ADMIN_USERNAME",
    "ADMIN_PASSWORD",
    "GENESYS_CFG_USERNAME",
    "GENESYS_CFG_PASSWORD"
)

$missing = @()
foreach ($name in $required) {
    if (-not [Environment]::GetEnvironmentVariable($name)) {
        $missing += $name
    }
}

if ($missing.Count -gt 0) {
    Write-Host "필수 환경변수가 없습니다: $($missing -join ', ')" -ForegroundColor Yellow
    Write-Host "실행 전에 환경변수를 설정하세요" -ForegroundColor Yellow
    exit 1
}

if ($env:CCC_SERVICE_ENC_ENABLED -eq "true") {
    if (-not $env:CCC_SERVICE_ENC_KEY -or -not $env:CCC_SERVICE_ENC_IV) {
        Write-Host "암호화가 활성화되었지만 CCC_SERVICE_ENC_KEY/IV가 없습니다." -ForegroundColor Yellow
        exit 1
    }
}

Write-Host "프로파일 $env:SPRING_PROFILES_ACTIVE" -ForegroundColor Cyan
Write-Host "앱을 시작합니다.." -ForegroundColor Cyan

mvn spring-boot:run
