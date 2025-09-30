try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/auth/login" -UseBasicParsing
    Write-Host "✅ 登录页面访问成功!" -ForegroundColor Green
    Write-Host "状态码: " $response.StatusCode
    Write-Host "内容长度: " $response.Content.Length
} catch {
    Write-Host "❌ 登录页面访问失败: " $_.Exception.Message -ForegroundColor Red
}

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/auth/register" -UseBasicParsing
    Write-Host "✅ 注册页面访问成功!" -ForegroundColor Green
    Write-Host "状态码: " $response.StatusCode
    Write-Host "内容长度: " $response.Content.Length
} catch {
    Write-Host "❌ 注册页面访问失败: " $_.Exception.Message -ForegroundColor Red
}

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/" -UseBasicParsing
    Write-Host "✅ 首页访问成功!" -ForegroundColor Green
    Write-Host "状态码: " $response.StatusCode
    Write-Host "内容长度: " $response.Content.Length
} catch {
    Write-Host "❌ 首页访问失败: " $_.Exception.Message -ForegroundColor Red
}






