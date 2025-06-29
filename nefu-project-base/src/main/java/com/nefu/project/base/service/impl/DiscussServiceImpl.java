package com.nefu.project.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nefu.project.base.mapper.IDiscussMapper;
import com.nefu.project.base.mapper.IKnowledgeMapper;
import com.nefu.project.base.service.IDiscussService;
import com.nefu.project.domain.entity.Discuss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DiscussServiceImpl implements IDiscussService {
    @Autowired
    private IDiscussMapper mapper;
    @Autowired
    private IKnowledgeMapper iKnowledgeMapper;

    @Override
    public boolean comment(Discuss d) {
        d.setDiscussUuid(UUID.randomUUID().toString());
        return mapper.insert(d) > 0;
    }

    @Override
    public List<Discuss> listByKnowledge(String uuid) {
        Long kId = iKnowledgeMapper.getIdByUuid(uuid);
        return mapper.selectList(Wrappers.lambdaQuery(Discuss.class)
                .eq(Discuss::getDiscussKnowledgeUuid, kId)
                .orderByAsc(Discuss::getDiscussCreatedTime));
    }

    @Override
    public boolean like(String uuid) {
        return mapper.update(null, Wrappers.lambdaUpdate(Discuss.class)
                .eq(Discuss::getDiscussUuid, uuid)
                .setSql("discuss_likes = discuss_likes + 1")) > 0;
    }

    @Override
    public boolean delete(String uuid) {
        return mapper.update(null, Wrappers.lambdaUpdate(Discuss.class)
                .eq(Discuss::getDiscussUuid, uuid)
                .set(Discuss::getDiscussStatus, "0")) > 0;
    }
}
