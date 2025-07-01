package com.nefu.project.expert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Consult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IConsultMapper extends BaseMapper<Consult> {
    // 自定义根据 uuid 删除
    @Delete("DELETE FROM consult_tab WHERE consult_uuid = #{uuid}")
    int deleteByUuid(String uuid);

    // 自定义根据 uuid 查询
    @Select("SELECT * FROM consult_tab WHERE consult_uuid = #{uuid}")
    Consult selectByUuid(String uuid);

    //自定义查找自己所有的申请
    @Select("SELECT * FROM consult_tab WHERE consult_user_uuid = #{userUuid}")
    List<Consult> getConsultListByUserUuid(String userUuid);
}
