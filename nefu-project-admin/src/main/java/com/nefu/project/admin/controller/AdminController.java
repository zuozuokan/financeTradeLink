package com.nefu.project.admin.controller;

import com.nefu.project.admin.service.IAdminService;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Knowledge;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Tag(name = "管理员接口")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IAdminService iAdminService;

    @SneakyThrows
    @Operation(summary = "审核融资项目")
    @PostMapping("/checkLoan")
    public HttpResult checkLoan(String loanUuid, String adminAdvice){
        if (loanUuid.isEmpty()||adminAdvice.isEmpty()){
            return HttpResult.failed("该项目异常,参数不能为空");
        }
        if (iAdminService.checkLoan(loanUuid,adminAdvice)) {
            return HttpResult.success("审核成功");
        }
        return HttpResult.failed("审核失败");

    }

    @SneakyThrows
    @Operation(summary = "新增内容")
    @PostMapping("/add-knowlodge")
    public HttpResult addKnowledge(Knowledge knowledge){
        if (Objects.isNull(knowledge)) {
            return HttpResult.failed("内容对象不能为空");
        }
        if (iAdminService.addKnowledge(knowledge)) {
            return HttpResult.success("新增成功");
        }
        return HttpResult.failed("新增失败");
    }

    @SneakyThrows
    @Operation(summary = "删除内容")
    @PostMapping("/delete-knowlodge")
    public HttpResult deleteKnowledge(String knowledgeUuid){
        if (Objects.isNull(knowledgeUuid)) {
            return HttpResult.failed("内容Id不能为空");
        }
        if (iAdminService.deleteKnowledge(knowledgeUuid)) {
            return HttpResult.success("删除成功");
        }
        return HttpResult.failed("删除失败");
    }

    @SneakyThrows
    @Operation(summary = "更新内容")
    @PostMapping("/update-knowlodge")
    public HttpResult updateKnowledge(Knowledge knowledge){
        if (Objects.isNull(knowledge)) {
            return HttpResult.failed("内容对象不能为空");
        }
        if (iAdminService.updateKnowledge(knowledge))
            return HttpResult.success("更新成功");
        return HttpResult.failed("更新失败");
    }


}
