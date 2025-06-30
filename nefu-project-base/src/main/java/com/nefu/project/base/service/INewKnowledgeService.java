package com.nefu.project.base.service;

import com.nefu.project.domain.entity.Knowledge;

public interface INewKnowledgeService {
    boolean updateKnowledage(String uuid, Knowledge knowledge);
}
