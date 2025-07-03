package com.nefu.project.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nefu.project.base.mapper.INewKnowledgeMapper;
import com.nefu.project.base.service.INewKnowledgeService;
import com.nefu.project.domain.entity.Knowledge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewKnowledgeImpl implements INewKnowledgeService {

    @Autowired
    private INewKnowledgeMapper iNewKnowledgeMapper;

    @Override
    public boolean updateKnowledage(String uuid, Knowledge knowledge) {
        LambdaQueryWrapper<Knowledge> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Knowledge::getKnowledgeUuid, uuid);
        Knowledge queryKnowledge = iNewKnowledgeMapper.selectOne(lambdaQueryWrapper);
        queryKnowledge.setKnowledgeTitle(knowledge.getKnowledgeTitle());
        queryKnowledge.setKnowledgeContent(knowledge.getKnowledgeContent());
        queryKnowledge.setKnowledgeCategory(knowledge.getKnowledgeCategory());
        queryKnowledge.setKnowledgeStatus(knowledge.getKnowledgeStatus());
        queryKnowledge.setKnowledgeIsTop(knowledge.getKnowledgeIsTop());
        int update = iNewKnowledgeMapper.update(queryKnowledge, lambdaQueryWrapper);
        if (update > 0) {
            return true;
        }
        return false;
    }
}
