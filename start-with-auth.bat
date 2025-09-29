@echo off
echo ========================================
echo  去中心化游戏平台 - 登录注册系统启动
echo ========================================
echo.

echo 正在启动应用...
echo.

REM 检查Java是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到Java，请先安装JDK 17+
    pause
    exit /b 1
)

REM 检查Maven是否安装
mvn -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到Maven，请先安装Maven
    pause
    exit /b 1
)

echo 编译项目...
call mvn clean compile
if errorlevel 1 (
    echo 编译失败，请检查错误信息
    pause
    exit /b 1
)

echo.
echo 启动应用...
echo ========================================
echo 访问地址：
echo   首页: http://localhost:8080/
echo   登录页面: http://localhost:8080/auth/login
echo   注册页面: http://localhost:8080/auth/register
echo   用户仪表板: http://localhost:8080/dashboard
echo   测试页面: http://localhost:8080/test
echo   API文档: http://localhost:8080/swagger-ui.html
echo.
echo 功能说明：
echo   1. 点击导航栏的"连接钱包"进行钱包登录
echo   2. 点击"登录"按钮进行传统登录
echo   3. 点击"注册"按钮进行用户注册
echo   4. 登录成功后会跳转到用户仪表板
echo   5. 使用测试页面验证各项功能
echo.
echo 按 Ctrl+C 停止服务
echo.

call mvn spring-boot:run
