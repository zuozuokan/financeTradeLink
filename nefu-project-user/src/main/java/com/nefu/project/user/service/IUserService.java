package com.nefu.project.user.service;

import com.nefu.project.domain.entity.User;

import java.util.List;

public interface IUserService {
    List<User> findAllUsers();

}
