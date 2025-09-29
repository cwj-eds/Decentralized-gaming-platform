$headers = @{"Content-Type" = "application/json"}
$body = @"
{
    "username": "testuser",
    "email": "test@example.com",
    "password": "test123456",
    "confirmPassword": "test123456"
}
"@

Invoke-WebRequest -Method POST -Uri "http://localhost:8080/api/auth/register" -Headers $headers -Body $body -UseBasicParsing






