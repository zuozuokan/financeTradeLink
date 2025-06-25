package com.nefu.project.user.service;


import com.nefu.project.domain.entity.LoanApplication;

import java.math.BigDecimal;
import java.util.List;

public interface ILoanApplicationService {
    void addLoanApplication(String userUuid , LoanApplication loadApplication);
    void deteleLoanApplication(String userUuid,String loanApplicationUuid);
    void updateLoanApplicationStatus(String loanApplicatinUuid,String loanApplicationNewstatus);
    void updateLoanApplicationRepayPlan(String loanApplicatinUuid,String loanApplicationRepayPlan);
    void updateLoanApplicationAmount(String loanApplicatinUuid, BigDecimal loanApplicationAmount);
    void updateLoanApplicationPurpose(String loanApplicatinUuid,String loanApplicationPurpose);
    List<LoanApplication> getLoanApplicationList(String userUuid);
    LoanApplication getLoanApplication(String loanApplicationUuid);
}
