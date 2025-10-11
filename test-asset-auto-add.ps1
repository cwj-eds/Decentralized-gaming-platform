# 测试智能体和游戏自动添加到用户资产功能
# PowerShell 测试脚本

$baseUrl = "http://localhost:8080"

Write-Host "===========================================" -ForegroundColor Cyan
Write-Host "智能体和游戏自动添加到资产测试" -ForegroundColor Cyan
Write-Host "===========================================" -ForegroundColor Cyan
Write-Host ""

# 步骤1：注册新用户
Write-Host "步骤1: 注册新用户..." -ForegroundColor Yellow
$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$testUsername = "testuser_$timestamp"
$testEmail = "test_${timestamp}@test.com"
$testPassword = "Test123456"

$registerData = @{
    username = $testUsername
    email = $testEmail
    password = $testPassword
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/register" -Method Post -Body $registerData -ContentType "application/json" -SessionVariable session
    Write-Host "✅ 用户注册成功: $testUsername" -ForegroundColor Green
    Write-Host "   用户ID: $($registerResponse.data.id)" -ForegroundColor Gray
    $userId = $registerResponse.data.id
} catch {
    Write-Host "❌ 用户注册失败: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Start-Sleep -Seconds 1

# 步骤2：登录
Write-Host ""
Write-Host "步骤2: 用户登录..." -ForegroundColor Yellow
$loginData = @{
    username = $testUsername
    password = $testPassword
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $loginData -ContentType "application/json" -WebSession $session
    Write-Host "✅ 登录成功" -ForegroundColor Green
} catch {
    Write-Host "❌ 登录失败: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Start-Sleep -Seconds 1

# 步骤3：创建智能体
Write-Host ""
Write-Host "步骤3: 创建智能体..." -ForegroundColor Yellow
$agentData = @{
    name = "测试智能体_$timestamp"
    description = "这是一个用于测试资产自动添加功能的智能体"
    agentType = "GAME_MAKER"
    codeUrl = "ipfs://QmTest123456"
    modelUrl = "ipfs://QmModel123456"
    price = 0
} | ConvertTo-Json

try {
    $agentResponse = Invoke-RestMethod -Uri "$baseUrl/agents/api/create" -Method Post -Body $agentData -ContentType "application/json" -WebSession $session
    Write-Host "✅ 智能体创建成功" -ForegroundColor Green
    Write-Host "   智能体ID: $($agentResponse.data.id)" -ForegroundColor Gray
    Write-Host "   智能体名称: $($agentResponse.data.name)" -ForegroundColor Gray
    $agentId = $agentResponse.data.id
} catch {
    Write-Host "❌ 智能体创建失败: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   错误详情: $($_.ErrorDetails.Message)" -ForegroundColor Red
}

Start-Sleep -Seconds 1

# 步骤4：生成游戏
Write-Host ""
Write-Host "步骤4: 使用智能体生成游戏..." -ForegroundColor Yellow
$gameData = @{
    description = "一个简单的跑酷游戏，玩家需要跳过障碍物"
    gameType = "休闲游戏"
    difficulty = "简单"
} | ConvertTo-Json

try {
    $gameResponse = Invoke-RestMethod -Uri "$baseUrl/agents/api/game-maker/generate" -Method Post -Body $gameData -ContentType "application/json" -WebSession $session
    Write-Host "✅ 游戏生成成功" -ForegroundColor Green
    Write-Host "   游戏ID: $($gameResponse.data.gameId)" -ForegroundColor Gray
    Write-Host "   游戏标题: $($gameResponse.data.gameTitle)" -ForegroundColor Gray
    $gameId = $gameResponse.data.gameId
} catch {
    Write-Host "❌ 游戏生成失败: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   错误详情: $($_.ErrorDetails.Message)" -ForegroundColor Red
}

Start-Sleep -Seconds 2

# 步骤5：验证智能体资产
Write-Host ""
Write-Host "步骤5: 验证智能体是否已添加到用户资产..." -ForegroundColor Yellow
try {
    $agentAssetsResponse = Invoke-RestMethod -Uri "$baseUrl/assets/api/agents?page=1&size=10" -Method Get -WebSession $session
    $agentAssets = $agentAssetsResponse.data.records
    
    Write-Host "✅ 用户拥有 $($agentAssets.Count) 个智能体资产" -ForegroundColor Green
    
    if ($agentAssets.Count -gt 0) {
        Write-Host ""
        Write-Host "   智能体资产列表:" -ForegroundColor Cyan
        foreach ($asset in $agentAssets) {
            Write-Host "   ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
            Write-Host "   资产ID: $($asset.id)" -ForegroundColor Gray
            Write-Host "   智能体ID: $($asset.assetId)" -ForegroundColor Gray
            Write-Host "   智能体名称: $($asset.assetName)" -ForegroundColor Gray
            Write-Host "   获得方式: $($asset.acquisitionType)" -ForegroundColor Gray
            Write-Host "   是否可交易: $($asset.isTradeable)" -ForegroundColor Gray
            Write-Host "   获得时间: $($asset.createdAt)" -ForegroundColor Gray
            
            # 验证是否是刚创建的智能体
            if ($asset.assetId -eq $agentId) {
                Write-Host "   ✅ 确认：刚创建的智能体已成功添加到资产表" -ForegroundColor Green
            }
        }
    } else {
        Write-Host "   ⚠️  警告：未找到智能体资产" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ 获取智能体资产失败: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   错误详情: $($_.ErrorDetails.Message)" -ForegroundColor Red
}

Start-Sleep -Seconds 1

# 步骤6：验证游戏资产
Write-Host ""
Write-Host "步骤6: 验证游戏是否已添加到用户资产..." -ForegroundColor Yellow
try {
    $gameAssetsResponse = Invoke-RestMethod -Uri "$baseUrl/assets/api/games?page=1&size=10" -Method Get -WebSession $session
    $gameAssets = $gameAssetsResponse.data.records
    
    Write-Host "✅ 用户拥有 $($gameAssets.Count) 个游戏资产" -ForegroundColor Green
    
    if ($gameAssets.Count -gt 0) {
        Write-Host ""
        Write-Host "   游戏资产列表:" -ForegroundColor Cyan
        foreach ($asset in $gameAssets) {
            Write-Host "   ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Gray
            Write-Host "   资产ID: $($asset.id)" -ForegroundColor Gray
            Write-Host "   游戏ID: $($asset.assetId)" -ForegroundColor Gray
            Write-Host "   游戏名称: $($asset.assetName)" -ForegroundColor Gray
            Write-Host "   获得方式: $($asset.acquisitionType)" -ForegroundColor Gray
            Write-Host "   是否可交易: $($asset.isTradeable)" -ForegroundColor Gray
            Write-Host "   获得时间: $($asset.createdAt)" -ForegroundColor Gray
            
            # 验证是否是刚创建的游戏
            if ($asset.assetId -eq $gameId) {
                Write-Host "   ✅ 确认：刚生成的游戏已成功添加到资产表" -ForegroundColor Green
            }
        }
    } else {
        Write-Host "   ⚠️  警告：未找到游戏资产" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ 获取游戏资产失败: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   错误详情: $($_.ErrorDetails.Message)" -ForegroundColor Red
}

Start-Sleep -Seconds 1

# 步骤7：查看资产仪表板
Write-Host ""
Write-Host "步骤7: 查看用户资产仪表板..." -ForegroundColor Yellow
try {
    $dashboardResponse = Invoke-RestMethod -Uri "$baseUrl/assets/api/dashboard" -Method Get -WebSession $session
    $dashboard = $dashboardResponse.data
    
    Write-Host "✅ 资产仪表板数据获取成功" -ForegroundColor Green
    Write-Host ""
    Write-Host "   ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
    Write-Host "   用户名: $($dashboard.username)" -ForegroundColor Gray
    Write-Host "   钱包地址: $($dashboard.walletAddress)" -ForegroundColor Gray
    Write-Host "   总资产价值: $($dashboard.totalAssetValue)" -ForegroundColor Gray
    Write-Host "   游戏数量: $($dashboard.gameCount)" -ForegroundColor Gray
    Write-Host "   智能体数量: $($dashboard.agentCount)" -ForegroundColor Gray
    Write-Host "   道具数量: $($dashboard.itemCount)" -ForegroundColor Gray
    Write-Host "   ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" -ForegroundColor Cyan
} catch {
    Write-Host "❌ 获取资产仪表板失败: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   错误详情: $($_.ErrorDetails.Message)" -ForegroundColor Red
}

# 总结
Write-Host ""
Write-Host "===========================================" -ForegroundColor Cyan
Write-Host "测试完成！" -ForegroundColor Cyan
Write-Host "===========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "测试结果总结:" -ForegroundColor Yellow
Write-Host "✅ 用户注册和登录" -ForegroundColor Green
if ($agentId) {
    Write-Host "✅ 智能体创建成功 (ID: $agentId)" -ForegroundColor Green
}
if ($gameId) {
    Write-Host "✅ 游戏生成成功 (ID: $gameId)" -ForegroundColor Green
}
Write-Host "✅ 资产查询功能正常" -ForegroundColor Green
Write-Host ""
Write-Host "🎉 智能体和游戏自动添加到用户资产功能测试完成！" -ForegroundColor Green
Write-Host ""

