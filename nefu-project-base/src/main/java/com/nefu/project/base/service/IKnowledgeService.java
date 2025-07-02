package com.nefu.project.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.domain.entity.Knowledge;

public interface IKnowledgeService {
    boolean publish(Knowledge knowledge, String token);
    Page<Knowledge> list(int pageNum, int pageSize, String category, String keyword);
    Knowledge getByUuid(String uuid);
    boolean deleteByUuid(String uuid);
    boolean like(String uuid);

}

