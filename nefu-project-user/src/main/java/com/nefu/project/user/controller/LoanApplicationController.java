package com.nefu.project.user.controller;

import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.LoanApplication;
import com.nefu.project.user.mapper.ILoanApplicationMapper;
import com.nefu.project.user.service.ILoanApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Tag(name = "融资申请管理")
@RestController
@RequestMapping("api/application/")
public class LoanApplicationController {

    @Autowired
    private ILoanApplicationService iLoanApplicationService;

    /**
     * 添加贷款申请
     */
    @Operation(summary = "添加融资申请")
    @PostMapping("add")
    public HttpResult addLoanApplication(@RequestParam String userUuid, @RequestBody LoanApplication loanApplication) {
        iLoanApplicationService.addLoanApplication(userUuid, loanApplication);
        return HttpResult.success("添加融资申请成功");
    }

    /**
     * 删除贷款申请
     */
    @Operation(summary = "删除贷款申请")
    @PostMapping("detele")
    public HttpResult deleteLoanApplication(@RequestParam String userUuid,@RequestParam String loanApplicationUuid) {
        iLoanApplicationService.deteleLoanApplication(userUuid, loanApplicationUuid);
        return HttpResult.success("删除融资申请成功");
    }

    /**
     * 更新贷款申请状态
     */
    @Operation(summary = "更新贷款申请状态,用户仅支持取消“cancelled")
    @PostMapping("update/status")
    public HttpResult updateLoanApplicationStatus( @RequestParam String loanApplicationUuid,@RequestParam String loanApplicationNewstatus) {
        iLoanApplicationService.updateLoanApplicationStatus(loanApplicationUuid, loanApplicationNewstatus);
        return HttpResult.success("更新贷款申请状态成功");
    }

    /**
     * 更新贷款还款计划
     */
    @Operation(summary = "更新贷款申请还款计划")
    @PostMapping("update/repay-plan")
    public HttpResult updateLoanApplicationRepayPlan(@RequestParam String loanApplicationUuid, @RequestParam String loanApplicationRepayPlan) {
        iLoanApplicationService.updateLoanApplicationRepayPlan(loanApplicationUuid, loanApplicationRepayPlan);
        return HttpResult.success("更新贷款申请还款计划成功");
    }

    /**
     * 更新贷款申请金额
     */
    @Operation(summary = "更新贷款申请金额")
    @PostMapping("update/amount")
    public HttpResult updateLoanApplicationAmount(@RequestParam String loanApplicationUuid, @RequestParam BigDecimal loanApplicationAmount) {
        iLoanApplicationService.updateLoanApplicationAmount(loanApplicationUuid, loanApplicationAmount);
        return HttpResult.success("更新贷款申请金额成功");
    }

    /**
     * 更新贷款申请用途
     */
    @Operation(summary = "更新贷款申请用途")
    @PostMapping("update/purpose")
    public HttpResult updateLoanApplicationPurpose(@RequestParam String loanApplicationUuid,@RequestParam String loanApplicationPurpose) {
        iLoanApplicationService.updateLoanApplicationPurpose(loanApplicationUuid, loanApplicationPurpose);
        return HttpResult.success("更新贷款申请用途成功");
    }

    /**
     * 获取该用户的所有贷款申请列表
     */
    @Operation(summary = "获取该用户的所有贷款申请列表")
    @GetMapping("user-list")
    public HttpResult<List<LoanApplication>> getLoanApplicationList(@RequestParam String userUuid) {

        List<LoanApplication> loanApplications = iLoanApplicationService.getLoanApplicationList(userUuid);

        return HttpResult.success(loanApplications);
    }

    @Operation(summary = "获取所有贷款申请列表")
    @GetMapping("all-list")
    public HttpResult<List<LoanApplication>> getAllLoanApplicationList() {
        List<LoanApplication> loanApplications = iLoanApplicationService.getAllLoanApplicationList();
        return HttpResult.success(loanApplications);
    }

    /** 
     * @description: 获取单个贷款申请详情 
     * @param: [loanApplicationUuid] 
     * @return: com.nefu.project.common.result.HttpResult<com.nefu.project.domain.entity.LoanApplication> 
     */
    @Operation(summary = "获取单个贷款申请详情")
    @GetMapping("/information")
    public HttpResult<LoanApplication> getLoanApplication(@RequestParam String loanApplicationUuid) {
        LoanApplication loanApplication = iLoanApplicationService.getLoanApplication(loanApplicationUuid);
        return HttpResult.success(loanApplication);
    }

    /**
     * 审核贷款申请
     */
    @Operation(summary = "审核贷款申请")
    @PostMapping("review")
    public HttpResult reviewLoanApplication(@RequestParam String loanApplicationUuid, @RequestParam String loanApplicationNewstatus) {
        iLoanApplicationService.reviewLoanApplication(loanApplicationUuid, loanApplicationNewstatus);
        return HttpResult.success("审核贷款申请成功");
    }

}