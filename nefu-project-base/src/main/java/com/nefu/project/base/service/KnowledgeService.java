package com.nefu.project.base.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nefu.project.domain.entity.Knowledge;

public interface KnowledgeService {
    boolean publish(Knowledge knowledge);
    Page<Knowledge> list(int pageNum, int pageSize, String category, String keyword);
    Knowledge getByUuid(String uuid);
    boolean deleteByUuid(String uuid);
    boolean like(String uuid);
}

