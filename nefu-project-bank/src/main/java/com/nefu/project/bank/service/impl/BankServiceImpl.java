package com.nefu.project.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nefu.project.bank.mapper.IBankMapper;
import com.nefu.project.bank.service.IBankService;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.domain.entity.LoanApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl implements IBankService {

    @Autowired
    private IBankMapper iBankMapper;
    /**
     * @description: 更新银行的管理意见
     * @param: [loanUuid, adminAdvice]
     * @return: boolean
     */
    @Override
    public boolean checkLoan(String loanUuid,String BankAdvice) {
        LambdaUpdateWrapper<LoanApplication> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(LoanApplication::getLoanApplicationUuid,loanUuid)
                .set(LoanApplication::getLoanApplicationStatus,BankAdvice);
        try{
            int rows = iBankMapper.update(null,lambdaUpdateWrapper);
            if (rows > 0) {
                return true;
            }
        }catch (DbException e){
            throw new DbException("数据库操作异常");
        }

        return false;
    }
}
