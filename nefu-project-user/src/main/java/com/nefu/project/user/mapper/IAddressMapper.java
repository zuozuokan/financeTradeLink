package com.nefu.project.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Address;
import com.nefu.project.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAddressMapper extends BaseMapper<Address> {
}
