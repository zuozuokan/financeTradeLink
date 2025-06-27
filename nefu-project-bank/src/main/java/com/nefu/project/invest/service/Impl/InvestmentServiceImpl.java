package com.nefu.project.invest.service.Impl;

import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.domain.entity.InvestmentRecord;
import com.nefu.project.domain.entity.LoanApplication;
import com.nefu.project.invest.mapper.IInvestmentMapper;
import com.nefu.project.invest.service.InvestmentService;
import com.nefu.project.invest.mapper.ILoanApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class InvestmentServiceImpl implements InvestmentService {

    @Autowired
    private IInvestmentMapper investmentMapper;

    @Autowired
    private ILoanApplicationMapper loanApplicationMapper;


    @Override
    public boolean save(InvestmentRecord record) {
        return investmentMapper.insert(record) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return investmentMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(InvestmentRecord record) {
        return investmentMapper.updateById(record) > 0;
    }

    @Override
    public InvestmentRecord getById(Long id) {
        return investmentMapper.selectById(id);
    }

    @Override
    public List<InvestmentRecord> getAll() {
        return investmentMapper.selectList(null);
    }

    @Override
    public Page<LoanApplication> getLoanList(int pageNum, int pageSize, String keyword) {
        Page<LoanApplication> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<LoanApplication> query = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            query.like(LoanApplication::getLoanApplicationPurpose, keyword);
        }

        return loanApplicationMapper.selectPage(page, query);
    }


    @Override
    public Long submitInvestment(String investorUuid, String loanUuid, BigDecimal amount) {
        InvestmentRecord record = InvestmentRecord.builder()
                .investmentRecordUuid(UUID.randomUUID().toString())
                .investmentRecordInvestorUuid(investorUuid)
                .investmentRecordLoanUuid(loanUuid)
                .investmentRecordCreatedTime(new Date())
                .investmentRecordUpdatedTime(new Date())
                .build();

        investmentMapper.insert(record);
        return record.getInvestmentRecordId();
    }

    @Override
    public List<InvestmentRecord> getMyInvestments(String investorUuid) {
        LambdaQueryWrapper<InvestmentRecord> query = new LambdaQueryWrapper<>();
        query.eq(InvestmentRecord::getInvestmentRecordInvestorUuid, investorUuid)
                .orderByDesc(InvestmentRecord::getInvestmentRecordCreatedTime);

        return investmentMapper.selectList(query);
    }
}
