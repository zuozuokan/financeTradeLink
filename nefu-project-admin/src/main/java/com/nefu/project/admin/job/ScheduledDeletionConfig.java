package com.nefu.project.admin.job;

import cn.hutool.core.lang.UUID;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class ScheduledDeletionConfig {

    @Autowired
    private Scheduler scheduler;

    // 创建 JobDetail 带唯一标识
    private JobDetail createJobDetail(Class<? extends Job> jobClass, String jobName, Map<String, Object> jobData) {
        return JobBuilder.newJob(jobClass)
                .withIdentity(jobName, "user_deletion_group")
                .usingJobData(new JobDataMap(jobData))
                .storeDurably()
                .build();
    }

    /**
     * 创建延迟任务（如果存在相同 jobName 的任务，会先删除后重新调度）
     */
    public void scheduleJobWithDelay(Class<? extends Job> jobClass, String jobName, int delaySeconds, Map<String, Object> jobData)
            throws SchedulerException {

        JobKey jobKey = JobKey.jobKey(jobName, "user_deletion_group");

        // 如果已经存在该任务，先删除（实现“重新计时”）
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }

        JobDetail jobDetail = createJobDetail(jobClass, jobName, jobData);

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("trigger_" + jobName, "user_deletion_group")
                .startAt(Date.from(Instant.now().plusSeconds(delaySeconds)))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 取消指定任务（用于恢复用户时调用）
     */
    public void cancelScheduledJob(String jobName) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(jobName, "user_deletion_group");
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
    }
}
