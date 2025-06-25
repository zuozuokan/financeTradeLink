package com.nefu.project.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Address;
import com.nefu.project.domain.entity.LoanApplication;
import com.nefu.project.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IAddressMapper extends BaseMapper<Address> {
//    // 自定义根据 uuid 查询
//    @Select("SELECT * FROM user_tab WHERE user_uuid = #{uuid}")
//    User selectByUuid(String uuid);
}
