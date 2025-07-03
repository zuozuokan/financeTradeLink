package com.nefu.project.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.base.mapper.IKnowledgeMapper;
import com.nefu.project.base.mapper.INewKnowledgeMapper;
import com.nefu.project.base.service.IKnowledgeService;
import com.nefu.project.common.util.JwtUtil;
import com.nefu.project.domain.entity.Knowledge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class KnowledgeServiceImpl implements IKnowledgeService {

    @Autowired
    private IKnowledgeMapper iKnowledgeMapper;

    @Autowired
    private INewKnowledgeMapper iNewKnowledgeMapper;


    @Override
    public boolean publish(Knowledge k, String token) {
        k.setKnowledgeUuid(IdWorker.getIdStr());
        k.setKnowledgeAuthorUuid(JwtUtil.getUuidFromToken(token));
        k.setKnowledgeStatus("pending");
        k.setKnowledgeViews(0);
        k.setKnowledgeLikes(0);
        k.setKnowledgeCreatedTime(new Date());
        k.setKnowledgeUpdatedTime(new Date());
        return iKnowledgeMapper.insert(k) > 0;
    }

    @Override
    public Page<Knowledge> list(int page, int size, String category, String keyword) {
        Page<Knowledge> pg = new Page<>(page, size);
        LambdaQueryWrapper<Knowledge> q = Wrappers.lambdaQuery();
        if (category != null) q.eq(Knowledge::getKnowledgeCategory, category);
        if (keyword != null) q.like(Knowledge::getKnowledgeTitle, keyword);
        q.orderByDesc(Knowledge::getKnowledgeIsTop, Knowledge::getKnowledgeCreatedTime);
        return iKnowledgeMapper.selectPage(pg, q);
    }

    @Override
    public Knowledge getByUuid(String uuid) {
        return iKnowledgeMapper.selectOne(Wrappers.lambdaQuery(Knowledge.class)
                .eq(Knowledge::getKnowledgeUuid, uuid));
    }

    @Override
    public boolean deleteByUuid(String uuid) {
        return iKnowledgeMapper.delete(Wrappers.lambdaQuery(Knowledge.class)
                .eq(Knowledge::getKnowledgeUuid, uuid)) > 0;
    }

    @Override
    public boolean like(String uuid) {
        return iKnowledgeMapper.update(null, Wrappers.lambdaUpdate(Knowledge.class)
                .eq(Knowledge::getKnowledgeUuid, uuid)
                .setSql("knowledge_likes = knowledge_likes + 1")) > 0;
    }

}
