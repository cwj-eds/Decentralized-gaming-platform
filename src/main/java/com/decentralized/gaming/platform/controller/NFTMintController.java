package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.dto.AgentMintRequest;
import com.decentralized.gaming.platform.dto.GameMintRequest;
import com.decentralized.gaming.platform.dto.NFTMintResponse;
import com.decentralized.gaming.platform.service.NFTMintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigInteger;

/**
 * NFT铸造控制器
 * 提供智能体和游戏NFT的铸造API接口
 *
 * @author DecentralizedGamingPlatform
 */
@Slf4j
@RestController
@RequestMapping("/api/nft")
@RequiredArgsConstructor
@Tag(name = "NFT铸造", description = "智能体和游戏NFT铸造相关接口")
public class NFTMintController {

    private final NFTMintService nftMintService;

    /**
     * 铸造智能体NFT
     */
    @PostMapping("/agent/mint")
    @Operation(summary = "铸造智能体NFT", description = "为用户铸造智能体NFT")
    public Result<NFTMintResponse> mintAgentNFT(@Valid @RequestBody AgentMintRequest request) {
        log.info("收到智能体NFT铸造请求: {}", request.getAgentName());
        NFTMintResponse response = nftMintService.mintAgentNFT(request);
        return response.isSuccess() ? Result.success(response) : Result.error(response.getErrorMessage());
    }

    /**
     * 铸造游戏NFT
     */
    @PostMapping("/game/mint")
    @Operation(summary = "铸造游戏NFT", description = "为用户铸造游戏NFT")
    public Result<NFTMintResponse> mintGameNFT(@Valid @RequestBody GameMintRequest request) {
        log.info("收到游戏NFT铸造请求: {}", request.getGameName());
        NFTMintResponse response = nftMintService.mintGameNFT(request);
        return response.isSuccess() ? Result.success(response) : Result.error(response.getErrorMessage());
    }

    /**
     * 使用智能体生成并铸造游戏NFT
     */
    @PostMapping("/game/generate-mint")
    @Operation(summary = "生成并铸造游戏NFT", description = "使用智能体生成游戏内容并铸造NFT")
    public Result<NFTMintResponse> generateAndMintGameNFT(@Valid @RequestBody GameMintRequest request) {
        log.info("收到生成并铸造游戏NFT请求: {}", request.getGameName());
        NFTMintResponse response = nftMintService.generateAndMintGameNFT(request);
        return response.isSuccess() ? Result.success(response) : Result.error(response.getErrorMessage());
    }

    /**
     * 管理员铸造智能体NFT
     */
    @PostMapping("/agent/admin-mint")
    @Operation(summary = "管理员铸造智能体NFT", description = "管理员为指定地址铸造智能体NFT")
    public Result<NFTMintResponse> adminMintAgentNFT(
            @Parameter(description = "管理员私钥") @RequestParam String adminPrivateKey,
            @Parameter(description = "接收者地址") @RequestParam String to,
            @Valid @RequestBody AgentMintRequest request) {
        log.info("收到管理员铸造智能体NFT请求，接收者: {}", to);
        NFTMintResponse response = nftMintService.adminMintAgentNFT(adminPrivateKey, to, request);
        return response.isSuccess() ? Result.success(response) : Result.error(response.getErrorMessage());
    }

    /**
     * 管理员铸造游戏NFT
     */
    @PostMapping("/game/admin-mint")
    @Operation(summary = "管理员铸造游戏NFT", description = "管理员为指定地址铸造游戏NFT")
    public Result<NFTMintResponse> adminMintGameNFT(
            @Parameter(description = "管理员私钥") @RequestParam String adminPrivateKey,
            @Parameter(description = "接收者地址") @RequestParam String to,
            @Valid @RequestBody GameMintRequest request) {
        log.info("收到管理员铸造游戏NFT请求，接收者: {}", to);
        NFTMintResponse response = nftMintService.adminMintGameNFT(adminPrivateKey, to, request);
        return response.isSuccess() ? Result.success(response) : Result.error(response.getErrorMessage());
    }

    /**
     * 获取智能体NFT铸造费用
     */
    @GetMapping("/agent/mint-fee")
    @Operation(summary = "获取智能体铸造费用", description = "查询铸造智能体NFT所需的费用")
    public Result<BigInteger> getAgentMintFee() {
        BigInteger fee = nftMintService.getAgentMintFee();
        return Result.success(fee);
    }

    /**
     * 获取游戏NFT铸造费用
     */
    @GetMapping("/game/mint-fee")
    @Operation(summary = "获取游戏铸造费用", description = "查询铸造游戏NFT所需的费用")
    public Result<BigInteger> getGameMintFee() {
        BigInteger fee = nftMintService.getGameMintFee();
        return Result.success(fee);
    }

    /**
     * 验证智能体NFT所有权
     */
    @GetMapping("/agent/{tokenId}/owner/{ownerAddress}")
    @Operation(summary = "验证智能体NFT所有权", description = "验证指定地址是否拥有指定的智能体NFT")
    public Result<Boolean> verifyAgentNFTOwnership(
            @Parameter(description = "NFT Token ID") @PathVariable BigInteger tokenId,
            @Parameter(description = "拥有者地址") @PathVariable String ownerAddress) {
        boolean isOwner = nftMintService.verifyAgentNFTOwnership(tokenId, ownerAddress);
        return Result.success(isOwner);
    }

    /**
     * 验证游戏NFT所有权
     */
    @GetMapping("/game/{tokenId}/owner/{ownerAddress}")
    @Operation(summary = "验证游戏NFT所有权", description = "验证指定地址是否拥有指定的游戏NFT")
    public Result<Boolean> verifyGameNFTOwnership(
            @Parameter(description = "NFT Token ID") @PathVariable BigInteger tokenId,
            @Parameter(description = "拥有者地址") @PathVariable String ownerAddress) {
        boolean isOwner = nftMintService.verifyGameNFTOwnership(tokenId, ownerAddress);
        return Result.success(isOwner);
    }

    /**
     * 获取智能体NFT信息
     */
    @GetMapping("/agent/{tokenId}/info")
    @Operation(summary = "获取智能体NFT信息", description = "查询指定智能体NFT的详细信息")
    public Result<Object> getAgentNFTInfo(
            @Parameter(description = "NFT Token ID") @PathVariable BigInteger tokenId) {
        Object info = nftMintService.getAgentNFTInfo(tokenId);
        return Result.success(info);
    }

    /**
     * 获取游戏NFT信息
     */
    @GetMapping("/game/{tokenId}/info")
    @Operation(summary = "获取游戏NFT信息", description = "查询指定游戏NFT的详细信息")
    public Result<Object> getGameNFTInfo(
            @Parameter(description = "NFT Token ID") @PathVariable BigInteger tokenId) {
        Object info = nftMintService.getGameNFTInfo(tokenId);
        return Result.success(info);
    }
}
