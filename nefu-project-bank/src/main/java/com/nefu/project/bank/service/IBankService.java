package com.nefu.project.bank.service;

public interface IBankService {
    /**
     * @description: 审核融资项目
     * @param: [loanUuid]
     * @return: boolean
     */
    boolean checkLoan(String loanUuid,String adminAdvice);
}
