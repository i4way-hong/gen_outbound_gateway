param(
    [string]$SdkDir = (Join-Path $PSScriptRoot "..\lib"),
    [string]$PomDir = (Join-Path $SdkDir "pom")
)

$ErrorActionPreference = "Stop"

if (-not (Test-Path $SdkDir)) {
    throw "Genesys SDK 디렉토리를 찾을 수 없습니다: $SdkDir"
}
if (-not (Test-Path $PomDir)) {
    throw "Genesys SDK POM 디렉토리를 찾을 수 없습니다: $PomDir"
}

$boms = @(
    "appblocks-bom.pom",
    "protocols-bom.pom"
)

foreach ($bom in $boms) {
    $bomPath = Join-Path $PomDir $bom
    if (Test-Path $bomPath) {
        Write-Host "Installing BOM: $bom" -ForegroundColor Cyan
        & mvn -q install:install-file -Dfile="$bomPath" -DpomFile="$bomPath" -Dpackaging=pom
    } else {
        Write-Warning "BOM POM을 찾지 못했습니다: $bomPath"
    }
}

Get-ChildItem -Path $SdkDir -Filter "*.jar" | ForEach-Object {
    $jarPath = $_.FullName
    $pomPath = Join-Path $PomDir ($_.BaseName + ".pom")

    if (Test-Path $pomPath) {
        Write-Host "Installing JAR: $($_.Name)" -ForegroundColor Cyan
        & mvn -q install:install-file -Dfile="$jarPath" -DpomFile="$pomPath" -Dpackaging=jar
    } else {
        Write-Warning "POM이 없어 설치를 건너뜀: $($_.Name)"
    }
}

Write-Host "Genesys SDK 로컬 설치 완료" -ForegroundColor Green
