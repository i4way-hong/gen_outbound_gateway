# Gen Outbound Gateway JAR 실행 스크립트 (PowerShell)
# Java 17 필요, JAR 이름: gen-outbound-gateway-0.0.1-SNAPSHOT.jar

chcp 65001 | Out-Null
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[Console]::InputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8

$scriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$baseDir = $scriptRoot
$parentLibDir = Join-Path $baseDir "..\lib"
$localLibDir = Join-Path $baseDir "lib"

if (-not $env:SPRING_PROFILES_ACTIVE) {
    $env:SPRING_PROFILES_ACTIVE = "prod"
}

if (-not $env:LOGBACK_CONFIG_PATH) {
    $env:LOGBACK_CONFIG_PATH = "./scripts/config/logback-spring.xml"
}

if (-not $env:LOG_DIR) {
    $env:LOG_DIR = "./logs"
}

if (-not $env:SPRING_CONFIG_ADDITIONAL_LOCATION) {
    $configDir = Join-Path $baseDir "config"
    if (Test-Path $configDir) {
        $env:SPRING_CONFIG_ADDITIONAL_LOCATION = "$configDir\"
    }
}

function Import-EnvFile {
    param([string]$Path)

    if (-not (Test-Path $Path)) {
        return
    }

    Get-Content $Path | ForEach-Object {
        $line = $_.Trim()
        if (-not $line -or $line.StartsWith("#")) {
            return
        }
        $pair = $line -split "=", 2
        if ($pair.Count -ne 2) {
            return
        }
        $name = $pair[0].Trim()
        $value = $pair[1].Trim().Trim('"')
        if ($name) {
            [Environment]::SetEnvironmentVariable($name, $value, "Process")
        }
    }
}

if (-not $env:ENV_FILE) {
    $defaultEnv = Join-Path $baseDir "config\.env.prod"
    $fallbackEnv = Join-Path $baseDir "config\.env"
    if (Test-Path $defaultEnv) {
        $env:ENV_FILE = $defaultEnv
    } elseif (Test-Path $fallbackEnv) {
        $env:ENV_FILE = $fallbackEnv
    }
}

if ($env:ENV_FILE) {
    Import-EnvFile $env:ENV_FILE
}

if (-not $env:DB_URL) {
    $env:DB_URL = "jdbc:sqlserver://172.168.30.61:1433;databaseName=RND_TEST;encrypt=true;trustServerCertificate=true"
}
if (-not $env:DB_USERNAME) {
    $env:DB_USERNAME = "RND_USER"
}
if (-not $env:DB_PASSWORD) {
    $env:DB_PASSWORD = "RND_USER"
}

if (-not $env:ADMIN_USERNAME) {
    $env:ADMIN_USERNAME = "admin"
}
if (-not $env:ADMIN_PASSWORD) {
    $env:ADMIN_PASSWORD = "admin123"
}

if (-not $env:JWT_ENABLED) {
    $env:JWT_ENABLED = "false"
}

if (-not $env:GENESYS_CFG_USERNAME) {
    $env:GENESYS_CFG_USERNAME = "default"
}
if (-not $env:GENESYS_CFG_PASSWORD) {
    $env:GENESYS_CFG_PASSWORD = "password"
}

if (-not $env:CCC_SERVICE_ENC_ENABLED) {
    $env:CCC_SERVICE_ENC_ENABLED = "false"
}
if (-not $env:CCC_SERVICE_ENC_KEY) {
    $env:CCC_SERVICE_ENC_KEY = "12345678901234567890123456789012"
}
if (-not $env:CCC_SERVICE_ENC_IV) {
    $env:CCC_SERVICE_ENC_IV = "1234567890123456"
}

$jarPath = $env:JAR_PATH
if (-not $jarPath) {
    $defaultJar = Join-Path $baseDir "gen-outbound-gateway-0.0.1-SNAPSHOT.jar"
    if (Test-Path $defaultJar) {
        $jarPath = $defaultJar
    }
}

if (-not $jarPath -or -not (Test-Path $jarPath)) {
    Write-Host "gen-outbound-gateway-0.0.1-SNAPSHOT.jar를 찾을 수 없습니다. 스크립트와 같은 폴더에 두거나 JAR_PATH를 지정하세요." -ForegroundColor Yellow
    exit 1
}

$loaderPath = $null
if (Test-Path $localLibDir) {
    $loaderPath = (Resolve-Path $localLibDir).Path
} elseif (Test-Path $parentLibDir) {
    $loaderPath = (Resolve-Path $parentLibDir).Path
}

$javaExe = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME "bin\java.exe" } else { "java" }
$javaVersion = & $javaExe -version 2>&1 | Out-String
if ($javaVersion -notmatch '"17\.') {
    Write-Host "Java 17이 필요합니다. 현재 버전: $javaVersion" -ForegroundColor Yellow
}

$required = @(
    "DB_URL",
    "DB_USERNAME",
    "DB_PASSWORD",
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
    Write-Host "config/.env.prod 또는 시스템 환경변수를 설정하세요." -ForegroundColor Yellow
    exit 1
}

if ($env:CCC_SERVICE_ENC_ENABLED -eq "true") {
    if (-not $env:CCC_SERVICE_ENC_KEY -or -not $env:CCC_SERVICE_ENC_IV) {
        Write-Host "암호화가 활성화되었지만 CCC_SERVICE_ENC_KEY/IV가 없습니다." -ForegroundColor Yellow
        exit 1
    }
}

$javaArgs = @()
if ($env:JAVA_OPTS) {
    $javaArgs += $env:JAVA_OPTS -split "\s+"
}
if ($loaderPath) {
    $javaArgs += "-Dloader.path=$loaderPath"
}

Write-Host "프로파일 $env:SPRING_PROFILES_ACTIVE" -ForegroundColor Cyan
Write-Host "JAR 실행: $jarPath" -ForegroundColor Cyan

& $javaExe @javaArgs -jar $jarPath
