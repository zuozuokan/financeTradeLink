package com.nefu.project.bank.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.LoanApplication;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IBankMapper extends BaseMapper<LoanApplication> {
    /**
     * @description: 审核融资项目
     * @param: [loanUuid]
     * @return: boolean
     */
    boolean checkLoan(String loanUuid,String adminAdvice);
}
