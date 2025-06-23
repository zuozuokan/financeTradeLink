package com.nefu.project.user.service.impl;

import com.nefu.project.domain.entity.User;
import com.nefu.project.user.mapper.IUserMapper;
import com.nefu.project.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper iUserMapper;

//    找到所有用户
    @Override
    public List<User> findAllUsers() {
        return iUserMapper.selectList(null);
    }
}
