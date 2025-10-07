// 认证相关JavaScript功能

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    console.log('认证模块已加载');

    // 绑定表单提交事件
    bindAuthEvents();

    // 初始化表单验证
    initFormValidation();
});

// 防止并发钱包请求的全局标志
let isWalletConnecting = false;

// 绑定认证相关事件
function bindAuthEvents() {
    // 登录表单提交
    const loginForm = document.getElementById('accountLoginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleAccountLogin);
    }

    // 注册表单提交
    const registerForm = document.getElementById('accountRegisterForm');
    if (registerForm) {
        registerForm.addEventListener('submit', handleAccountRegister);
    }

    // 钱包注册表单提交
    const walletRegisterForm = document.getElementById('walletRegisterForm');
    if (walletRegisterForm) {
        walletRegisterForm.addEventListener('submit', handleWalletRegister);
    }
}

// 账号登录处理
async function handleAccountLogin(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const loginData = {
        username: formData.get('username'),
        password: formData.get('password')
    };

    try {
        showLoading('正在登录...');

        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        const result = await response.json();

        if (result && result.code === 200) {
            const payload = result.data || {};
            const user = payload.user || payload; // 兼容两种返回结构
            // 登录成功，保存用户信息
            localStorage.setItem('user', JSON.stringify(user));
            localStorage.setItem('authType', 'account');

            showNotification('登录成功！', 'success');

            // 延迟跳转到首页并提示
            setTimeout(() => {
                window.location.href = '/?login=success';
            }, 1500);
        } else {
            showNotification(result.message || '登录失败', 'error');
        }
    } catch (error) {
        console.error('登录失败:', error);
        showNotification('登录失败，请检查网络连接', 'error');
    } finally {
        hideLoading();
    }
}

// 账号注册处理
async function handleAccountRegister(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const registerData = {
        username: formData.get('username'),
        email: formData.get('email'),
        password: formData.get('password'),
        confirmPassword: formData.get('confirmPassword'),
        walletAddress: formData.get('walletAddress') || null
    };

    // 前端验证
    if (registerData.password !== registerData.confirmPassword) {
        showNotification('两次输入的密码不一致', 'error');
        return;
    }

    if (registerData.password.length < 6) {
        showNotification('密码长度至少6位', 'error');
        return;
    }

    try {
        showLoading('正在注册...');

        const response = await fetch('/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registerData)
        });

        const result = await response.json();

        if (result && result.code === 200) {
            const payload = result.data || {};
            const user = payload.user || payload;
            // 注册成功，保存用户信息
            localStorage.setItem('user', JSON.stringify(user));
            localStorage.setItem('authType', 'account');

            showNotification('注册成功！', 'success');

            // 延迟跳转到首页并提示
            setTimeout(() => {
                window.location.href = '/?login=success';
            }, 1500);
        } else {
            showNotification(result.message || '注册失败', 'error');
        }
    } catch (error) {
        console.error('注册失败:', error);
        showNotification('注册失败，请检查网络连接', 'error');
    } finally {
        hideLoading();
    }
}

// 钱包注册处理
async function handleWalletRegister(event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const registerData = {
        walletAddress: formData.get('walletAddress'),
        username: formData.get('username'),
        email: formData.get('email') || null,
        password: formData.get('password') || null,
        signature: localStorage.getItem('walletSignature'),
        message: localStorage.getItem('walletMessage')
    };

    try {
        showLoading('正在完成注册...');

        const response = await fetch('/api/auth/wallet-register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registerData)
        });

        const result = await response.json();

        if (result.success) {
            // 注册成功，保存用户信息
            localStorage.setItem('user', JSON.stringify(result.data.user));
            localStorage.setItem('authType', 'wallet');

            showNotification('注册成功！', 'success');

            // 延迟跳转到首页
            setTimeout(() => {
                window.location.href = '/';
            }, 1500);
        } else {
            showNotification(result.message || '注册失败', 'error');
        }
    } catch (error) {
        console.error('注册失败:', error);
        showNotification('注册失败，请检查网络连接', 'error');
    } finally {
        hideLoading();
    }
}

// 连接钱包用于注册
async function connectWalletForRegister() {
    if (typeof window.ethereum === 'undefined') {
        showNotification('请先安装MetaMask钱包', 'error');
        window.open('https://metamask.io/', '_blank');
        return;
    }

    try {
        if (isWalletConnecting) {
            showNotification('钱包请求正在处理中，请在钱包中完成或稍后再试', 'warning');
            return;
        }
        isWalletConnecting = true;
        showLoading('正在连接钱包...');

        // 请求账户访问权限
        const accounts = await window.ethereum.request({
            method: 'eth_requestAccounts'
        });

        if (accounts.length > 0) {
            const walletAddress = accounts[0];

            // 显示钱包注册表单
            document.getElementById('walletAddress').value = walletAddress;
            document.getElementById('walletAddressDisplay').textContent = formatAddress(walletAddress);
            document.getElementById('walletRegisterForm').style.display = 'block';

            // 获取签名消息
            const messageResponse = await fetch('/api/wallet/login-message');
            const messageResult = await messageResponse.json();

            if (messageResult.success) {
                const message = messageResult.data;

                // 签名消息
                const signature = await web3.eth.personal.sign(message, walletAddress);

                // 保存签名信息
                localStorage.setItem('walletSignature', signature);
                localStorage.setItem('walletMessage', message);

                showNotification('钱包连接成功！请填写注册信息', 'success');
            }
        }
    } catch (error) {
        console.error('钱包连接失败:', error);
        // MetaMask: -32002 表示已有请求在处理中
        if (error && (error.code === -32002 || String(error.message || '').includes('Already processing'))) {
            showNotification('已有钱包请求在处理中，请在钱包中完成或稍后再试', 'warning');
        } else {
            showNotification('钱包连接失败: ' + error.message, 'error');
        }
    } finally {
        hideLoading();
        isWalletConnecting = false;
    }
}

// 连接钱包用于绑定
async function connectWalletForBinding() {
    if (typeof window.ethereum === 'undefined') {
        showNotification('请先安装MetaMask钱包', 'error');
        window.open('https://metamask.io/', '_blank');
        return;
    }

    try {
        if (isWalletConnecting) {
            showNotification('钱包请求正在处理中，请在钱包中完成或稍后再试', 'warning');
            return;
        }
        isWalletConnecting = true;
        showLoading('正在连接钱包...');

        // 请求账户访问权限
        const accounts = await window.ethereum.request({
            method: 'eth_requestAccounts'
        });

        if (accounts.length > 0) {
            const walletAddress = accounts[0];
            document.getElementById('registerWalletAddress').value = walletAddress;
            document.getElementById('registerWalletAddress').placeholder = formatAddress(walletAddress);
            showNotification('钱包绑定成功！', 'success');
        }
    } catch (error) {
        console.error('钱包连接失败:', error);
        if (error && (error.code === -32002 || String(error.message || '').includes('Already processing'))) {
            showNotification('已有钱包请求在处理中，请在钱包中完成或稍后再试', 'warning');
        } else {
            showNotification('钱包连接失败: ' + error.message, 'error');
        }
    } finally {
        hideLoading();
        isWalletConnecting = false;
    }
}

// 切换密码显示
function togglePassword(inputId) {
    const passwordInput = document.getElementById(inputId);
    const toggleBtn = passwordInput.parentNode.querySelector('.btn');

    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        toggleBtn.innerHTML = '<i class="fas fa-eye-slash"></i>';
    } else {
        passwordInput.type = 'password';
        toggleBtn.innerHTML = '<i class="fas fa-eye"></i>';
    }
}

// 初始化表单验证
function initFormValidation() {
    // 用户名验证
    const usernameInputs = document.querySelectorAll('input[name="username"]');
    usernameInputs.forEach(input => {
        input.addEventListener('blur', function() {
            const username = this.value.trim();
            if (username && username.length < 3) {
                showNotification('用户名长度至少3位', 'warning');
            }
        });
    });

    // 邮箱验证
    const emailInputs = document.querySelectorAll('input[name="email"]');
    emailInputs.forEach(input => {
        input.addEventListener('blur', function() {
            const email = this.value.trim();
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (email && !emailRegex.test(email)) {
                showNotification('请输入有效的邮箱地址', 'warning');
            }
        });
    });

    // 密码强度检查
    const passwordInputs = document.querySelectorAll('input[name="password"]');
    passwordInputs.forEach(input => {
        input.addEventListener('input', function() {
            const password = this.value;
            const strength = checkPasswordStrength(password);
            showPasswordStrength(strength);
        });
    });
}

// 检查密码强度
function checkPasswordStrength(password) {
    let strength = 0;

    if (password.length >= 8) strength++;
    if (/[a-z]/.test(password)) strength++;
    if (/[A-Z]/.test(password)) strength++;
    if (/[0-9]/.test(password)) strength++;
    if (/[^A-Za-z0-9]/.test(password)) strength++;

    return strength;
}

// 显示密码强度
function showPasswordStrength(strength) {
    const strengthIndicator = document.getElementById('passwordStrength');
    if (!strengthIndicator) return;

    let strengthText = '';
    let strengthClass = '';

    switch (strength) {
        case 0:
        case 1:
            strengthText = '很弱';
            strengthClass = 'text-danger';
            break;
        case 2:
            strengthText = '弱';
            strengthClass = 'text-warning';
            break;
        case 3:
            strengthText = '中等';
            strengthClass = 'text-info';
            break;
        case 4:
            strengthText = '强';
            strengthClass = 'text-success';
            break;
        case 5:
            strengthText = '很强';
            strengthClass = 'text-success';
            break;
    }

    strengthIndicator.textContent = `密码强度: ${strengthText}`;
    strengthIndicator.className = strengthClass;
}

// 显示加载状态
function showLoading(message = '加载中...') {
    // 移除现有的加载提示
    hideLoading();

    const loadingDiv = document.createElement('div');
    loadingDiv.id = 'authLoading';
    loadingDiv.className = 'loading-overlay';
    loadingDiv.innerHTML = `
        <div class="loading-content">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <div class="loading-text">${message}</div>
        </div>
    `;

    document.body.appendChild(loadingDiv);
}

// 隐藏加载状态
function hideLoading() {
    const loadingDiv = document.getElementById('authLoading');
    if (loadingDiv) {
        loadingDiv.remove();
    }
}

// 显示通知
function showNotification(message, type = 'info') {
    // 移除现有的通知
    const existingNotification = document.querySelector('.auth-notification');
    if (existingNotification) {
        existingNotification.remove();
    }

    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show auth-notification`;
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    // 插入到页面顶部
    const container = document.querySelector('.container');
    if (container) {
        container.insertBefore(notification, container.firstChild);
    }

    // 自动隐藏
    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}
