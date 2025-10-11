# 测试游戏创建者ID是否正确
# 用于验证修复后，不同用户创建的游戏是否有正确的creator_id

Write-Host "===========================================" -ForegroundColor Cyan
Write-Host "游戏创建者ID修复验证脚本" -ForegroundColor Cyan
Write-Host "===========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"

# 测试用户1登录
Write-Host "1. 测试用户1登录..." -ForegroundColor Yellow
$user1Login = @{
    username = "alice"
    password = "123456"
} | ConvertTo-Json

try {
    $response1 = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $user1Login -ContentType "application/json" -SessionVariable session1
    if ($response1.code -eq 200) {
        $user1 = $response1.data.user
        $token1 = $response1.data.token
        Write-Host "✓ 用户1登录成功" -ForegroundColor Green
        Write-Host "  用户ID: $($user1.id)" -ForegroundColor Gray
        Write-Host "  用户名: $($user1.username)" -ForegroundColor Gray
        Write-Host "  Token: $($token1.Substring(0, 20))..." -ForegroundColor Gray
    } else {
        Write-Host "✗ 用户1登录失败: $($response1.message)" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "✗ 用户1登录失败: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""

# 用户1创建游戏
Write-Host "2. 用户1创建游戏..." -ForegroundColor Yellow
$gameRequest1 = @{
    description = "测试游戏 - 用户1创建"
    gameType = "ACTION"
    difficulty = "EASY"
    theme = "SPACE"
} | ConvertTo-Json

try {
    $headers1 = @{
        "Authorization" = "Bearer $token1"
        "Content-Type" = "application/json"
    }
    
    $gameResponse1 = Invoke-RestMethod -Uri "$baseUrl/agents/api/game-maker/generate" -Method Post -Body $gameRequest1 -Headers $headers1
    if ($gameResponse1.success) {
        Write-Host "✓ 用户1游戏创建成功" -ForegroundColor Green
        Write-Host "  游戏ID: $($gameResponse1.data.gameId)" -ForegroundColor Gray
        Write-Host "  游戏标题: $($gameResponse1.data.gameTitle)" -ForegroundColor Gray
        $game1Id = $gameResponse1.data.gameId
    } else {
        Write-Host "✗ 用户1游戏创建失败: $($gameResponse1.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ 用户1游戏创建失败: $_" -ForegroundColor Red
}

Write-Host ""

# 测试用户2登录
Write-Host "3. 测试用户2登录..." -ForegroundColor Yellow
$user2Login = @{
    username = "bob"
    password = "123456"
} | ConvertTo-Json

try {
    $response2 = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $user2Login -ContentType "application/json" -SessionVariable session2
    if ($response2.code -eq 200) {
        $user2 = $response2.data.user
        $token2 = $response2.data.token
        Write-Host "✓ 用户2登录成功" -ForegroundColor Green
        Write-Host "  用户ID: $($user2.id)" -ForegroundColor Gray
        Write-Host "  用户名: $($user2.username)" -ForegroundColor Gray
        Write-Host "  Token: $($token2.Substring(0, 20))..." -ForegroundColor Gray
    } else {
        Write-Host "✗ 用户2登录失败: $($response2.message)" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "✗ 用户2登录失败: $_" -ForegroundColor Red
    exit 1
}

Write-Host ""

# 用户2创建游戏
Write-Host "4. 用户2创建游戏..." -ForegroundColor Yellow
$gameRequest2 = @{
    description = "测试游戏 - 用户2创建"
    gameType = "PUZZLE"
    difficulty = "MEDIUM"
    theme = "FANTASY"
} | ConvertTo-Json

try {
    $headers2 = @{
        "Authorization" = "Bearer $token2"
        "Content-Type" = "application/json"
    }
    
    $gameResponse2 = Invoke-RestMethod -Uri "$baseUrl/agents/api/game-maker/generate" -Method Post -Body $gameRequest2 -Headers $headers2
    if ($gameResponse2.success) {
        Write-Host "✓ 用户2游戏创建成功" -ForegroundColor Green
        Write-Host "  游戏ID: $($gameResponse2.data.gameId)" -ForegroundColor Gray
        Write-Host "  游戏标题: $($gameResponse2.data.gameTitle)" -ForegroundColor Gray
        $game2Id = $gameResponse2.data.gameId
    } else {
        Write-Host "✗ 用户2游戏创建失败: $($gameResponse2.message)" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ 用户2游戏创建失败: $_" -ForegroundColor Red
}

Write-Host ""

# 验证游戏创建者
Write-Host "5. 验证游戏创建者ID..." -ForegroundColor Yellow
Write-Host "===========================================" -ForegroundColor Cyan
Write-Host ""

if ($game1Id) {
    Write-Host "游戏1 (ID: $game1Id)" -ForegroundColor Cyan
    Write-Host "  预期创建者: 用户1 (ID: $($user1.id))" -ForegroundColor Gray
    Write-Host "  实际创建者: (需要在数据库中查询)" -ForegroundColor Gray
    Write-Host "  SQL查询: SELECT id, title, creator_id FROM games WHERE id = $game1Id;" -ForegroundColor DarkGray
}

Write-Host ""

if ($game2Id) {
    Write-Host "游戏2 (ID: $game2Id)" -ForegroundColor Cyan
    Write-Host "  预期创建者: 用户2 (ID: $($user2.id))" -ForegroundColor Gray
    Write-Host "  实际创建者: (需要在数据库中查询)" -ForegroundColor Gray
    Write-Host "  SQL查询: SELECT id, title, creator_id FROM games WHERE id = $game2Id;" -ForegroundColor DarkGray
}

Write-Host ""
Write-Host "===========================================" -ForegroundColor Cyan
Write-Host "测试完成！" -ForegroundColor Green
Write-Host "请在数据库中执行上述SQL查询，验证creator_id是否正确" -ForegroundColor Yellow
Write-Host "===========================================" -ForegroundColor Cyan

