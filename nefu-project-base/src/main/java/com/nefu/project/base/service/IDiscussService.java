package com.nefu.project.base.service;

import com.nefu.project.domain.entity.Discuss;

import java.util.List;

public interface IDiscussService {
    boolean comment(Discuss d);
    List<Discuss> listByKnowledge(String uuid);
    boolean like(String uuid);
    boolean delete(String uuid);
}
