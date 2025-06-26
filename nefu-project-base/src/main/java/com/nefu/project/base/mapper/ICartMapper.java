package com.nefu.project.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Cart;
import com.nefu.project.domain.entity.LoanApplication;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ICartMapper extends BaseMapper<Cart> {
    // 自定义根据 uuid 删除
    @Delete("DELETE FROM cart_tab WHERE cart_uuid = #{uuid}")
    int deleteByUuid(String uuid);

    // 自定义根据 uuid 查询
    @Select("SELECT * FROM cart_tab WHERE cart_uuid = #{uuid}")
    Cart selectByUuid(String uuid);

    //自定义查找自己所有的申请
    @Select("SELECT * FROM cart_tab WHERE cart_user_uuid = #{userUuid}")
    List<Cart> getCartListByUserUuid(String userUuid);

}
