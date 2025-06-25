package com.nefu.project.base.service.impl;

import cn.hutool.crypto.SmUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nefu.project.base.mapper.IUserMapper;
import com.nefu.project.base.service.ILoginService;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private IUserMapper iUserMapper;


    @Override
    public User login(String userName, String password,String role) {

        // 校验用户名和密码
        if (userName.isEmpty() || Objects.isNull(userName)) {
            throw new UserException("用户名不能为空");
        }
        // 查找用户
        User user = iUserMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUserUserName, userName)
        );
        if (Objects.isNull(user)) {
            throw new UserException("用户不存在");
        }
        // 判断角色
        if (!user.getUserRole().equals(role)) {
            throw new UserException("用户角色或账号密码错误");
        }
        String cpass = SmUtil.sm3(password);
        if (!cpass.equals(user.getUserPassword())) {
            throw new UserException("用户账号或密码错误");
        }
        return user;
    }
}
