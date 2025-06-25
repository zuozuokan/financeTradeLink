package com.nefu.project.base.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nefu.project.base.mapper.IUserMapper;
import com.nefu.project.base.service.IInitAdminRoleService;
import com.nefu.project.base.service.IRegisterService;
import com.nefu.project.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InitAdminRoleImpl implements IInitAdminRoleService {
    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private IRegisterService iRegisterService;
    // 初始化管理员角色

    public void initAdmin() {
        // 初始化管理员
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserRole, User.Role.ADMIN.toString());
        if (iUserMapper.selectCount(queryWrapper) == 0) {
            log.debug("检测到数据库中无管理员,管理员账号开始初始化...");
            User user = new User();
            user.setUserName("admin");
            user.setUserUserName("admin");
            user.setUserPassword("admin");
            user.setUserRole(User.Role.ADMIN.toString());
            if (iRegisterService.register(user)) {
                log.debug("管理员账号初始化完成...");
            }
        }
    }
}
