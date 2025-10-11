### 区块链子系统开发文档（按顺序开发）

本文面向后端开发者，指导如何基于现有接口完善并上线“区块链子系统”。内容涵盖分层设计、逐步开发顺序、验收标准、测试方案、运维与安全要点。整体遵循“先基础设施→只读能力→写入交易→批处理→事件→运维观测”的顺序，确保可控上线与快速回滚。

---

## 0. 基础准备与环境
- **节点与链**: 本地或测试网节点（推荐 Hardhat/Anvil，本项目默认 `http://localhost:8545`、`chainId=31337`）。
- **合约地址**: `application.yml -> app.blockchain.contracts.*` 已配置平台代币、游戏NFT、智能体NFT、市场、奖励的地址。
- **Web3j 依赖**: 已在 `pom.xml`，无需额外添加。
- **配置文件**: `app.blockchain.enabled=true`；确认 `networkUrl/chainId/gasLimit/gasPrice` 合理。
- **关键类**:
  - 配置层：`BlockchainConfig`（Web3j、GasProvider、配置校验）
  - 网关层：`BlockchainController`
  - 合约封装：`com.decentralized.gaming.platform.contracts.*`（Web3j Wrapper）
  - 服务层：`service.blockchain.*`（Token/NFT/Market/Rewards/Batch/Events/Metrics/Cache）

验收标准
- 启动日志显示 Web3j 连接成功，并打印链信息；合约地址校验通过。
- GET `/api/blockchain/health` 返回成功。

---

## 1. 配置层与网络连通性（基础必做）
- 任务
  - 完成 `BlockchainConfig` 中的连接校验与降级策略（enabled=false 时以降级模式运行）。
  - 完成 `ContractAddressService`/`ContractConfigService`：统一提供合约地址、合约元信息。
  - TTL 缓存策略：`BlockchainCacheService` 支持余额、GasPrice、区块号缓存，TTL 取自 `application.yml`。

验收标准
- GET `/api/blockchain/network/info` 返回链ID、最新区块、客户端版本等。
- GET `/api/blockchain/contracts/info` 返回五大合约的地址与基本信息。

---

## 2. 代币（ERC20）模块（优先只读，再写入）
- 服务：`PlatformTokenService`
- 已有接口映射：
  - 信息与查询：GET `/api/blockchain/token/info`
  - 批量转账：POST `/api/blockchain/batch/transfer-tokens`、`/api/batch/token/transfer`
  - 批量授权：POST `/api/batch/token/approve`
- 实现要点
  - 读：`name/symbol/decimals/totalSupply/balanceOf/allowance`
  - 写：`transfer/approve`；签名用私钥凭证（当前接口接收 `privateKey`，生产需替换为服务端签名代理）
  - 失败重试：按 `app.blockchain.retry.*` 实现指数退避（仅对临时错误）

验收标准
- GET `/api/blockchain/token/info` 返回正确。
- 提交一笔小额转账交易，收据成功，余额变化正确（可通过 `TransactionStatusController` 验证）。

---

## 3. NFT 基础服务（ERC721）与铸造
- 服务：`GameNFTService`、`AgentNFTService`、`NFTMintService`
- 控制器：
  - `NFTMintController` 基础路径 `/api/nft`
  - `BlockchainController` 中 also 提供铸造信息/状态（便于运维）
- 已有接口映射
  - 用户铸造：POST `/api/nft/game/mint`、`/api/nft/agent/mint`
  - 生成并铸造（游戏）：POST `/api/nft/game/generate-mint`
  - 管理员铸造：POST `/api/nft/game/admin-mint`、`/api/nft/agent/admin-mint`
  - 费用：GET `/api/nft/game/mint-fee`、`/api/nft/agent/mint-fee`
  - 铸造状态：GET `/api/blockchain/game/mint/status/{txHash}`、`/agent/mint/status/{txHash}`
- 实现要点
  - 铸造参数校验（名称、描述、URI 格式、接收地址、重复校验）
  - 元数据生成（如需）与存储（可扩展为 IPFS）
  - 返回交易哈希、`tokenId`、`tokenURI`

验收标准
- 能正常为测试地址铸造游戏/智能体 NFT，`ownerOf` 与 `tokenURI` 正确。
- 铸造状态接口可依据交易哈希返回成功/失败/确认数。

---

## 4. NFT 转账与授权
- 控制器：
  - 转账 `NFTTransferController` 基础路径 `/api/nft/transfer`
  - 授权 `NFTApprovalController` 基础路径 `/api/nft/approval`
- 已有接口映射（节选）
  - 转账：POST `/game/transfer`、`/game/safe-transfer`、`/agent/transfer`、`/agent/safe-transfer`
  - 批量转账：POST `/game/batch-transfer`、`/agent/batch-transfer`
  - 查询：GET `/game/owner/{tokenId}`、`/game/balance/{address}`、`/agent/owner/{tokenId}`、`/agent/balance/{address}`
  - 授权：POST `/game/approve`、`/game/approve-all`、`/agent/approve`、`/agent/approve-all`
  - 撤销：POST `/game/revoke`、`/game/revoke-all`、`/agent/revoke`、`/agent/revoke-all`
  - 授权查询：GET `/game/approved/{tokenId}`、`/game/approved-for-all`、`/agent/approved/{tokenId}`、`/agent/approved-for-all`
- 实现要点
  - `safeTransferFrom` 对应的 `data` 参数可选；确保回调合约兼容。
  - 地址、`tokenId`、余额与授权额度的边界校验（不存在/不足/非拥有者/未授权）。
  - 历史查询：从链上事件（Transfer/Approval）构建分页结果（可引入轻量索引缓存）。

验收标准
- 转账/授权/撤销的 happy path 全绿；非 happy case 返回明确错误。
- 批量转账/授权成功返回列表结果且支持部分失败上报。

---

## 5. 市场（Marketplace）模块
- 服务：`MarketplaceService`
- 现有接口（批处理方式暴露）：
  - 批量上架：POST `/api/batch/marketplace/list`
  - 批量取消：POST `/api/batch/marketplace/cancel`
  - 信息：GET `/api/blockchain/marketplace/info`
- 实现要点
  - 上架参数校验（`tokenId`、价格、计价币种、过期时间）
  - 授权依赖检查（上架前 `approve/approveAll`）
  - 事件回调/监听（上架、成交、取消）

验收标准
- 能完成上架→查询→取消的完整流程；成交事件可被监听并记录。

---

## 6. 奖励（Rewards）模块
- 服务：`RewardsService`
- 接口：
  - GET `/api/blockchain/rewards/info`
  - POST `/api/blockchain/batch/issue-rewards`
- 实现要点
  - 批量空投代币，支持多目标多金额；签名账户与限额保护。
  - 幂等键与重试策略，防止重复发放。

验收标准
- 一次批量奖励发放成功；重复提交通过幂等键避免重复扣减。

---

## 7. 批处理（Batch）模块与异步执行
- 服务：`BatchOperationService`
- 控制器：`BatchOperationController` 基础路径 `/api/batch`
- 已有接口映射（节选）
  - 代币：POST `/token/transfer`、`/token/approve`
  - NFT：POST `/nft/game/transfer`、`/nft/agent/transfer`、`/nft/game/approve`、`/nft/agent/approve`、`/nft/game/mint`、`/nft/agent/mint`
  - 市场：POST `/marketplace/list`、`/marketplace/cancel`
  - 异步与状态：POST `/async/token/transfer`、GET `/status/{batchId}`、`/operations`、`/operations/active`、DELETE `/operations/{batchId}`
- 实现要点
  - 批次ID、任务明细、状态机（待处理/进行中/成功/失败/取消）、失败原因收集。
  - 线程池隔离、限流与告警。
  - 幂等设计：客户端重试不影响最终一致性。
  - 可选：持久化任务表（已存在迁移脚本可参考）。

验收标准
- 批次状态查询可逐步反映进度；取消能及时停止未执行任务；异步任务执行稳定。

---

## 8. 事件与订阅（Event）模块
- 服务：`EventListeningService`
- 控制器：`EventListeningController` 基础路径 `/api/events`
- 已有接口映射（节选）
  - 控制：POST `/start`、`/stop`、GET `/status`
  - 历史：GET `/historical`、`/historical/transfer`、`/historical/approval`
  - 合约：GET `/game-nft/events`、`/agent-nft/events`、`/marketplace/events`、`/platform-token/events`
  - 订阅：POST `/subscribe`、DELETE `/subscribe/{subscriptionId}`、GET `/subscriptions`、`/subscriptions/{subscriptionId}`、PUT `/subscriptions/{subscriptionId}/pause|resume`
  - 分析：GET `/statistics`、`/analytics/volume`、`/search`、`/address/{address}/events`
- 实现要点
  - 监听断线重连、回溯起点（起始区块）、位点存储（DB/KV）、消费幂等。
  - 历史查询的分页、过滤条件（合约、事件类型、时间范围）。
  - 订阅模型：持久化订阅与推送（可先返回轮询拉取）。

验收标准
- 能稳定监听五大合约关键事件；历史查询可按条件、分页返回；订阅增删改查有效。

---

## 9. 交易状态与网络信息
- 控制器：`TransactionStatusController` 基础路径 `/api/transaction`
- 已有接口映射（节选）
  - GET `/status/{txHash}`、`/receipt/{txHash}`、`/details/{txHash}`、`/confirmations/{txHash}`
  - POST `/wait-confirmation/{txHash}`、`/batch/status`
  - GET `/history/{address}`、`/history/{address}/contract/{contractAddress}`、`/history/{address}/nft`
  - GET `/network/status`、`/network/gas-price`
- 实现要点
  - 等待确认：可设置确认数阈值与超时；返回最终状态。
  - 地址历史：结合事件与交易列表，必要时走轻量缓存与游标。

验收标准
- 任意交易哈希可正确返回状态/收据；等待确认在超时时间内给出明确结果。

---

## 10. 运维与观测（Metrics/Cache/服务状态）
- 服务：`BlockchainMetricsService`、`BlockchainCacheService`
- 接口：
  - GET `/api/blockchain/services/status`、`/metrics`、`/cache/stats`
  - POST `/api/blockchain/cache/clear`、`/metrics/reset`
- 实现要点
- 指标：交易耗时、成功率、失败类型分布、事件延迟、重试次数、节点健康。
- 缓存：命中率、大小、过期策略；可按类型清理。

验收标准
- 指标面板（可选接入 Prometheus/Grafana）直观看到负载与错误；缓存可清理，命中率可观测。

---

## 11. 安全与合规（贯穿式）
- 私钥传递：当前接口以 `privateKey` 形式入参，仅限测试。生产需：
  - 服务端托管签名账户或引入签名网关，前端仅提交签名消息。
  - 限制管理员操作接口（RBAC/白名单/IP 保护/二次确认）。
- 速率限制与防重放：对交易提交/批处理接口加限流与 nonce 管理、防重放校验。
- 参数校验与数据清洗：地址、`tokenId`、金额、URI 规范化；错误分级返回。

---

## 12. 测试与验收策略
- 单测：对 `service.blockchain.*` 的读写方法做 mock/web3j stub 单测。
- 集成测试：本地 Hardhat 起链，使用测试账户完成转账、铸造、授权、事件监听。
- 回归清单：
  - 代币：余额/授权/转账（含批量）
  - NFT：铸造/转账/授权/撤销/信息查询（含批量）
  - 市场：上架/取消（可打桩）
  - 奖励：批量发放
  - 事件：实时/历史/订阅
  - 交易状态：状态/收据/确认数/历史
  - 运维：指标/缓存/服务状态
- 示例（本地）：
```bash
# 健康检查
curl -s http://localhost:8080/api/blockchain/health

# 查询账户余额
curl -s http://localhost:8080/api/blockchain/account/0xYourAddress/balance

# 铸造游戏NFT（测试）
curl -s -X POST "http://localhost:8080/api/nft/game/mint" -H "Content-Type: application/json" -d '{"gameName":"Demo","gameDescription":"Desc","gameImageUrl":"https://...","gameUrl":"https://..."}'
```

---

## 13. 上线与回滚
- 分阶段开关：通过 `app.blockchain.enabled` 与分模块开关（可扩展）灰度启用。
- 回滚策略：出问题立即 `enabled=false` 降级；批处理/事件监听支持一键停止。
- 数据一致性：写操作失败要具备重试与人工补单工具（后续 CLI/管理页可补充）。

---

## 14. 里程碑与顺序建议（可按 Sprint 执行）
1) 配置与连通性（第 0–1 节）  
2) 代币只读→写入（第 2 节）  
3) NFT 铸造（第 3 节）  
4) NFT 转账与授权（第 4 节）  
5) 市场与奖励（第 5–6 节）  
6) 批处理（第 7 节）  
7) 事件与订阅（第 8 节）  
8) 交易状态与运维（第 9–10 节）  
9) 安全加固与测试（第 11–12 节）  
10) 上线与回滚预案（第 13 节）

---

### 可交付物清单（每步最少交付）
- 配置与网关：`application.yml` 更新、`BlockchainConfig`、`/health`、`/network/info`
- 代币服务：`PlatformTokenService` 读写、`/token/info`、一笔成功转账
- NFT：铸造（含管理员）、`/mint-fee`、`/mint/status`、转账与授权全链路
- 市场与奖励：上架/取消/发放奖励，事件可见
- 批处理：批次状态与取消、并行稳定性
- 事件：实时与历史、订阅生命周期
- 运维：指标、缓存、服务状态
- 安全：签名代理/权限/限流
- 测试：单测覆盖核心分支、集成测试跑通所有主流程

---

备注：以上模块与接口来自仓库现有代码（控制器与服务层），本文档按“顺序开发”给出实施路径、验收标准与关键注意事项。可结合 `docs/blockchain-service-architecture.md` 与 `docs/blockchain-service-refactoring.md` 一起查看整体架构与演进设计。


