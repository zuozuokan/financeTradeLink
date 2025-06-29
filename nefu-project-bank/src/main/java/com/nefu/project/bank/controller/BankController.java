package com.nefu.project.bank.controller;

import com.nefu.project.bank.service.IBankService;
import com.nefu.project.common.result.HttpResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
