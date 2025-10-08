// 去中心化游戏平台 - 主JavaScript文件

// 全局变量
let web3;
let userAccount;
let isWalletConnected = false;

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    console.log('去中心化游戏平台已加载');
    
    // 初始化Web3
    initWeb3();
    
    // 检查钱包连接状态
    checkWalletConnection();
    
    // 绑定事件
    bindEvents();

    // 基于后端登录状态切换导航菜单
    refreshAuthMenusFromServer();
});

// 初始化Web3
async function initWeb3() {
    if (typeof window.ethereum !== 'undefined') {
        try {
            web3 = new Web3(window.ethereum);
            console.log('Web3已初始化');
        } catch (error) {
            console.error('Web3初始化失败:', error);
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
        showNotification('钱包连接失败: ' + error.message, 'error');
    } finally {
        hideLoading();
    }
}

// 断开钱包连接
function disconnect() {
    userAccount = null;
    isWalletConnected = false;
    updateWalletUI();
    showNotification('已断开钱包连接', 'info');
    
    // 刷新页面
    setTimeout(() => {
        location.reload();
    }, 1000);
}

// 钱包登录（统一为后端生成消息 + personal_sign）
async function walletLogin() {
    try {
        // 1) 向后端获取用于签名的登录消息
        const msgResp = await fetch('/api/wallet/login-message', { headers: { 'Accept': 'application/json' } });
        if (!msgResp.ok) {
            throw new Error('获取登录消息失败');
        }
        const msgBody = await msgResp.json();
        const message = (msgBody && msgBody.data) ? msgBody.data : null;
        if (!message) {
            throw new Error('登录消息为空');
        }

        // 2) 使用 ethereum.request 优先签名（避免 web3.js 的参数校验问题）；失败再回退
        let signature;
        try {
            signature = await window.ethereum.request({
                method: 'personal_sign',
                params: [message, userAccount]
            });
        } catch (e1) {
            signature = await web3.eth.personal.sign(message, userAccount);
        }

        // 3) 提交到统一的后端登录接口
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
        if (result && result.success) {
            console.log('用户登录成功:', result.data);
            // 存储用户信息到本地存储
            localStorage.setItem('user', JSON.stringify(result.data));
            // 更新页面用户信息
            updateUserInfo(result.data);
        } else {
            throw new Error((result && result.message) || '登录请求失败');
        }
    } catch (error) {
        console.error('登录失败:', error);
        showNotification('登录失败: ' + error.message, 'error');
    }
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
                
                // 不再自动登录，避免页面跳转或模块切换时弹窗
                // 用户可通过显式点击“连接钱包”按钮触发登录
            }
        } catch (error) {
            console.error('检查钱包连接状态失败:', error);
        }
    }
}

// 更新钱包UI
function updateWalletUI() {
    const connectButton = document.querySelector('[onclick="connectWallet()"]');
    const userDropdown = document.querySelector('.dropdown');
    const walletStatus = document.querySelector('.wallet-status');
    
    if (isWalletConnected && userAccount) {
        // 隐藏连接按钮，显示用户信息
        if (connectButton) {
            connectButton.style.display = 'none';
        }
        
        // 显示钱包状态
        if (walletStatus) {
            walletStatus.textContent = `已连接: ${formatAddress(userAccount)}`;
            walletStatus.classList.add('connected');
        }
        
        console.log('钱包UI已更新');
    } else {
        // 显示连接按钮，隐藏用户信息
        if (connectButton) {
            connectButton.style.display = 'block';
        }
        
        // 隐藏钱包状态
        if (walletStatus) {
            walletStatus.classList.remove('connected');
        }
    }
}

// 更新用户信息
function updateUserInfo(user) {
    // 更新导航栏用户名
    const usernameSpan = document.querySelector('.navbar-nav span');
    if (usernameSpan) {
        usernameSpan.textContent = user.username || formatAddress(user.walletAddress);
    }
}

// 格式化地址
function formatAddress(address) {
    if (!address) return '';
    return `${address.substring(0, 6)}...${address.substring(address.length - 4)}`;
}

// 绑定事件
function bindEvents() {
    // 监听账户变化
    if (window.ethereum) {
        window.ethereum.on('accountsChanged', function(accounts) {
            if (accounts.length === 0) {
                disconnect();
            } else if (accounts[0] !== userAccount) {
                userAccount = accounts[0];
                // 不自动签名登录，提示用户手动登录
                showNotification('检测到钱包账户变化，请手动重新登录', 'info');
                updateWalletUI();
            }
        });

        // 监听网络变化
        window.ethereum.on('chainChanged', function(chainId) {
            console.log('网络已切换:', chainId);
            // 可以在这里添加网络切换的处理逻辑
        });
    }

    // 游戏生成表单
    const gameForm = document.getElementById('gameGenerationForm');
    if (gameForm) {
        gameForm.addEventListener('submit', handleGameGeneration);
    }
}

// 处理游戏生成
async function handleGameGeneration(event) {
    event.preventDefault();
    
    if (!isWalletConnected) {
        showNotification('请先连接钱包', 'warning');
        return;
    }

    const description = document.getElementById('gameDescription').value;
    const title = document.getElementById('gameTitle').value;
    
    if (!description.trim()) {
        showNotification('请输入游戏描述', 'warning');
        return;
    }

    try {
        // 显示生成进度
        document.getElementById('generationProgress').style.display = 'block';
        document.getElementById('generationResult').style.display = 'none';
        
        // 模拟进度更新
        updateProgress(0);
        setTimeout(() => updateProgress(30), 1000);
        setTimeout(() => updateProgress(60), 2000);
        setTimeout(() => updateProgress(90), 3000);
        
        // 这里应该调用实际的AI生成API
        setTimeout(() => {
            updateProgress(100);
            showGenerationResult({
                title: title || 'AI生成的游戏',
                type: '动作游戏',
                description: '基于您的描述生成的游戏'
            });
        }, 4000);
        
    } catch (error) {
        console.error('游戏生成失败:', error);
        showNotification('游戏生成失败: ' + error.message, 'error');
    }
}

// 更新进度条
function updateProgress(percent) {
    const progressBar = document.querySelector('.progress-bar');
    if (progressBar) {
        progressBar.style.width = percent + '%';
    }
}

// 显示生成结果
function showGenerationResult(gameData) {
    document.getElementById('generationProgress').style.display = 'none';
    document.getElementById('generationResult').style.display = 'block';
    
    document.getElementById('generatedGameTitle').textContent = gameData.title;
    document.getElementById('generatedGameType').textContent = gameData.type;
    document.getElementById('generatedGameDesc').textContent = gameData.description;
}

// 保存游戏
function saveGame() {
    if (!isWalletConnected) {
        showNotification('请先连接钱包', 'warning');
        return;
    }
    
    showNotification('游戏保存成功！', 'success');
    setTimeout(() => {
        window.location.href = '/assets';
    }, 1500);
}

// 试玩游戏
function playGame() {
    showNotification('启动游戏...', 'info');
    // 这里应该打开游戏窗口或跳转到游戏页面
}

// 重新生成游戏
function regenerateGame() {
    document.getElementById('generationResult').style.display = 'none';
    showNotification('请重新描述您的游戏创意', 'info');
}

// 显示通知
function showNotification(message, type = 'info') {
    // 创建通知元素
    const notification = document.createElement('div');
    notification.className = `alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show position-fixed`;
    notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    
    notification.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(notification);
    
    // 自动移除通知
    setTimeout(() => {
        if (notification.parentNode) {
            notification.parentNode.removeChild(notification);
        }
    }, 5000);
}

// 显示加载状态
function showLoading(message = '加载中...') {
    let loadingOverlay = document.getElementById('loadingOverlay');
    
    if (!loadingOverlay) {
        loadingOverlay = document.createElement('div');
        loadingOverlay.id = 'loadingOverlay';
        loadingOverlay.className = 'loading-overlay';
        loadingOverlay.innerHTML = `
            <div class="loading-spinner">
                <div class="spinner-border text-primary mb-3" role="status"></div>
                <div>${message}</div>
            </div>
        `;
        document.body.appendChild(loadingOverlay);
    }
    
    loadingOverlay.style.display = 'flex';
}

// 隐藏加载状态
function hideLoading() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.style.display = 'none';
    }
}

// 工具函数
const Utils = {
    // 格式化代币数量
    formatTokenAmount: function(amount, decimals = 18) {
        return parseFloat(amount / Math.pow(10, decimals)).toFixed(4);
    },
    
    // 格式化时间
    formatTime: function(timestamp) {
        return new Date(timestamp * 1000).toLocaleString();
    },
    
    // 复制到剪贴板
    copyToClipboard: function(text) {
        navigator.clipboard.writeText(text).then(() => {
            showNotification('已复制到剪贴板', 'success');
        }).catch(err => {
            console.error('复制失败:', err);
            showNotification('复制失败', 'error');
        });
    },
    
    // 验证以太坊地址
    isValidAddress: function(address) {
        return /^0x[a-fA-F0-9]{40}$/.test(address);
    }
};

// 导出给全局使用
window.connectWallet = connectWallet;
window.disconnect = disconnect;
window.logout = async function logout(){
    try{
        // 调用后端登出（可选）
        await fetch('/api/auth/logout',{method:'POST'}).catch(()=>{});
    }finally{
        // 清除本地信息
        localStorage.removeItem('user');
        localStorage.removeItem('authType');
        // 清除TOKEN Cookie
        document.cookie = 'TOKEN=; Max-Age=0; path=/';
        showNotification('已退出登录','success');
        setTimeout(()=>{ window.location.href = '/auth/login?from=' + encodeURIComponent(window.location.pathname + window.location.search); }, 800);
    }
}
window.saveGame = saveGame;
window.playGame = playGame;
window.regenerateGame = regenerateGame;
window.Utils = Utils;

// 根据后端状态渲染导航的登录/用户菜单
async function refreshAuthMenusFromServer(){
    try{
        const res = await fetch('/api/auth/me',{headers:{'Accept':'application/json'}});
        if(!res.ok){
            toggleMenus(null);
            return;
        }
        const body = await res.json();
        const user = body && (body.data || body);
        toggleMenus(user);
    }catch(e){
        toggleMenus(null);
    }
}

function toggleMenus(user){
    const authMenu = document.getElementById('authMenu');
    const userMenu = document.getElementById('userMenu');
    if(authMenu && userMenu){
        if(user){
            authMenu.style.display = 'none';
            userMenu.style.display = 'block';
            const nameEl = userMenu.querySelector('.username');
            if(nameEl){ nameEl.textContent = user.username || (user.walletAddress ? formatAddress(user.walletAddress) : '用户'); }
        }else{
            authMenu.style.display = 'block';
            userMenu.style.display = 'none';
        }
    }
}
