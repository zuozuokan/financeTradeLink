package com.nefu.project.expert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Expert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IExpertMapper extends BaseMapper<Expert> {

    // 自定义根据 uuid 查询
    @Select("SELECT * FROM expert_tab WHERE expert_uuid = #{uuid}")
    Expert selectByUuid(String uuid);

    // 自定义根据用户uuid查询专家列表
    @Select("SELECT * FROM expert_tab")
    List<Expert> getExperts();
}