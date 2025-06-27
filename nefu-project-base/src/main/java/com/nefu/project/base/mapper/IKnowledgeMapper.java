package com.nefu.project.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Knowledge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IKnowledgeMapper extends BaseMapper<Knowledge> {

    @Select("SELECT knowledge_id FROM knowledge_tab WHERE knowledge_uuid = #{uuid} LIMIT 1")
    Long getIdByUuid(@Param("uuid") String uuid);

}
