package com.decentralized.gaming.platform.controller;

import com.decentralized.gaming.platform.common.Result;
import com.decentralized.gaming.platform.service.IpfsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ipfs")
@Tag(name = "IPFS", description = "IPFS 上传与下载接口")
@RequiredArgsConstructor
public class IpfsController {

    private final IpfsService ipfsService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件到IPFS(Multipart)")
    public Result<IpfsUploadResp> upload(@RequestPart("file") MultipartFile file) {
        String cid = ipfsService.uploadFile(file);
        return Result.success(new IpfsUploadResp(cid, ipfsService.gatewayUrl(cid)));
    }

    @PostMapping(value = "/upload-bytes", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "上传二进制到IPFS")
    public Result<IpfsUploadResp> uploadBytes(@RequestHeader(value = "X-Filename", required = false) String filename,
                                              @RequestBody byte[] body) {
        String cid = ipfsService.uploadBytes(filename != null ? filename : "blob", body);
        return Result.success(new IpfsUploadResp(cid, ipfsService.gatewayUrl(cid)));
    }

    @GetMapping("/download/{cid}")
    @Operation(summary = "通过网关下载IPFS文件")
    public ResponseEntity<byte[]> download(@PathVariable String cid) {
        byte[] bytes = ipfsService.download(cid);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + cid)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    public record IpfsUploadResp(String cid, String url) {}
}

 