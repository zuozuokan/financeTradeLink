package com.nefu.project.user.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestRequest {
    private String investorUuid;
    private String loanUuid;
    private BigDecimal amount;
}