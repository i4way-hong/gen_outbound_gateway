#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SDK_DIR="${SDK_DIR:-${SCRIPT_DIR}/../lib}"
POM_DIR="${POM_DIR:-${SDK_DIR}/pom}"

if [[ ! -d "$SDK_DIR" ]]; then
  echo "Genesys SDK 디렉토리를 찾을 수 없습니다: $SDK_DIR" >&2
  exit 1
fi
if [[ ! -d "$POM_DIR" ]]; then
  echo "Genesys SDK POM 디렉토리를 찾을 수 없습니다: $POM_DIR" >&2
  exit 1
fi

for bom in appblocks-bom.pom protocols-bom.pom; do
  bom_path="$POM_DIR/$bom"
  if [[ -f "$bom_path" ]]; then
    echo "Installing BOM: $bom"
    mvn -q install:install-file -Dfile="$bom_path" -DpomFile="$bom_path" -Dpackaging=pom
  else
    echo "WARN: BOM POM을 찾지 못했습니다: $bom_path" >&2
  fi
done

shopt -s nullglob
for jar in "$SDK_DIR"/*.jar; do
  base_name="$(basename "$jar" .jar)"
  pom_path="$POM_DIR/$base_name.pom"
  if [[ -f "$pom_path" ]]; then
    echo "Installing JAR: $(basename "$jar")"
    mvn -q install:install-file -Dfile="$jar" -DpomFile="$pom_path" -Dpackaging=jar
  else
    echo "WARN: POM이 없어 설치를 건너뜀: $(basename "$jar")" >&2
  fi
done

printf "Genesys SDK 로컬 설치 완료\n"
