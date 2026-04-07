# 폐쇄망 개발환경 구성 체크리스트 (Gen Outbound Gateway)

작성일: 2026-04-06  
대상: 인터넷이 차단된 내부(폐쇄망) PC에 개발/검증 환경을 재구성하는 경우

---

## 1) 목표와 기준

- 목표: 폐쇄망 PC에서 `mvn -DskipTests compile` 성공 + 애플리케이션 기동 확인
- 기준:
  - Java 17 / Maven 정상 인식
  - Genesys SDK 로컬 Maven 설치 완료
  - 필수 환경변수 주입 완료
  - `run-app` 또는 `run-jar` 실행 성공

---

## 2) 반입 준비물 (인터넷 가능 구간에서 준비)

### A. 필수 소프트웨어
- JDK 17 설치 파일 (Windows 권장: zip 또는 msi)
- Maven 3.9+ (zip)
- (선택) Git, VS Code, VS Code 확장

### B. 프로젝트/라이브러리
- 프로젝트 소스 전체 (`Gen_Outbound_Gateway`)
- `lib/` 폴더 (Genesys SDK JAR)
- `scripts/` 폴더
  - `install-genesys-sdk-local.ps1|.sh`
  - `run-app*`, `debugging-app*`, `run-jar*`

### C. Maven 의존성 준비
아래 중 하나를 선택:
1. 오프라인 `.m2/repository` 반입
2. 내부 Nexus/Artifactory에 선반영 후 폐쇄망에서 내부 저장소 사용

### D. 운영/개발 변수값
- DB: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- 관리자: `ADMIN_USERNAME`, `ADMIN_PASSWORD`
- 보안: `JWT_SECRET`, `CCC_SERVICE_ENC_KEY`, `CCC_SERVICE_ENC_IV`
- Genesys: `GENESYS_CFG_*`, `OUTBOUND_*`, `GENESYS_STAT_*`, `GENESYS_TSERVER_*`, `GENESYS_SCS_*`

> 민감값은 저장소 커밋 금지. 폐쇄망 내 보안 정책(비밀 저장소, OS 환경변수, 암호화 파일)을 사용.

---

## 3) 폐쇄망 PC 설치 절차 (Windows 기준)

### 3-1. 기본 툴 설치
1. JDK 17 설치
2. `JAVA_HOME` 설정
3. `PATH`에 `%JAVA_HOME%\bin` 추가
4. Maven 설치 후 `PATH`에 Maven `bin` 추가

### 3-1-1. Git 설치 (선택)
1. Git 설치 파일(오프라인 설치본) 실행
2. 설치 후 `git --version`으로 확인
3. 사내 인증서/프록시 정책이 있으면 Git 설정 반영

### 3-1-2. VS Code 설치 (선택)
1. VS Code 설치 파일(User/System Installer) 실행
2. 설치 후 `code --version`으로 확인
3. Workspace 경로를 열어 프로젝트 인덱싱 완료 대기

### 3-1-3. VS Code 확장 설치 (선택)
폐쇄망에서는 `.vsix` 파일 반입 후 설치 권장.

- 필수 권장
  - Java Extension Pack (`vscjava.vscode-java-pack`)
  - Spring Boot Extension Pack (`vmware.vscode-boot-dev-pack`)
  - Maven for Java (`vscjava.vscode-maven`)

- 품질/편의 권장
  - Checkstyle for Java (`shengchen.vscode-checkstyle`)
  - SonarLint (`sonarsource.sonarlint-vscode`)
  - EditorConfig (`EditorConfig.EditorConfig`)

설치 확인 항목:
- Java 파일 열기 시 Language Server 오류 없음
- Maven 프로젝트 탐지 및 `pom.xml` 인식
- Spring Boot 대시보드/런 구성 표시

### 3-1-4. VSIX 반입 패키지 만들기 절차 (인터넷 PC → 폐쇄망)

#### 1) 인터넷 가능한 PC에서 VSIX 수집
1. VS Code Marketplace에서 필요한 확장 페이지 접속
2. 확장별 `.vsix` 파일 다운로드
  - 예: `vscjava.vscode-java-pack`, `vmware.vscode-boot-dev-pack`, `vscjava.vscode-maven`
3. 다운로드한 파일을 한 폴더에 정리
  - 예: `offline-vsix/`

현재 개발 PC 기준 확장 목록은 별도 파일로 관리:
- `docs/works/offline-vsix-extensions-list-2026-04-06.txt`

현재 VS Code 기준 추가 권장(미설치) 확장:
- `redhat.vscode-yaml` (YAML 편집/검증)
- `humao.rest-client` (`.http` API 시나리오 실행)
- `EditorConfig.EditorConfig` (팀 공통 포맷 강제)
- `shengchen.vscode-checkstyle` (Java Checkstyle 룰 즉시 확인)
- `sonarsource.sonarlint-vscode` (정적분석 보조)

> 참고: GitHub Copilot 계열 확장은 폐쇄망 정책상 인증/외부통신 제한 가능성이 높아 선택 항목으로 분리 권장.

권장 폴더 구조:

```text
offline-vsix/
  README.txt
  extensions-list.txt
  checksums.sha256
  vscjava.vscode-java-pack-<version>.vsix
  vmware.vscode-boot-dev-pack-<version>.vsix
  vscjava.vscode-maven-<version>.vsix
  shengchen.vscode-checkstyle-<version>.vsix
  sonarsource.sonarlint-vscode-<version>.vsix
  EditorConfig.EditorConfig-<version>.vsix
```

#### 2) 무결성 정보 생성(권장)
인터넷 PC에서 `.vsix` 파일 체크섬 파일 생성:

```powershell
Set-Location "C:\path\to\offline-vsix"
Get-ChildItem *.vsix | Get-FileHash -Algorithm SHA256 | ForEach-Object { "$($_.Hash)  $($_.Path | Split-Path -Leaf)" } | Set-Content checksums.sha256 -Encoding UTF8
```

자동화 스크립트 사용(권장):

```powershell
Set-Location "D:\project\현대자동차\dev_src\ai상담센터_2026\Gen_Outbound_Gateway"
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\download-vsix-offline.ps1 -ListFile .\docs\works\offline-vsix-extensions-list-2026-04-06.txt -OutputRoot C:\offline-vsix
```

사전 점검(DryRun):

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\download-vsix-offline.ps1 -ListFile .\docs\works\offline-vsix-extensions-list-2026-04-06.txt -OutputRoot C:\offline-vsix -DryRun
```

버전 미지정 항목까지 latest로 시도하려면:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File .\scripts\download-vsix-offline.ps1 -ListFile .\docs\works\offline-vsix-extensions-list-2026-04-06.txt -OutputRoot C:\offline-vsix -AllowNoVersion
```

#### 3) 반입 패키지 작성
1. `offline-vsix` 폴더를 ZIP 압축
2. 내부 반입 매체(보안 승인 USB/중계서버 등)로 폐쇄망 반입
3. 폐쇄망 반입 정책(백신 스캔/승인 절차) 준수

다운로드/저장 경로 권장:
- 인터넷 PC 작업 루트: `C:\offline-vsix\`
- VSIX 저장: `C:\offline-vsix\downloads\`
- 메타파일(`extensions-list.txt`, `checksums.sha256`): `C:\offline-vsix\`
- 최종 반입 ZIP: `C:\offline-vsix\offline-vsix-YYYYMMDD.zip`

마켓플레이스 URL 형식:
- 웹 페이지: `https://marketplace.visualstudio.com/items?itemName=<publisher>.<extension>`
- 직접 VSIX URL(버전 고정): `https://marketplace.visualstudio.com/_apis/public/gallery/publishers/<publisher>/vsextensions/<extension>/<version>/vspackage`

#### 4) 폐쇄망 PC에서 VSIX 설치
VS Code UI 설치:
1. Extensions 뷰 열기
2. `...` 메뉴 → `Install from VSIX...`
3. 반입한 `.vsix` 파일 선택 설치

또는 CLI 설치:

```powershell
Set-Location "D:\path\to\offline-vsix"
code --install-extension .\vscjava.vscode-java-pack-<version>.vsix
code --install-extension .\vmware.vscode-boot-dev-pack-<version>.vsix
code --install-extension .\vscjava.vscode-maven-<version>.vsix
```

#### 5) 설치 후 검증
```powershell
code --list-extensions
```

검증 체크:
- 필수 확장이 목록에 표시됨
- Java 프로젝트 import 성공
- `pom.xml` 인식 및 Maven 태스크 표시
- Spring Boot 실행/디버그 구성 확인

### 3-2. 소스 배치
- 예: `D:\project\현대자동차\dev_src\ai상담센터_2026\Gen_Outbound_Gateway`

### 3-3. Genesys SDK 로컬 Maven 등록
프로젝트 루트에서:

```powershell
Set-Location "D:\project\현대자동차\dev_src\ai상담센터_2026\Gen_Outbound_Gateway"
.\scripts\install-genesys-sdk-local.ps1
```

### 3-4. 환경변수/프로파일 설정
- 로컬 개발: `SPRING_PROFILES_ACTIVE=local` (H2 사용)
- 운영 검증: `SPRING_PROFILES_ACTIVE=prod` + 운영 변수 일체 주입
- JAR 실행 시 권장: `scripts/config/.env.prod` 사용 (`.env.prod.example` 참고)

### 3-5. 컴파일 검증

```powershell
mvn -DskipTests compile
```

### 3-6. 실행 검증
소스 실행:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
.\scripts\run-app.ps1
```

디버그 실행:

```powershell
$env:SPRING_PROFILES_ACTIVE="local"
.\scripts\debugging-app.cmd
```

JAR 실행:

```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:ENV_FILE="./scripts/config/.env.prod"
.\scripts\run-jar.ps1
```

---

## 4) 최초 점검 포인트

- [ ] `java -version`이 17인지 확인
- [ ] `mvn -version` 정상 출력
- [ ] `mvn -DskipTests compile` 성공
- [ ] 기동 로그에서 Spring Boot started 확인
- [ ] `GET /actuator/health` 응답 확인
- [ ] 필요 시 `GET /api/status`로 Genesys 연결 상태 확인

---

## 5) 자주 발생하는 문제와 조치

### 증상 A: `debugging-app.cmd`가 코드 1로 종료
- 원인: 필수 환경변수 누락(DB/ADMIN/GENESYS 비밀번호 등)
- 조치: 스크립트 안내 메시지의 누락 변수 주입 후 재실행

### 증상 B: Maven 의존성 다운로드 실패
- 원인: 폐쇄망에서 외부 저장소 접근 불가
- 조치: `.m2/repository` 반입 또는 내부 Nexus 설정

### 증상 C: Genesys 관련 클래스/아티팩트 미해결
- 원인: 로컬 Maven에 Genesys SDK 미설치
- 조치: `scripts/install-genesys-sdk-local.ps1` 재실행

### 증상 D: JWT/암복호화 오류
- 원인: `JWT_SECRET`, `CCC_SERVICE_ENC_KEY/IV` 누락/오입력
- 조치: 값 재주입 및 길이 규칙 확인 (KEY 32, IV 16)

---

## 6) 권장 운영 방식

1. 폐쇄망 내부 Maven 저장소(Nexus/Artifactory) 운영
2. 표준 `settings.xml` 템플릿 배포
3. 표준 `.env.prod.example` 유지 + 시스템별 `.env.prod` 분리
4. 배포 전 고정 검증 절차
   - `mvn compile` → `mvn verify` → 기동 smoke test

---

## 7) 참고 파일

- `README.md`
- `scripts/install-genesys-sdk-local.ps1`
- `scripts/run-app.ps1`, `scripts/debugging-app.cmd`, `scripts/run-jar.ps1`
- `scripts/config/.env.prod.example`
- `src/main/resources/application.yml`
