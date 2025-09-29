// 区块链交互JavaScript文件

// 全局变量
let web3;
let userAccount;
let isWalletConnected = false;
let isWalletRequesting = false; // 防重入：避免并发请求钱包
let currentUser = null;

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    console.log('区块链模块已加载');

    // 初始化Web3
    initWeb3();

    // 检查钱包连接状态
    checkWalletConnection();

    // 绑定事件
    bindBlockchainEvents();
});

// 初始化Web3
async function initWeb3() {
    if (typeof window.ethereum !== 'undefined') {
        try {
            web3 = new Web3(window.ethereum);
            console.log('Web3已初始化');
        } catch (error) {
            console.error('Web3初始化失败:', error);
            showNotification('Web3初始化失败', 'error');
        }
    } else {
        console.warn('未检测到Web3钱包');
        showNotification('请安装MetaMask钱包以使用完整功能', 'warning');
    }
}

// 连接钱包
async function connectWallet() {
    if (typeof window.ethereum === 'undefined') {
        showNotification('请先安装MetaMask钱包', 'error');
        window.open('https://metamask.io/', '_blank');
        return;
    }

    try {
        if (isWalletRequesting) {
            showNotification('已有钱包请求在处理中，请在钱包中完成或稍后再试', 'warning');
            return;
        }
        isWalletRequesting = true;
        showLoading('正在连接钱包...');

        // 请求账户访问权限
        const accounts = await window.ethereum.request({
            method: 'eth_requestAccounts'
        });

        if (accounts.length > 0) {
            userAccount = accounts[0];
            isWalletConnected = true;

            // 更新UI
            updateWalletUI();

            // 执行钱包登录
            await walletLogin();

            showNotification('钱包连接成功！', 'success');
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
        isWalletRequesting = false;
    }
}

// 钱包登录
async function walletLogin() {
    try {
        // 获取登录消息
        const messageResponse = await fetch('/api/wallet/login-message');
        const messageResult = await messageResponse.json();

        if (!messageResult.success) {
            throw new Error('获取登录消息失败');
        }

        const message = messageResult.data;

        // 确保 web3 已初始化
        if (!web3 && window.ethereum) {
            web3 = new Web3(window.ethereum);
        }

        // 签名消息（兼容 MetaMask）
        let signature;
        try {
            signature = await web3.eth.personal.sign(message, userAccount);
        } catch (e) {
            // 回退到 ethereum.request
            signature = await window.ethereum.request({
                method: 'personal_sign',
                params: [message, userAccount]
            });
        }

        // 发送登录请求
        const response = await fetch('/api/wallet/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                walletAddress: userAccount,
                signature: signature,
                message: message
            })
        });

        const result = await response.json();

        if (result.success) {
            currentUser = result.data;
            console.log('用户登录成功:', currentUser);

            // 存储用户信息到本地存储
            localStorage.setItem('user', JSON.stringify(currentUser));

            // 更新页面用户信息
            updateUserInfo(currentUser);

            // 触发登录成功事件
            window.dispatchEvent(new CustomEvent('walletLoginSuccess', {
                detail: { user: currentUser }
            }));
        } else {
            throw new Error(result.message || '登录失败');
        }
    } catch (error) {
        console.error('登录失败:', error);
        showNotification('登录失败: ' + error.message, 'error');
    }
}

// 断开钱包连接
function disconnectWallet() {
    userAccount = null;
    isWalletConnected = false;
    currentUser = null;

    // 清除本地存储
    localStorage.removeItem('user');

    // 更新UI
    updateWalletUI();

    // 触发断开连接事件
    window.dispatchEvent(new CustomEvent('walletDisconnected'));

    showNotification('已断开钱包连接', 'info');

    // 刷新页面
    setTimeout(() => {
        location.reload();
    }, 1000);
}

// 退出登录
function logout() {
    // 清除本地存储的所有认证信息
    localStorage.removeItem('user');
    localStorage.removeItem('authType');
    localStorage.removeItem('walletSignature');
    localStorage.removeItem('walletMessage');

    // 重置全局变量
    userAccount = null;
    isWalletConnected = false;
    currentUser = null;

    // 更新UI
    updateWalletUI();

    showNotification('已退出登录', 'info');

    // 刷新页面
    setTimeout(() => {
        location.reload();
    }, 1000);
}

// 检查钱包连接状态
async function checkWalletConnection() {
    if (typeof window.ethereum !== 'undefined') {
        try {
            const accounts = await window.ethereum.request({
                method: 'eth_accounts'
            });

            if (accounts.length > 0) {
                userAccount = accounts[0];
                isWalletConnected = true;
                updateWalletUI();

                // 检查本地存储的用户信息
                const storedUser = localStorage.getItem('user');
                if (storedUser) {
                    currentUser = JSON.parse(storedUser);
                    updateUserInfo(currentUser);
                } else {
                    // 自动登录
                    await walletLogin();
                }
            }
        } catch (error) {
            console.error('检查钱包连接状态失败:', error);
        }
    }
}

// 更新钱包UI
function updateWalletUI() {
    const connectButton = document.querySelector('[onclick="connectWallet()"]');
    const disconnectButton = document.querySelector('[onclick="disconnectWallet()"]');
    const walletStatus = document.querySelector('.wallet-status');
    const userDropdown = document.querySelector('.user-dropdown');

    if (isWalletConnected && userAccount) {
        // 隐藏连接按钮，显示断开按钮
        if (connectButton) {
            connectButton.style.display = 'none';
        }
        if (disconnectButton) {
            disconnectButton.style.display = 'block';
        }

        // 显示钱包状态
        if (walletStatus) {
            walletStatus.textContent = `已连接: ${formatAddress(userAccount)}`;
            walletStatus.classList.add('connected');
        }

        // 显示用户下拉菜单
        if (userDropdown) {
            userDropdown.style.display = 'block';
        }

        console.log('钱包UI已更新');
    } else {
        // 显示连接按钮，隐藏断开按钮
        if (connectButton) {
            connectButton.style.display = 'block';
        }
        if (disconnectButton) {
            disconnectButton.style.display = 'none';
        }

        // 隐藏钱包状态
        if (walletStatus) {
            walletStatus.classList.remove('connected');
        }

        // 隐藏用户下拉菜单
        if (userDropdown) {
            userDropdown.style.display = 'none';
        }
    }
}

// 更新用户信息
function updateUserInfo(user) {
    // 更新导航栏用户名
    const usernameSpan = document.querySelector('.navbar-nav .username');
    if (usernameSpan) {
        usernameSpan.textContent = user.username || formatAddress(user.walletAddress);
    }

    // 更新用户头像
    const avatarImg = document.querySelector('.navbar-nav .user-avatar');
    if (avatarImg && user.avatarUrl) {
        avatarImg.src = user.avatarUrl;
    }
}

// 格式化地址
function formatAddress(address) {
    if (!address) return '';
    return `${address.substring(0, 6)}...${address.substring(address.length - 4)}`;
}

// 绑定区块链事件
function bindBlockchainEvents() {
    // 监听账户变化
    if (window.ethereum) {
        window.ethereum.on('accountsChanged', function(accounts) {
            if (accounts.length === 0) {
                disconnectWallet();
            } else if (accounts[0] !== userAccount) {
                userAccount = accounts[0];
                walletLogin();
            }
        });

        // 监听网络变化
        window.ethereum.on('chainChanged', function(chainId) {
            console.log('网络已切换:', chainId);
            showNotification('网络已切换，请重新连接', 'warning');
            disconnectWallet();
        });
    }
}

// 获取当前用户
function getCurrentUser() {
    return currentUser;
}

// 检查是否已连接钱包
function isWalletConnectedState() {
    return isWalletConnected && userAccount !== null;
}

// 获取当前账户
function getCurrentAccount() {
    return userAccount;
}

// 导出给全局使用
window.connectWallet = connectWallet;
window.disconnectWallet = disconnectWallet;
window.logout = logout;
window.getCurrentUser = getCurrentUser;
window.isWalletConnected = isWalletConnectedState;
window.getCurrentAccount = getCurrentAccount;
window.formatAddress = formatAddress;