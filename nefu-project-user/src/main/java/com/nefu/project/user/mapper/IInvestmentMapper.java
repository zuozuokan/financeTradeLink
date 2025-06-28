package com.nefu.project.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.InvestmentRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IInvestmentMapper extends BaseMapper<InvestmentRecord> {
}
