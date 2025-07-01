package com.nefu.project.expert.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.consult.ConsultException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.domain.entity.Consult;
import com.nefu.project.domain.entity.Expert;
import com.nefu.project.domain.entity.User;

import com.nefu.project.expert.mapper.IConsultMapper;
import com.nefu.project.expert.mapper.IExpertMapper;
import com.nefu.project.expert.mapper.IUserMapper;
import com.nefu.project.expert.service.IConsultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 咨询预约服务实现类
 * 处理咨询预约的业务逻辑
 */
@Slf4j
@Service
public class ConsultImpl implements IConsultService {

    @Autowired
    private IConsultMapper iConsultMapper;

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private IExpertMapper iExpertMapper;


    /**
     * 更新咨询预约状态
     *
     * @param consultUuid 咨询预约UUID
     * @param status 新状态
     */
    @Override
    @Transactional
    public void updateConsultStatus(String consultUuid, String status) {
        log.debug("更新咨询预约状态，consultUuid: {}, status: {}", consultUuid, status);
        // 参数校验
        stringIsExist(consultUuid, "咨询预约ID为空");
        stringIsExist(status, "状态不能为空");
        // 转换为小写统一格式
        status = status.toLowerCase();
        // 检查合法状态值
        if(!(status.equals("accepted")||status.equals("rejected")||status.equals("completed"))) {
            throw new ConsultException("非法修改状态，无权修改");
        }
        // 检查咨询预约是否存在
        Consult consult = iConsultMapper.selectByUuid(consultUuid);
        if (Objects.isNull(consult)) {
            throw new ConsultException("该咨询预约不存在");
        }
        if(consult.getConsultStatus().equals("cancelled")) {
            throw new ConsultException("该咨询用户已经取消");
        }
        // 更新状态和时间
        LambdaUpdateWrapper<Consult> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Consult::getConsultUuid, consultUuid)
                .set(Consult::getConsultStatus, status)
                .set(Consult::getConsultUpdatedTime, new Date());

        try {
            int rows = iConsultMapper.update(null, updateWrapper);
            if (rows == 0) {
                throw new ConsultException("咨询预约状态更新失败");
            }
        } catch (DbException e) {
            throw new DbException("数据库操作失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户的咨询预约列表
     *
     * @param userUuid 用户UUID
     * @return 咨询预约列表
     */
    @Override
    public List<Consult> getConsultListByUserUuid(String userUuid) {
        log.debug("获取用户的咨询预约列表，userUuid: {}", userUuid);

        // 参数校验
        stringIsExist(userUuid, "用户ID为空");

        // 查询数据库
        try {


            List<Consult> consultList = iConsultMapper.selectList(
                    new LambdaQueryWrapper<Consult>()
                            .eq(Consult::getConsultExpertUuid, userUuid)

            );
            if (consultList.isEmpty()) {
                throw new ConsultException("该用户没有咨询预约记录");
            }
            return consultList;
        } catch (DbException e) {
            throw new DbException("数据库查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据UUID获取单个咨询预约详情
     *
     * @param consultUuid 咨询预约UUID
     * @return 咨询预约对象
     */
    @Override
    public Consult getConsultByConsultUuid(String consultUuid) {
        log.debug("获取咨询预约详情，consultUuid: {}", consultUuid);

        // 参数校验
        stringIsExist(consultUuid, "咨询预约ID为空");

        // 查询数据库
        Consult consult = iConsultMapper.selectByUuid(consultUuid);
        if (Objects.isNull(consult)) {
            throw new ConsultException("该咨询预约不存在");
        }
        return consult;
    }

    /**
     * 检查字符串是否存在（非空）
     */
    private void stringIsExist(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new ConsultException(message);
        }
    }
}