package com.nefu.project.bank.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.LoanApplication;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ILoanApplicationMapper extends BaseMapper<LoanApplication> {
    // 自定义根据 uuid 删除
    @Delete("DELETE FROM loan_application_tab WHERE loan_application_uuid = #{uuid}")
    int deleteByUuid(String uuid);

    // 自定义根据 uuid 查询
    @Select("SELECT * FROM loan_application_tab WHERE loan_application_uuid = #{uuid}")
    LoanApplication selectByUuid(String uuid);

    //自定义查找自己所有的申请
    @Select("SELECT * FROM loan_application_tab ")
    List<LoanApplication> getLoanApplicationListByUserUuid();


}
