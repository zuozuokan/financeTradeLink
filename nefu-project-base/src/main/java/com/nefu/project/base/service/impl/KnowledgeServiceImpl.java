package com.nefu.project.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.base.mapper.IKnowledgeMapper;
import com.nefu.project.base.mapper.INewKnowledgeMapper;
import com.nefu.project.base.service.IKnowledgeService;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.common.util.JwtUtil;
import com.nefu.project.domain.entity.Knowledge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class KnowledgeServiceImpl implements IKnowledgeService {

    @Autowired
    private IKnowledgeMapper iKnowledgeMapper;

    @Autowired
    private INewKnowledgeMapper iNewKnowledgeMapper;


    @Override
    public boolean publish(Knowledge k, String token) {
        k.setKnowledgeUuid(IdWorker.getIdStr()); // 自动生成19位左右的雪花ID字符串
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
    public boolean like(String knowledgeUuid) {
        return iKnowledgeMapper.update(null, Wrappers.lambdaUpdate(Knowledge.class)
                .eq(Knowledge::getKnowledgeUuid, knowledgeUuid)
                .setSql("knowledge_likes = knowledge_likes + 1")) > 0;
    }
    @Override
    public boolean view(String knowledgeUuid) {
        return iKnowledgeMapper.update(null, Wrappers.lambdaUpdate(Knowledge.class)
                .eq(Knowledge::getKnowledgeUuid, knowledgeUuid)
                .setSql("knowledge_views = knowledge_views + 1")) > 0;
    }

    @Override
    public Page<Knowledge> getKnowledgeByUserUuid(String userUuid) {
        if(userUuid == null||userUuid.trim().isEmpty()) {
            throw new UserException("无法正常获取用户ID");
        }

        try{
            Page<Knowledge> pg = new Page<>(1, 10);
            LambdaQueryWrapper<Knowledge> q = Wrappers.lambdaQuery();
            q.eq(Knowledge::getKnowledgeAuthorUuid, userUuid);
            q.orderByDesc(Knowledge::getKnowledgeIsTop, Knowledge::getKnowledgeCreatedTime);
            Page<Knowledge> knowledgeList= iKnowledgeMapper.selectPage(pg, q);
            if(knowledgeList.getSize() == 0) {
                return null;
            }
            log.info(knowledgeList.toString());
            return knowledgeList;

        }
        catch(DbException e){
            throw new DbException("数据库查询失败"+e.getMessage());
        }
    }
}
