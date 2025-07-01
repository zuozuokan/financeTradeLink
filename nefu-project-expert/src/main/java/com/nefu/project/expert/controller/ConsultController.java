package com.nefu.project.expert.controller;


import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Consult;

import com.nefu.project.expert.service.IConsultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 咨询预约控制器
 *
 */
@Slf4j
@Tag(name = "咨询预约管理")
@RestController
@RequestMapping("api/consult/")
public class ConsultController {

    @Autowired
    private IConsultService iConsultService;
    /**
     * 更新咨询预约状态
     */
    @Operation(summary = "更新咨询预约状态")
    @PostMapping("update/status")
    public HttpResult updateConsultStatus(@RequestParam String consultUuid, @RequestParam String status) {

        iConsultService.updateConsultStatus(consultUuid, status);
        return HttpResult.success("更新咨询预约状态成功");
    }

    /**
     * 获取用户的咨询预约列表
     */
    @Operation(summary = "获取用户的咨询预约列表")
    @GetMapping("user-list")
    public HttpResult<List<Consult>> getConsultListByUserUuid(@RequestParam String userUuid) {
        List<Consult> consultList = iConsultService.getConsultListByUserUuid(userUuid);
        return HttpResult.success(consultList);
    }

    /**
     * 获取单个咨询预约详情
     */
    @Operation(summary = "获取单个咨询预约详情")
    @GetMapping("detail")
    public HttpResult<Consult> getConsultByConsultUuid(@RequestParam String consultUuid) {
        Consult consult = iConsultService.getConsultByConsultUuid(consultUuid);
        return HttpResult.success(consult);
    }
}