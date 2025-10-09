package com.decentralized.gaming.platform.service.impl;

import com.decentralized.gaming.platform.service.IpfsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class IpfsServiceImpl implements IpfsService {

    private final RestTemplate restTemplate = new RestTemplate();

    // 统一公网关
    @Value("${app.ipfs.gateway:https://ipfs.io/ipfs/}")
    private String publicGateway;

    // Pinata
    @Value("${app.ipfs.pinata.enabled:true}")
    private boolean pinataEnabled;
    @Value("${app.ipfs.pinata.base-url:https://api.pinata.cloud}")
    private String pinataBaseUrl;
    @Value("${app.ipfs.pinata.jwt:}")
    private String pinataJwt;
    @Value("${app.ipfs.pinata.gateway:}")
    private String pinataGateway;

    // 本地 Kubo
    @Value("${app.ipfs.kubo.enabled:true}")
    private boolean kuboEnabled;
    @Value("${app.ipfs.kubo.api-url:http://127.0.0.1:5001/api/v0}")
    private String kuboApiUrl;

    @Override
    public String uploadBytes(String filename, byte[] content) {
        // 优先 Pinata
        if (pinataEnabled && pinataJwt != null && !pinataJwt.isEmpty()) {
            try {
                String cid = pinataPinFile(filename, content);
                if (cid != null) return cid;
            } catch (Exception e) {
                log.warn("Pinata 上传失败，回退 Kubo: {}", e.getMessage());
            }
        }
        // 回退 Kubo
        if (kuboEnabled) {
            return kuboAdd(filename, content);
        }
        throw new IllegalStateException("IPFS 上传失败：Pinata 与 Kubo 均不可用");
    }

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            return uploadBytes(file.getOriginalFilename(), file.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("读取上传文件失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String gatewayUrl(String cid) {
        String base = (pinataEnabled && pinataGateway != null && !pinataGateway.isEmpty()) ? pinataGateway : publicGateway;
        if (!base.endsWith("/")) base = base + "/";
        return base + cid;
    }

    @Override
    public byte[] download(String cid) {
        String url = gatewayUrl(cid);
        ResponseEntity<byte[]> resp = restTemplate.getForEntity(URI.create(url), byte[].class);
        if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
            throw new RuntimeException("下载失败: " + url);
        }
        return resp.getBody();
    }

    private String pinataPinFile(String filename, byte[] content) {
        String url = pinataBaseUrl + "/pinning/pinFileToIPFS";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(pinataJwt.replace("Bearer ", ""));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResourceWithFilename(content, filename));

        // 可选: metadata/pinataOptions
        body.add("pinataMetadata", new HttpEntity<>("{\"name\":\"" + safeName(filename) + "\"}", jsonHeaders()));

        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, Object>> resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                req,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            Object hash = resp.getBody().get("IpfsHash");
            if (hash != null) return String.valueOf(hash);
        }
        throw new RuntimeException("Pinata 返回异常");
    }

    private String kuboAdd(String filename, byte[] content) {
        String url = kuboApiUrl.endsWith("/") ? kuboApiUrl + "add" : kuboApiUrl + "/add";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResourceWithFilename(content, filename));

        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, Object>> resp = restTemplate.exchange(
                url,
                HttpMethod.POST,
                req,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
            Object hash = resp.getBody().get("Hash");
            if (hash != null) return String.valueOf(hash);
        }
        throw new RuntimeException("Kubo 返回异常");
    }

    private static String safeName(String name) {
        if (name == null) return "file";
        return name.replaceAll("[^a-zA-Z0-9_.-]", "_");
    }

    private static HttpHeaders jsonHeaders() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8));
        return h;
    }

    /**
     * Multipart ByteArrayResource 并携带文件名
     */
    static class ByteArrayResourceWithFilename extends org.springframework.core.io.ByteArrayResource {
        private final String filename;
        ByteArrayResourceWithFilename(byte[] byteArray, String filename) {
            super(byteArray);
            this.filename = (filename == null || filename.isEmpty()) ? "file" : filename;
        }
        @Override
        public String getFilename() {
            return filename;
        }
    }
}


