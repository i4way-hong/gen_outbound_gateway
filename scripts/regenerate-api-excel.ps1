param(
    [string]$OpenApiUrl = "http://localhost:8080/v3/api-docs",
    [string]$OpenApiOut = "docs/works/api-docs-latest.json",
    [string]$ExcelOut = "docs/works/api-spec-latest.xlsx",
    [int]$MaxAttempts = 12,
    [int]$DelaySeconds = 5
)

$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $PSScriptRoot
$openApiPath = Join-Path $root $OpenApiOut
$excelPath = Join-Path $root $ExcelOut

function Test-OpenApiDownload {
    param([string]$Url, [string]$OutFile)
    try {
        Invoke-WebRequest -Uri $Url -OutFile $OutFile -UseBasicParsing | Out-Null
        return $true
    } catch {
        return $false
    }
}

$started = $false
if (-not (Test-OpenApiDownload -Url $OpenApiUrl -OutFile $openApiPath)) {
    Write-Host "OpenAPI가 실행 중이지 않아 앱을 시작합니다." -ForegroundColor Yellow
    $proc = Start-Process -FilePath "cmd.exe" -ArgumentList "/c", "scripts\debugging-app.cmd" -WorkingDirectory $root -PassThru -WindowStyle Hidden
    $started = $true

    $success = $false
    for ($i = 0; $i -lt $MaxAttempts; $i++) {
        Start-Sleep -Seconds $DelaySeconds
    if (Test-OpenApiDownload -Url $OpenApiUrl -OutFile $openApiPath) {
            $success = $true
            break
        }
    }

    if (-not $success) {
        throw "OpenAPI 다운로드 실패: $OpenApiUrl"
    }
}

$python = Join-Path $root ".venv\Scripts\python.exe"
if (-not (Test-Path $python)) {
    $python = "python"
}

$env:OPENAPI_JSON = $openApiPath
$env:OUTPUT_XLSX = $excelPath

& $python (Join-Path $root "scripts\generate_api_excel.py")

if ($started) {
    Get-CimInstance Win32_Process | Where-Object {
        $_.Name -eq "java.exe" -and $_.CommandLine -like "*gen-outbound-gateway*"
    } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force }

    if ($proc -and (Get-Process -Id $proc.Id -ErrorAction SilentlyContinue)) {
        Stop-Process -Id $proc.Id -Force
    }
}

Write-Host "완료: $excelPath" -ForegroundColor Green
