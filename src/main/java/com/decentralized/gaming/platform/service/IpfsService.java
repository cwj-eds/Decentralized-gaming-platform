package com.decentralized.gaming.platform.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * IPFS 统一服务：封装 Pinata 与本地 Kubo 上传/下载能力
 */
public interface IpfsService {

    /**
     * 上传原始字节到 IPFS（优先 Pinata，失败回退 Kubo）。
     * @param filename 文件名
     * @param content  文件字节
     * @return 返回 CID（不带网关前缀）
     */
    String uploadBytes(String filename, byte[] content);

    /**
     * 上传 Multipart 文件到 IPFS。
     * @param file 文件
     * @return 返回 CID
     */
    String uploadFile(MultipartFile file);

    /**
     * 拼接网关访问 URL。
     */
    String gatewayUrl(String cid);

    /**
     * 通过网关下载（读取）文件字节。
     */
    byte[] download(String cid);
}


