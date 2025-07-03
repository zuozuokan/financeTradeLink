package com.nefu.project.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.nefu.project.domain.entity.LoanApplication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ILoanApplicationMapper extends BaseMapper<LoanApplication> {

    /**
     * 自定义的分页查询方法，换个名称，避免冲突
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @return 分页结果
     */
    @Select("SELECT * FROM loan_application_tab ${ew.customSqlSegment}")
    IPage<LoanApplication> customSelectPage(IPage<LoanApplication> page, @Param("ew") Wrapper<LoanApplication> queryWrapper);
}