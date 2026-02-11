@echo off
REM OpenAPI -> Excel 재생성 스크립트 (CMD)
setlocal
powershell -ExecutionPolicy Bypass -File "%~dp0regenerate-api-excel.ps1"
endlocal
