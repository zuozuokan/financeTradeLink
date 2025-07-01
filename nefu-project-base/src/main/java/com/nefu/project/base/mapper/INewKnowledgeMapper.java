package com.nefu.project.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nefu.project.domain.entity.Knowledge;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface INewKnowledgeMapper extends BaseMapper<Knowledge> {
}
