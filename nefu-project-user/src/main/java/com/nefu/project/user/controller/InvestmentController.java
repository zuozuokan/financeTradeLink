package com.nefu.project.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.domain.entity.InvestmentRecord;
import com.nefu.project.domain.entity.LoanApplication;
import com.nefu.project.user.dto.InvestRequest;
import com.nefu.project.user.service.InvestmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.nefu.project.common.result.HttpResult;

import java.util.List;

@Tag(name = "投资接口")
@RestController
@RequestMapping("/api/invest")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;


    // 1. 获取融资项目分页列表
    @GetMapping("/listloan")
    public Page<LoanApplication> getLoanList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword
    ) {
        return investmentService.getLoanList(pageNum, pageSize, keyword);
    }

    // 2. 提交投资意向
    @PostMapping("/submit")
    public HttpResult<String> invest(@RequestBody InvestRequest request) {
        Long id = investmentService.submitInvestment(
                request.getInvestorUuid(),
                request.getLoanUuid(),
                request.getAmount()
        );
        return HttpResult.success(id.toString());
    }

    // 3. 获取我的投资记录
    @GetMapping("/my")
    public HttpResult<List<InvestmentRecord>> getMyInvestments(@RequestParam("investorUuid") String investorUuid) {
        // TODO：解析token
        List<InvestmentRecord> list = investmentService.getMyInvestments(investorUuid);
        return HttpResult.success(list);
    }


    // 假设从token中解析用户UUID
    // TODO：解析token
    private String parseInvestorUuidFromToken(String token) {
        // 生产环境应使用 JWT + 拦截器
        return "investor-uuid-from-token"; // 示例返回
    }
}
