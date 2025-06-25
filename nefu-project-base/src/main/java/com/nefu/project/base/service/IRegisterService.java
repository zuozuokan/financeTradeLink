package com.nefu.project.base.service;

import com.nefu.project.domain.entity.User;

import java.util.List;

public interface IRegisterService {
    /**
     * 注册
     * @param user
     * @return
     */

    boolean register(User user);
    /**
     * @description: 增删改查
     * @param:
     * @return:
     */


    void addUser(User user);
    void removeUser(int userId);
    void updateUser(User user);
    List<User> findAllUsers();
}
