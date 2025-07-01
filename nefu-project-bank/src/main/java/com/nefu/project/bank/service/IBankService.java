package com.nefu.project.bank.service;

import com.nefu.project.domain.entity.LoanApplication;

import java.util.List;

public interface IBankService {
    /**
     * @description: 审核融资项目
     * @param: [loanUuid]
     * @return: boolean
     */
    boolean checkLoan(String loanUuid,String adminAdvice);
    List<LoanApplication> getLoanApplicationList(String userUuid);
    LoanApplication getLoanApplication(String loanApplicationUuid);
}
