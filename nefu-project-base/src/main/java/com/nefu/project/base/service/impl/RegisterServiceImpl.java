package com.nefu.project.base.service.impl;

import cn.hutool.crypto.SmUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.user.UserRegistryException;
import com.nefu.project.base.mapper.IUserMapper;
import com.nefu.project.base.service.IRegisterService;
import com.nefu.project.base.util.BloomFilterUtil;
import com.nefu.project.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class RegisterServiceImpl implements IRegisterService {
    @Autowired
    private IUserMapper iUserMapper;

    @Override
    public void addUser(User user) {
        iUserMapper.insert(user);
    }

    @Override
    public void removeUser(int userId) {
        iUserMapper.deleteById(userId);
    }

    @Override
    public void updateUser(User user) {
        iUserMapper.updateById(user);
    }

    // 找到所有的用户
    @Override
    public List<User> findAllUsers() {
        LambdaQueryWrapper<User> queryWrapper
                = new LambdaQueryWrapper<>();
        return iUserMapper.selectList(queryWrapper);
    }

    @Override
    public boolean register(User user) {

        // 获取用户名
        String userName = user.getUserUserName();
        // 根据布隆过滤器判断是否存在
        boolean alreadyRegistry = BloomFilterUtil.getInstance().contains(userName);
        // 若存在，拒绝注册
        if (alreadyRegistry) {
            throw new UserRegistryException("用户注册异常，用户名已存在");
        }
        // 若不存在，则注册
        // 密码加密
        String newPassWord = SmUtil.sm3(user.getUserPassword());
        // 构建用户
        User newUser = User.builder()
                .userName(user.getUserName())
                .userUserName(userName)
                .userPassword(newPassWord)
                .userPhone(user.getUserPhone())
                .userStatus(user.getUserStatus())
                .userRole(user.getUserRole())
                .userUuid(IdWorker.getIdStr()) // 雪花算法生成uuid
                .userCreateTime(new Date())
                .userUpdateTime(new Date())
                .userAmount(user.getUserAmount())
                .build();
        // 将用户添加到数据库中
        try{
            addUser(newUser);
        }
        catch (Exception e){
            throw new UserRegistryException("用户注册异常，数据库异常");
        }
        // 将用户名添加到布隆过滤器中
        BloomFilterUtil.getInstance().add(user.getUserUserName());
        // 返回注册结果
        return true;

    }
}
