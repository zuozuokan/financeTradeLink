package com.nefu.project.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Address;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAddressMapper  extends BaseMapper<Address> {
}
