package com.nefu.project.bank.controller;

import com.nefu.project.bank.service.IBankService;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.LoanApplication;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private IBankService iBankService;

    @Operation(summary = "审核融资项目")
    @PostMapping("/checkLoan")
    public HttpResult<String> checkLoan(String loanUuid, String bankAdvice){
        if (loanUuid.isEmpty()||bankAdvice.isEmpty()){
            return HttpResult.failed("该项目异常,参数不能为空");
        }
        if (iBankService.checkLoan(loanUuid,bankAdvice)) {
            return HttpResult.success("审核成功");
        }
        return HttpResult.failed("审核失败");

    }
    /**
     * 获取所有贷款申请列表
     */
    @Operation(summary = "获取所有贷款申请列表")
    @GetMapping("loan-application-list")
    public HttpResult<List<LoanApplication>> getLoanApplicationList(@RequestParam String userUuid) {

        List<LoanApplication> loanApplications = iBankService.getLoanApplicationList(userUuid);

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
        LoanApplication loanApplication = iBankService.getLoanApplication(loanApplicationUuid);
        return HttpResult.success(loanApplication);
    }

}
