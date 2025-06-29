package com.nefu.project.user.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import com.nefu.project.common.exception.consult.ConsultException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.domain.entity.Consult;
import com.nefu.project.domain.entity.Expert;
import com.nefu.project.domain.entity.User;

import com.nefu.project.user.mapper.IConsultMapper;
import com.nefu.project.user.mapper.IExpertMapper;
import com.nefu.project.user.mapper.IUserMapper;
import com.nefu.project.user.service.IConsultService;
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
     * 添加咨询预约
     *
     * @param userUuid 用户UUID
     * @param consult 咨询预约对象
     */
    @Override
    public void addConsult(String userUuid, Consult consult) {
        log.debug("添加咨询预约，userUuid: {}, consult: {}", userUuid, consult);

        // 参数校验
        stringIsExist(userUuid, "用户ID为空");
        stringIsExist(consult.getConsultTitle(), "咨询标题不能为空");
        stringIsExist(consult.getConsultDescription(), "咨询内容不能为空");
        stringIsExist(consult.getConsultType(), "咨询类型不能为空");
        stringIsExist(consult.getConsultExpertUuid(), "专家ID不能为空");

        // 检查用户是否存在
        User user = iUserMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUserUuid, userUuid)
                        .eq(User::getUserStatus,"1")
                        .eq(User::getUserRole,"USER")

        );
        if (Objects.isNull(user)) {
            throw new UserException("用户不存在，添加咨询预约失败");
        }
        if(!(consult.getConsultType().equals("online")||consult.getConsultType().equals("offline"))) {
            throw  new ConsultException("预约类型异常");
        }
        // 检查专家是否存在
        Expert expert = iExpertMapper.selectOne(
                new LambdaQueryWrapper<Expert>()
                        .eq(Expert::getExpertUuid, consult.getConsultExpertUuid())
                         .eq(Expert::getExpertStatus,1)
        );
        log.debug("expert: {}", expert);
        if (Objects.isNull(expert)) {
            throw new ConsultException("专家不存在，添加咨询预约失败");
        }

        // 检查预约时间是否合法
        if (consult.getConsultAppointTime() == null ||
                consult.getConsultAppointTime().before(new Date())) {
            throw new ConsultException("预约时间必须为未来时间");
        }
        log.debug(consult.getConsultAppointTime().toString());

        // 生成UUID并设置基础信息
        consult.setConsultUuid(IdWorker.getIdStr());
        consult.setConsultUserUuid(userUuid);
        consult.setConsultStatus("pending");  // 初始状态为待处理
        consult.setConsultCreatedTime(new Date());
        consult.setConsultUpdatedTime(new Date());
        log.debug("consult: {}$$", consult);
        // 插入数据库
        iConsultMapper.insert(consult);
    }

    /**
     * 删除咨询预约
     *
     * @param userUuid 用户UUID
     * @param consultUuid 咨询预约UUID
     */
    @Override
    @Transactional
    public void deleteConsult(String userUuid, String consultUuid) {
        log.debug("删除咨询预约，userUuid: {}, consultUuid: {}", userUuid, consultUuid);

        // 参数校验
        stringIsExist(userUuid, "用户ID为空");
        stringIsExist(consultUuid, "咨询预约ID为空");

        // 检查咨询预约是否存在
        Consult consult = iConsultMapper.selectByUuid(consultUuid);
        if (Objects.isNull(consult)) {
            throw new ConsultException("该咨询预约不存在");
        }

        // 权限校验：只能删除自己的预约
        if (!userUuid.equals(consult.getConsultUserUuid())) {
            throw new ConsultException("无权限删除此咨询预约");
        }

        // 检查状态是否允许删除（例如：已完成的预约不允许删除）
        if (!consult.getConsultStatus().equals("pending") &&
                !consult.getConsultStatus().equals("cancelled")) {
            throw new ConsultException("状态为[" + consult.getConsultStatus() + "]的预约不允许删除");
        }

        // 删除数据库记录
        try {
            int removed = iConsultMapper.deleteById(consult.getConsultId());
            if (removed == 0) {
                throw new ConsultException("未知原因，咨询预约删除失败");
            }
        } catch (DbException e) {
            throw new DbException("数据库操作失败：" + e.getMessage());
        }
    }

    /**
     * 更新咨询预约信息
     *
     * @param userUuid 用户UUID
     * @param consult 咨询预约对象（包含更新后的信息）
     */
    @Override
    @Transactional
    public void updateConsult(String userUuid, Consult consult) {
        log.debug("更新咨询预约，userUuid: {}, consult: {}", userUuid, consult);

        // 参数校验
        stringIsExist(userUuid, "用户ID为空");
        stringIsExist(consult.getConsultUuid(), "咨询预约ID为空");
        stringIsExist(consult.getConsultTitle(), "咨询标题不能为空");
        stringIsExist(consult.getConsultDescription(), "咨询内容不能为空");
        stringIsExist(consult.getConsultType(), "预约类型不能为空");

        // 检查咨询预约是否存在
        Consult existingConsult = iConsultMapper.selectByUuid(consult.getConsultUuid());
        if (Objects.isNull(existingConsult)) {
            throw new ConsultException("该咨询预约不存在");
        }
        //预约类型
        if(!(consult.getConsultType().equals("online")||consult.getConsultType().equals("offline"))) {
            throw  new ConsultException("预约类型异常");
        }
        // 权限校验
        if (!userUuid.equals(existingConsult.getConsultUserUuid())) {
            throw new ConsultException("无权限更新此咨询预约");
        }

        // 检查预约时间是否合法（仅允许更新为未来时间）
        if (consult.getConsultAppointTime() != null &&
                consult.getConsultAppointTime().before(new Date())) {
            throw new ConsultException("预约时间必须为未来时间");
        }

        // 更新信息（保留不可修改的字段，如用户ID、专家ID）
        consult.setConsultUserUuid(existingConsult.getConsultUserUuid());
        consult.setConsultExpertUuid(existingConsult.getConsultExpertUuid());
        consult.setConsultStatus("pending");
        consult.setConsultCreatedTime(existingConsult.getConsultCreatedTime());
        consult.setConsultUpdatedTime(new Date());
       log.debug("consult: {}$$", consult);
        // 更新数据库
        LambdaUpdateWrapper<Consult> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Consult::getConsultUuid, consult.getConsultUuid())
                .set(Consult::getConsultTitle, consult.getConsultTitle())
                .set(Consult::getConsultDescription, consult.getConsultDescription())
                .set(Consult::getConsultType, consult.getConsultType())
                .set(Consult::getConsultAppointTime, consult.getConsultAppointTime())
                .set(Consult::getConsultUpdatedTime, consult.getConsultUpdatedTime());

        try {
            int rows = iConsultMapper.update(null, updateWrapper);
            if (rows == 0) {
                throw new ConsultException("咨询预约更新失败");
            }
        } catch (DbException e) {
            throw new DbException("数据库操作失败：" + e.getMessage());
        }
    }

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
        if(!status.equals("cancelled")) {
            throw new ConsultException("用户仅支持取消");
        }
        // 检查咨询预约是否存在
        Consult consult = iConsultMapper.selectByUuid(consultUuid);
        if (Objects.isNull(consult)) {
            throw new ConsultException("该咨询预约不存在");
        }
        if(!consult.getConsultStatus().equals("pending")) {
            throw new ConsultException("该咨询状态非审核状态");
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
            List<Consult> consultList = iConsultMapper.getConsultListByUserUuid(userUuid);
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