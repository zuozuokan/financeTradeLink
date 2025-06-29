package com.nefu.project.admin.controller;

import com.nefu.project.admin.job.ScheduledDeletionConfig;
import com.nefu.project.admin.job.UserDelayedTaskJob;
import com.nefu.project.admin.service.IAdminService;
import com.nefu.project.admin.service.impl.AdminServiceimpl;
import com.nefu.project.common.result.HttpResult;
import com.nefu.project.domain.entity.Expert;
import com.nefu.project.domain.entity.Knowledge;
import com.nefu.project.domain.entity.User;
import io.swagger.v3.oas.annotations.OpenAPI31;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "管理员接口")
@RestController
@Slf4j
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private IAdminService iAdminService;

    @Autowired
    private ScheduledDeletionConfig scheduledDeletionConfig;


    @SneakyThrows
    @Operation(summary = "银行账号注册")
    @PostMapping("/register-bank")
    public HttpResult registerBank(String username,String password){
        if (iAdminService.addBank(username,password)) {
            return HttpResult.success("银行该账户注册成功");
        }
        return HttpResult.failed("银行该账户注册失败");
    }

    @SneakyThrows
    @Operation(summary = "专家账号注册")
    @PostMapping("/register-expert")
    public HttpResult registerExpert(String username,String password){
        if (iAdminService.addExpert(username,password)) {
            return HttpResult.success("专家该账户注册成功");
        }
        return HttpResult.failed("专家该账户注册失败");
    }

    @SneakyThrows
    @Operation(summary = "审核融资项目")
    @PostMapping("/checkLoan")
    public HttpResult<String> checkLoan(String loanUuid, String adminAdvice){
        if (loanUuid.isEmpty()||adminAdvice.isEmpty()){
            return HttpResult.failed("该项目异常,参数不能为空");
        }
        if (iAdminService.checkLoan(loanUuid,adminAdvice)) {
            return HttpResult.success("审核成功");
        }
        return HttpResult.failed("审核失败");

    }

    @SneakyThrows
    @Operation(summary = "新增内容")
    @PostMapping("/add-knowlodge")
    public HttpResult<String> addKnowledge(@RequestBody Knowledge knowledge){
        if (Objects.isNull(knowledge)) {
            return HttpResult.failed("内容对象不能为空");
        }
        if (iAdminService.addKnowledge(knowledge)) {
            return HttpResult.success("新增成功");
        }
        return HttpResult.failed("新增失败");
    }

    @SneakyThrows
    @Operation(summary = "删除内容")
    @PostMapping("/delete-knowlodge")
    public HttpResult<String> deleteKnowledge(String knowledgeUuid){
        if (Objects.isNull(knowledgeUuid)) {
            return HttpResult.failed("内容Id不能为空");
        }
        if (iAdminService.deleteKnowledge(knowledgeUuid)) {
            return HttpResult.success("删除成功");
        }
        return HttpResult.failed("删除失败");
    }

    @SneakyThrows
    @Operation(summary = "更新内容")
    @PostMapping("/update-knowlodge")
    public HttpResult<String> updateKnowledge(@RequestBody Knowledge knowledge){
        if (Objects.isNull(knowledge)) {
            return HttpResult.failed("内容对象不能为空");
        }
        if (iAdminService.updateKnowledge(knowledge))
            return HttpResult.success("更新成功");
        return HttpResult.failed("更新失败");
    }

    @SneakyThrows
    @Operation(summary = "获取所有普通用户")
    @GetMapping("/find-all-users")
    public HttpResult<List<User>> findAllUsers(){
        List<User> users = iAdminService.getAllUser();
        return HttpResult.success(users);
    }

    @SneakyThrows
    @Operation(summary = "获取所有银行用户")
    @GetMapping("/find-all-banks")
    public HttpResult<List<User>> findAllBanks(){
        List<User> users = iAdminService.getAllBank();
        return HttpResult.success(users);
    }

    @SneakyThrows
    @Operation(summary = "获取所有专家用户")
    @GetMapping("/find-all-experts")
    public HttpResult<List<User>> findAllExperts(){
        List<User> users = iAdminService.getAllExpert();
        return HttpResult.success(users);
    }
    @SneakyThrows
    @Operation(summary = "获取所有专家详细信息")
    @GetMapping("/find-all-experts-info")
    public HttpResult<List<Expert>> findAllExpertsinfo(String userUuid){
        List<Expert> experts = iAdminService.getExpertsInfo(userUuid);
        return HttpResult.success(experts);
    }
    @SneakyThrows
    @Operation(summary = "更新专家信息的状态")
    @GetMapping("/update-experts-info")
    public HttpResult findAllExpertsinfo(String userUuid,String expertUuid){
        iAdminService.updateExpertStatus(userUuid,expertUuid);
        return HttpResult.success("更新专家信息成功");
    }
    @SneakyThrows
    @Operation(summary = "操作用户")
    @PostMapping("/operation-user")
    public HttpResult<String> operationUser(String userUuid, String status) {
        // 调用iAdminService的operationUser方法，传入userUuid和status，返回布尔值
        if (iAdminService.operationUser(userUuid, status)) {

            String jobName = "user_deletion_job_" + userUuid;

            // 情况 1：封禁或注销，创建/重启定时器
            if (status.equals("2") || status.equals("0")) {
                // 创建一个Map，用于存储定时器的数据
                Map<String, Object> jobData = new HashMap<>();
                // 将userUuid放入Map中
                jobData.put("userUuid", userUuid);
                // 调用scheduledDeletionConfig的scheduleJobWithDelay方法，传入UserDelayedTaskJob类，jobName，15552000（30天）和jobData，创建/重启定时器
                scheduledDeletionConfig.scheduleJobWithDelay(UserDelayedTaskJob.class, jobName, 15552000, jobData);
                // 打印日志，记录状态和userUuid
                log.debug("状态为 {}，开始或重启定时删除任务，UUID: {}", status, userUuid);
            }

            // 情况 2：用户恢复为活跃，取消任务
            if (status.equals("1")) {
                // 调用scheduledDeletionConfig的cancelScheduledJob方法，传入jobName，取消定时任务
                scheduledDeletionConfig.cancelScheduledJob(jobName);
                // 打印日志，记录userUuid
                log.debug("用户已恢复活跃状态，取消定时任务，UUID: {}", userUuid);
            }

            // 返回成功信息
            return HttpResult.success("用户账号状态变更成功");
        }

        // 返回失败信息
        return HttpResult.failed("用户账号状态变更失败");
    }



}
