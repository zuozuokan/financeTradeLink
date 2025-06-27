package com.nefu.project.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.domain.entity.InvestmentRecord;
import com.nefu.project.domain.entity.LoanApplication;

import java.math.BigDecimal;
import java.util.List;

public interface InvestmentService {


    // 增删改查
    boolean save(InvestmentRecord record);

    boolean deleteById(Long id);

    boolean updateById(InvestmentRecord record);

    InvestmentRecord getById(Long id);

    List<InvestmentRecord> getAll();


    // 获取融资项目
    Page<LoanApplication> getLoanList(int pageNum, int pageSize, String keyword);

    // 提交投资请求
    Long submitInvestment(String investorUuid, String loanUuid, BigDecimal amount);

    // 获取当前用户的投资请求
    List<InvestmentRecord> getMyInvestments(String investorUuid);
}