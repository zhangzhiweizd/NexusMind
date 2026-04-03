# NexusMind PowerShell 启动脚本
# 用法：.\start.ps1 [dev|prod]

param(
    [string]$Environment = "dev"
)

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  NexusMind 启动脚本" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "当前环境：$Environment" -ForegroundColor Green
Write-Host ""

if ($Environment -eq "dev") {
    Write-Host "启动开发环境..." -ForegroundColor Yellow
    mvn spring-boot:run
}
elseif ($Environment -eq "prod") {
    Write-Host "启动生产环境..." -ForegroundColor Yellow
    mvn clean package -Pprod -DskipTests
    java -jar target/NexusMind-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
}
else {
    Write-Host "未知环境：$Environment" -ForegroundColor Red
    Write-Host ""
    Write-Host "用法：.\start.ps1 [dev|prod]" -ForegroundColor White
    Write-Host ""
    Write-Host "示例:" -ForegroundColor White
    Write-Host "  .\start.ps1 dev    - 启动开发环境" -ForegroundColor Gray
    Write-Host "  .\start.ps1 prod   - 启动生产环境" -ForegroundColor Gray
    exit 1
}
