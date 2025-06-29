package com.nefu.project.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOrderManageMapper extends BaseMapper<Order> {

}
