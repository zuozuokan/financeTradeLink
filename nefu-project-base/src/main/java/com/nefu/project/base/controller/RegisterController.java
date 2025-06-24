package com.nefu.project.base.controller;
import com.nefu.project.base.service.IRegisterService;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "注册接口")
@RestController
@RequestMapping("/api")
@Slf4j
public class RegisterController {

    @Autowired
    private IRegisterService iRegisterService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public HttpResult register(@RequestBody User user) {

        if(iRegisterService.register(user)) {
            return HttpResult.success(user);
        }
        return HttpResult.failed("未知错误,注册失败");
    }

}
