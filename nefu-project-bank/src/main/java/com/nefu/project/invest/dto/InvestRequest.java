package com.nefu.project.invest.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvestRequest {
    private String loanUuid;
    private BigDecimal amount;
}
