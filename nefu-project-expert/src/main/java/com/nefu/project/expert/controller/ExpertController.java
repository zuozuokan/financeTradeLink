package com.nefu.project.expert.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Expert;
import com.nefu.project.expert.service.IExpertService;
import com.nefu.project.expert.service.IMinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Tag(name = "专家管理")
@RestController
@RequestMapping("api/expert/")
@RequiredArgsConstructor
public class ExpertController {

    @Autowired
    private final IExpertService expertService;

    /**
     * 添加专家信息
     */
    @Operation(summary = "添加专家信息")
    @PostMapping("upload")
    public HttpResult addExpert(
            @RequestParam("UserUuid") String userUuid,
            @RequestPart("headshotFile") MultipartFile headshotFile,
            @RequestPart("certificateFile") MultipartFile certificateFile,
            @RequestPart("data") String expertData) {
        try {
            JSONObject jsonObject = JSONUtil.parseObj(expertData);
            Expert expert = JSONUtil.toBean(jsonObject, Expert.class);
            expertService.addExpert(userUuid, headshotFile,certificateFile, expert);
            return HttpResult.success("添加专家信息成功");
        } catch (Exception e) {
            log.error("解析专家数据失败", e);
            return HttpResult.failed("解析专家数据失败：" + e.getMessage());
        }
    }

    /**
     * 删除专家信息
     */
    @Operation(summary = "删除专家信息")
    @PostMapping("delete")
    public HttpResult deleteExpert(
            @RequestParam("UserUuid") String userUuid,
            @RequestParam("expertUuid") String expertUuid) {
        expertService.deleteExpert(userUuid, expertUuid);
        return HttpResult.success("删除专家信息成功");
    }

    /**
     * 更新专家头像
     */
    @Operation(summary = "更新专家头像")
    @PostMapping("update/headshot")
    public HttpResult updateExpertHeadshot(
            @RequestParam("expertUuid") String expertUuid,
            @RequestPart("headshotFile") MultipartFile headshotFile) {
       expertService.updateExpertHeadshot(expertUuid, headshotFile);
       return HttpResult.success("更新专家头像成功");
    }

    /**
     * 更新专家资质证书
     */
    @Operation(summary = "更新专家资质证书")
    @PostMapping("update/certificate")
    public HttpResult updateExpertCertificate(
            @RequestParam("expertUuid") String expertUuid,
            @RequestPart("certificateFile") MultipartFile certificateFile) {
        expertService.updateExpertCertificate(expertUuid, certificateFile);
        return HttpResult.success("更新专家信息成功");
    }

    /**
     * 更新专家信息
     */
    @Operation(summary = "更新专家信息")
    @PostMapping("update/info")
    public HttpResult updateExpert(
            @RequestParam("expertUuid") String expertUuid,
            @RequestBody Expert expertData) {
        expertService.updateExpert(expertUuid, expertData);
        return HttpResult.success("更新专家信息成功");
    }

    /**
     * 获取专家详情
     */
    @Operation(summary = "获取专家详情")
    @GetMapping("info")
    public HttpResult<Expert> getExpert(@RequestParam("expertUuid") String expertUuid) {
        return HttpResult.success(expertService.getExpert(expertUuid));
    }
}