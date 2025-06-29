package com.nefu.project.base.controller;
import com.nefu.project.base.service.IRegisterService;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Tag(name = "注册接口")
@RestController
@RequestMapping("/api/register-login")
@Slf4j
public class RegisterController {

    @Autowired
    private IRegisterService iRegisterService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public HttpResult register(@RequestBody User user) {

        // 判断用户是否输入账号和密码
        if (Objects.isNull(user.getUserUserName()) || user.getUserUserName().isEmpty()) {
            throw new UserException("账号不能为空");
        }
        if (Objects.isNull(user.getUserPassword()) || user.getUserPassword().isEmpty()) {
            throw new UserException("密码不能为空");
        }
        // 判断用户提交的角色申请
       if (!user.getUserRole().isEmpty() && !user.getUserRole().equals(User.Role.USER.toString()))
       {
           throw new UserException("用户角色不合法");
       }
        // 默认用户角色
        user.setUserRole(User.Role.USER.toString());
        // 默认用户状态
        user.setUserStatus("1");

        if(iRegisterService.register(user)) {
            return HttpResult.success("注册成功");
        }

        return HttpResult.failed("未知错误,注册失败");
    }

}
