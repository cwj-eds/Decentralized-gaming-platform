package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.dto.AgentMintRequest;
import com.decentralized.gaming.platform.dto.GameMintRequest;
import com.decentralized.gaming.platform.dto.NFTMintResponse;
import com.decentralized.gaming.platform.service.NFTMintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * NFT铸造控制器测试
 *
 * @author DecentralizedGamingPlatform
 */
@WebMvcTest(NFTMintController.class)
class NFTMintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NFTMintService nftMintService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testMintAgentNFT() throws Exception {
        // 准备测试数据
        AgentMintRequest request = new AgentMintRequest();
        request.setCreatorPrivateKey("0x1234567890abcdef");
        request.setAgentName("测试智能体");
        request.setAgentDescription("测试智能体描述");
        request.setAgentType("GPT-4");

        NFTMintResponse response = NFTMintResponse.success(
            BigInteger.ONE,
            "0x1234567890abcdef",
            "0xe7f1725E7734CE288F8367e1Bb143E90bb3F0512",
            "https://ipfs.io/ipfs/QmXXX",
            BigInteger.valueOf(150000)
        );

        when(nftMintService.mintAgentNFT(any(AgentMintRequest.class))).thenReturn(response);

        // 执行测试
        mockMvc.perform(post("/api/nft/agent/mint")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.tokenId").value("1"));
    }

    @Test
    void testMintGameNFT() throws Exception {
        // 准备测试数据
        GameMintRequest request = new GameMintRequest();
        request.setCreatorPrivateKey("0x1234567890abcdef");
        request.setGameName("测试游戏");
        request.setGameDescription("测试游戏描述");
        request.setGameType("RPG");

        NFTMintResponse response = NFTMintResponse.success(
            BigInteger.valueOf(2),
            "0xabcdef1234567890",
            "0x9fE46736679d2D9a65F0992F2272dE9f3c7fa6e0",
            "https://ipfs.io/ipfs/QmYYY",
            BigInteger.valueOf(180000)
        );

        when(nftMintService.mintGameNFT(any(GameMintRequest.class))).thenReturn(response);

        // 执行测试
        mockMvc.perform(post("/api/nft/game/mint")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.tokenId").value("2"));
    }

    @Test
    void testGetAgentMintFee() throws Exception {
        when(nftMintService.getAgentMintFee()).thenReturn(BigInteger.valueOf(1000000000000000000L));

        mockMvc.perform(get("/api/nft/agent/mint-fee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("1000000000000000000"));
    }

    @Test
    void testGetGameMintFee() throws Exception {
        when(nftMintService.getGameMintFee()).thenReturn(BigInteger.valueOf(2000000000000000000L));

        mockMvc.perform(get("/api/nft/game/mint-fee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("2000000000000000000"));
    }

    @Test
    void testVerifyAgentNFTOwnership() throws Exception {
        when(nftMintService.verifyAgentNFTOwnership(BigInteger.ONE, "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6"))
                .thenReturn(true);

        mockMvc.perform(get("/api/nft/agent/1/owner/0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void testVerifyGameNFTOwnership() throws Exception {
        when(nftMintService.verifyGameNFTOwnership(BigInteger.valueOf(2), "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6"))
                .thenReturn(true);

        mockMvc.perform(get("/api/nft/game/2/owner/0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(true));
    }
}
