package com.nefu.project.base.service;


import com.nefu.project.domain.entity.User;

public interface ILoginService {
    User login(String userName, String password);
}
