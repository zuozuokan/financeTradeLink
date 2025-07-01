package com.nefu.project.user.controller;

import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Address;
import com.nefu.project.domain.entity.User;
import com.nefu.project.user.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Tag(name = "用户模块接口")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IUserService iUserService;

    /**
     * @description: 获取当前用户信息
     * @param: []
     * @return: com.nefu.project.common.result.HttpResult
     */
    @PostMapping("/get-current-user-info")
    public HttpResult getCurrentUserInfo(String uuid) {
        if (Objects.isNull(uuid) || uuid.isEmpty()) {
            return HttpResult.failed("用户uuid不能为空");
        }
        User user = iUserService.getCurrentUserInfo(uuid);
        return HttpResult.success(user);
    }

   /** 
    * @description: 修改密码 
    * @param: [uuid, newPassword] 
    * @return: com.nefu.project.common.result.HttpResult 
    */
    @PostMapping("/update-password")
    public HttpResult updatePassword(String uuid, String newPassword) {
        if (iUserService.updatePassword(uuid, newPassword)) {
            return HttpResult.success("密码修改成功");
        }
        return HttpResult.failed("密码修改失败");
    }
    /** 
     * @description:  修改昵称
     * @param: [uuid, nickname] 
     * @return: com.nefu.project.common.result.HttpResult 
     */
    @PostMapping("/update-name")
    public HttpResult updateName(String uuid, String nickname) {
        if (iUserService.updateName(uuid, nickname)) {
            return HttpResult.success("昵称修改成功");
        }
        return HttpResult.failed("昵称修改失败");
    }
    /** 
     * @description:  修改电话号码
     * @param: [uuid, phone] 
     * @return: com.nefu.project.common.result.HttpResult 
     */
    @PostMapping("/update-phone")
    public HttpResult updatePhone(String uuid, String phone) {
        if (iUserService.updatePhone(uuid, phone)) {
            return HttpResult.success("电话号码修改成功");
        }
        return HttpResult.failed("电话号码修改失败");
    }
    /** 
     * @description:  新增收货地址
     * @param: [uuid, address] 
     * @return: com.nefu.project.common.result.HttpResult 
     */
    @PostMapping("/add-address")
    public HttpResult addAddress(@RequestParam String uuid, @RequestBody Address address) {
        if (iUserService.addAddress(uuid, address)) {
            return HttpResult.success("地址添加成功");
        }
        return HttpResult.failed("地址添加失败");
    }
   
    /** 
     * @description:  修改收货地址
     * @param: [addressUuid, address] 
     * @return: com.nefu.project.common.result.HttpResult 
     */
    @PostMapping("/update-address")
    public HttpResult updateAddress(@RequestParam String addressUuid, @RequestBody Address address) {
        if (iUserService.updateAddress(addressUuid, address)) {
            return HttpResult.success("地址修改成功");
        }
        return HttpResult.failed("地址修改失败");
    }

    @GetMapping("/find-all-users")
    public HttpResult findAllUsers() {
        List<User> users = iUserService.findAllNormalUsers();
        if (users != null && !users.isEmpty()) {
            return HttpResult.success(users);
        }
        return HttpResult.failed("没有找到用户信息");}

    @GetMapping("/find-all-experts")
    public HttpResult findAllExperts() {
        List<User> users = iUserService.findAllExpertUsers();
        if (users != null && !users.isEmpty()) {
            return HttpResult.success(users);
        }
        return HttpResult.failed("没有找到用户信息");
    }

    @GetMapping("/find-all-banks")
    public HttpResult findAllBanks() {
        List<User> users = iUserService.findAllBankUsers();
        if (users != null && !users.isEmpty()) {
            return HttpResult.success(users);
        }
        return HttpResult.failed("没有找到用户信息");
    }

    //  修改用户状态（启用/禁用）
    @PostMapping("/update-status")
    public HttpResult<String> updateStatus(@RequestParam String uuid,
                                           @RequestParam String status) {
        boolean ok = iUserService.updateUserStatus(uuid, status);
        return ok ? HttpResult.success("状态更新成功") : HttpResult.failed("更新失败");
    }

    // 修改用户信息
    @PostMapping("/update-user")
    public HttpResult<String> updateUser(@RequestBody User user) {
        boolean updated = iUserService.updateUserInfo(user);
        return updated ? HttpResult.success("更新成功") : HttpResult.failed("更新失败");
    }

    // TODO: 新增账号

}
