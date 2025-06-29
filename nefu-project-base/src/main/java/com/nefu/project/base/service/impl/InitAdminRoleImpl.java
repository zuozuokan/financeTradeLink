package com.nefu.project.base.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.nefu.project.base.mapper.IUserMapper;
import com.nefu.project.base.service.IInitAdminRoleService;
import com.nefu.project.base.service.IRegisterService;
import com.nefu.project.common.exception.user.DbException;
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
        Long adminNums =  iUserMapper.selectCount(queryWrapper);
        if (adminNums < User.ADMIN_NUM) {
            log.debug("检测到管理员数量不足,管理员账号开始初始化...");

            // 修改注册逻辑错误
            for (Long i = adminNums; i < User.ADMIN_NUM; i++) {
                User user = new User();
                user.setUserName("admin" + (i + 1));
                user.setUserUserName("admin" + (i + 1 ));
                user.setUserPassword("admin");
                user.setUserRole(User.Role.ADMIN.toString());
                try {
                    log.info(user.getUserUserName() + "正在注册... ");
                    iRegisterService.register(user);
                }catch (DbException e) {
                    throw new DbException("管理员初始化异常");
                }
            }
            log.debug("所有管理员账号初始化完成...");


        }
    }
}
