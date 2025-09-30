package com.decentralized.gaming.platform.service.blockchain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 区块链服务重构测试
 * 验证接口-实现类结构是否正确
 *
 * @author DecentralizedGamingPlatform
 */
@SpringBootTest
@ActiveProfiles("test")
public class BlockchainServiceRefactoringTest {

    @Test
    public void testServiceInterfacesExist() {
        // 验证所有服务接口都存在
        assertNotNull(BlockchainService.class);
        assertNotNull(BlockchainRetryService.class);
        assertNotNull(BlockchainCacheService.class);
        assertNotNull(PlatformTokenService.class);
        assertNotNull(GameNFTService.class);
        assertNotNull(AgentNFTService.class);
        assertNotNull(MarketplaceService.class);
        assertNotNull(RewardsService.class);
        assertNotNull(EventListeningService.class);
        assertNotNull(ContractConfigService.class);
        assertNotNull(BlockchainMetricsService.class);
        assertNotNull(TransactionMonitoringService.class);
        assertNotNull(TransactionHistoryService.class);
    }

    @Test
    public void testServiceInterfacesAreInterfaces() {
        // 验证所有服务都是接口
        assertTrue(BlockchainService.class.isInterface());
        assertTrue(BlockchainRetryService.class.isInterface());
        assertTrue(BlockchainCacheService.class.isInterface());
        assertTrue(PlatformTokenService.class.isInterface());
        assertTrue(GameNFTService.class.isInterface());
        assertTrue(AgentNFTService.class.isInterface());
        assertTrue(MarketplaceService.class.isInterface());
        assertTrue(RewardsService.class.isInterface());
        assertTrue(EventListeningService.class.isInterface());
        assertTrue(ContractConfigService.class.isInterface());
        assertTrue(BlockchainMetricsService.class.isInterface());
        assertTrue(TransactionMonitoringService.class.isInterface());
        assertTrue(TransactionHistoryService.class.isInterface());
    }

    @Test
    public void testServiceInterfacesHaveMethods() {
        // 验证接口有方法定义
        assertTrue(BlockchainService.class.getDeclaredMethods().length > 0);
        assertTrue(BlockchainRetryService.class.getDeclaredMethods().length > 0);
        assertTrue(BlockchainCacheService.class.getDeclaredMethods().length > 0);
        assertTrue(PlatformTokenService.class.getDeclaredMethods().length > 0);
        assertTrue(GameNFTService.class.getDeclaredMethods().length > 0);
        assertTrue(AgentNFTService.class.getDeclaredMethods().length > 0);
        assertTrue(MarketplaceService.class.getDeclaredMethods().length > 0);
        assertTrue(RewardsService.class.getDeclaredMethods().length > 0);
        assertTrue(EventListeningService.class.getDeclaredMethods().length > 0);
        assertTrue(ContractConfigService.class.getDeclaredMethods().length > 0);
        assertTrue(BlockchainMetricsService.class.getDeclaredMethods().length > 0);
        assertTrue(TransactionMonitoringService.class.getDeclaredMethods().length > 0);
        assertTrue(TransactionHistoryService.class.getDeclaredMethods().length > 0);
    }
}
