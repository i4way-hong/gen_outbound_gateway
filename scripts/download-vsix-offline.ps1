<#
.SYNOPSIS
VS Code 확장 목록 파일을 기반으로 오프라인 반입용 VSIX 패키지를 다운로드합니다.

.DESCRIPTION
- 입력 목록 형식:
  <extension-id>@<version> | <marketplace-url>
  예) vscjava.vscode-maven@0.45.1 | https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-maven

- extension-id만 있고 버전이 없으면(예: EditorConfig.EditorConfig), 기본적으로 다운로드를 건너뜁니다.
- -AllowNoVersion 을 지정하면 itemName URL(vspackage/latest)로 시도합니다.

.PARAMETER ListFile
확장 목록 파일 경로

.PARAMETER OutputRoot
다운로드 산출물 루트 경로

.PARAMETER AllowNoVersion
버전이 없는 라인도 latest 주소로 다운로드 시도

.PARAMETER DryRun
다운로드 없이 처리 계획만 출력
#>
[CmdletBinding()]
param(
    [string]$ListFile = "./docs/works/offline-vsix-extensions-list-2026-04-06.txt",
    [string]$OutputRoot = "C:\offline-vsix",
    [switch]$AllowNoVersion,
    [switch]$DryRun
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Write-Info([string]$Message) {
    Write-Host "[INFO] $Message" -ForegroundColor Cyan
}

function Write-Warn([string]$Message) {
    Write-Host "[WARN] $Message" -ForegroundColor Yellow
}

function ConvertTo-ExtensionEntry([string]$line) {
    $raw = $line.Trim()
    if ([string]::IsNullOrWhiteSpace($raw)) { return $null }
    if ($raw.StartsWith("#")) { return $null }

    $parts = $raw -split "\|", 2
    $left = $parts[0].Trim()
    $url = if ($parts.Count -ge 2) { $parts[1].Trim() } else { "" }

    $id = $left
    $version = $null

    if ($left -match "^([^@]+)@(.+)$") {
        $id = $Matches[1].Trim()
        $version = $Matches[2].Trim()
    }

    if ([string]::IsNullOrWhiteSpace($id)) {
        return $null
    }

    return [pscustomobject]@{
        Id      = $id
        Version = $version
        UrlHint = $url
    }
}

function Get-VsixDownloadUrl([string]$extensionId, [string]$version, [bool]$allowNoVersion) {
    if ($extensionId -notmatch "^([^.]+)\.(.+)$") {
        throw "확장 ID 형식이 잘못되었습니다: $extensionId"
    }

    $publisher = $Matches[1]
    $extension = $Matches[2]

    if (-not [string]::IsNullOrWhiteSpace($version)) {
        return "https://marketplace.visualstudio.com/_apis/public/gallery/publishers/$publisher/vsextensions/$extension/$version/vspackage"
    }

    if ($allowNoVersion) {
        return "https://marketplace.visualstudio.com/_apis/public/gallery/publishers/$publisher/vsextensions/$extension/latest/vspackage"
    }

    return $null
}

if (-not (Test-Path -LiteralPath $ListFile)) {
    throw "목록 파일을 찾을 수 없습니다: $ListFile"
}

$outputRootFull = (Resolve-Path (Split-Path -Parent $OutputRoot) -ErrorAction SilentlyContinue)
if (-not $outputRootFull) {
    $parent = Split-Path -Parent $OutputRoot
    if ([string]::IsNullOrWhiteSpace($parent)) { $parent = "." }
    New-Item -ItemType Directory -Path $parent -Force | Out-Null
}

New-Item -ItemType Directory -Path $OutputRoot -Force | Out-Null
$downloadDir = Join-Path $OutputRoot "downloads"
$metaDir = Join-Path $OutputRoot "metadata"
New-Item -ItemType Directory -Path $downloadDir -Force | Out-Null
New-Item -ItemType Directory -Path $metaDir -Force | Out-Null

$lines = Get-Content -LiteralPath $ListFile -Encoding UTF8
$entries = New-Object System.Collections.Generic.List[object]

foreach ($line in $lines) {
    $entry = ConvertTo-ExtensionEntry $line
    if ($null -ne $entry) {
        $entries.Add($entry)
    }
}

if ($entries.Count -eq 0) {
    Write-Warn "처리할 확장 항목이 없습니다."
    exit 0
}

Write-Info "대상 확장 수: $($entries.Count)"
Write-Info "출력 경로: $OutputRoot"

$results = New-Object System.Collections.Generic.List[object]

foreach ($entry in $entries) {
    try {
    $vsixUrl = Get-VsixDownloadUrl -extensionId $entry.Id -version $entry.Version -allowNoVersion:$AllowNoVersion
        if ([string]::IsNullOrWhiteSpace($vsixUrl)) {
            Write-Warn "버전 없음(건너뜀): $($entry.Id)"
            $results.Add([pscustomobject]@{
                Id        = $entry.Id
                Version   = $entry.Version
                Status    = "Skipped-NoVersion"
                VsixUrl   = ""
                FilePath  = ""
                UrlHint   = $entry.UrlHint
            })
            continue
        }

        $safeName = $entry.Id.Replace("/", ".")
        $ver = if ([string]::IsNullOrWhiteSpace($entry.Version)) { "latest" } else { $entry.Version }
        $fileName = "$safeName-$ver.vsix"
        $filePath = Join-Path $downloadDir $fileName

        if ($DryRun) {
            Write-Info "[DryRun] $($entry.Id) -> $vsixUrl"
            $results.Add([pscustomobject]@{
                Id        = $entry.Id
                Version   = $entry.Version
                Status    = "Planned"
                VsixUrl   = $vsixUrl
                FilePath  = $filePath
                UrlHint   = $entry.UrlHint
            })
            continue
        }

        Write-Info "다운로드: $($entry.Id) ($ver)"
        Invoke-WebRequest -Uri $vsixUrl -OutFile $filePath -UseBasicParsing

        $results.Add([pscustomobject]@{
            Id        = $entry.Id
            Version   = $entry.Version
            Status    = "Downloaded"
            VsixUrl   = $vsixUrl
            FilePath  = $filePath
            UrlHint   = $entry.UrlHint
        })
    }
    catch {
        Write-Warn "실패: $($entry.Id) - $($_.Exception.Message)"
        $results.Add([pscustomobject]@{
            Id        = $entry.Id
            Version   = $entry.Version
            Status    = "Failed"
            VsixUrl   = ""
            FilePath  = ""
            UrlHint   = $entry.UrlHint
        })
    }
}

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$manifestPath = Join-Path $metaDir "download-result-$timestamp.json"
$results | ConvertTo-Json -Depth 4 | Set-Content -LiteralPath $manifestPath -Encoding UTF8

$downloaded = Get-ChildItem -LiteralPath $downloadDir -Filter *.vsix -File -ErrorAction SilentlyContinue
if ($downloaded) {
    $checksumPath = Join-Path $OutputRoot "checksums.sha256"
    $checksumLines = $downloaded | Sort-Object Name | ForEach-Object {
        $hash = (Get-FileHash -LiteralPath $_.FullName -Algorithm SHA256).Hash
        "$hash  $($_.Name)"
    }
    Set-Content -LiteralPath $checksumPath -Value $checksumLines -Encoding UTF8
    Write-Info "체크섬 생성: $checksumPath"
}

$summary = $results | Group-Object Status | Sort-Object Name | ForEach-Object { "$($_.Name)=$($_.Count)" }
Write-Info ("요약: " + ($summary -join ", "))
Write-Info "결과 메타파일: $manifestPath"
