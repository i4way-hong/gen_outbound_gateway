# Gen Outbound Gateway - k6 scenario runner
# Prerequisite: k6 installed and target server running

$env:K6_EXE="C:\Program Files\k6\k6.exe"

$ErrorActionPreference = 'Stop'

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$scenarios = @(
  'scenarios/01-health-load.js',
  'scenarios/02-auth-login-stress.js',
  'scenarios/03-outbound-status-spike.js',
  'scenarios/04-scs-sse-soak.js'
)

function Resolve-K6Executable {
  if ($env:K6_EXE -and (Test-Path $env:K6_EXE)) {
    return $env:K6_EXE
  }

  $k6Cmd = Get-Command k6 -ErrorAction SilentlyContinue
  if ($k6Cmd) {
    return $k6Cmd.Source
  }

  $localK6 = Join-Path $root 'tools\k6\k6.exe'
  if (Test-Path $localK6) {
    return $localK6
  }

  return $null
}

$k6Exe = Resolve-K6Executable

Write-Host '[INFO] PerformanceTest start' -ForegroundColor Cyan
Write-Host "[INFO] BASE_URL: $($env:BASE_URL)" -ForegroundColor Cyan
Write-Host "[INFO] K6_EXE: $($env:K6_EXE)" -ForegroundColor Cyan

if (-not $k6Exe) {
  Write-Host '[ERROR] k6 executable not found.' -ForegroundColor Red
  Write-Host '[HINT] 1) Install k6 and add it to PATH.' -ForegroundColor Yellow
  Write-Host '[HINT] 2) Set $env:K6_EXE="C:\path\to\k6.exe"' -ForegroundColor Yellow
  Write-Host '[HINT] 3) Or place k6.exe at .\PerformanceTest\tools\k6\k6.exe' -ForegroundColor Yellow
  exit 1
}

Write-Host "[INFO] k6 executable: $k6Exe" -ForegroundColor Cyan

foreach ($scenario in $scenarios) {
  $path = Join-Path $root $scenario
  Write-Host "[RUN] k6 run $path" -ForegroundColor Yellow
  & $k6Exe run $path
}

Write-Host '[DONE] PerformanceTest complete' -ForegroundColor Green
