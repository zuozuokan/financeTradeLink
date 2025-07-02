package com.nefu.project.expert.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.Expert.ExpertException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.domain.entity.Expert;
import com.nefu.project.domain.entity.User;
import com.nefu.project.expert.mapper.IExpertMapper;
import com.nefu.project.expert.service.IExpertService;
import com.nefu.project.expert.service.IMinioService;
import com.nefu.project.expert.mapper.IUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ExpertImpl implements IExpertService {

    @Autowired
    private IExpertMapper iExpertMapper;

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private IMinioService iMinioService;


    /*
     * description 添加专家信息
     *
     * @params[userUuid, headFile, certificateFile, expert]
     * @return void
     */
    @Override
    @Transactional
    public void addExpert(String userUuid, MultipartFile headFile, MultipartFile certificateFile,Expert expert) {
        log.debug("userUuid:{}, expert:{}", userUuid, expert);
        // 参数校验
        stringIsExist(userUuid, "用户ID为空");
        stringIsExist(expert.getExpertTitle(), "专家职称不能为空");
        stringIsExist(expert.getExpertSpecialty(), "专家擅长领域不能为空");
        stringIsExist(expert.getExpertIntroduction(), "专家简介不能为空");
        // 检查用户是否存在
        User user = iUserMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUserUuid, userUuid)
                        .eq(User::getUserRole,"EXPERT")
                        .eq(User::getUserStatus,"1")
        );
        if (Objects.isNull(user)) {
            throw new UserException("用户不存在,添加专家失败");
        }
        Expert expert1 = iExpertMapper.selectOne(
                new LambdaQueryWrapper<Expert>()
                        .eq(Expert::getExpertUserUuid,userUuid)
        );
        if (!Objects.isNull(expert1)) {
            throw new ExpertException("该用户已经存在信息");
        }
        // 上传头像
        String headshotUrl = "";
        if (headFile != null && !headFile.isEmpty()) {
            headshotUrl = iMinioService.uploadImage(headFile,"headshot/"+IdWorker.getIdStr());
        }
        expert.setExpertHeadshot(headshotUrl);
        String certificateUrl = "";
        if (certificateFile != null && !certificateFile.isEmpty()) {
            certificateUrl = iMinioService.uploadImage(certificateFile,"certificate/"+IdWorker.getIdStr());
        }
        expert.setExpertCertificate(certificateUrl);
        // 生成UUID并设置关联用户
        expert.setExpertUuid(IdWorker.getIdStr());
        expert.setExpertUserUuid(userUuid);
        expert.setExpertStatus("1");
        expert.setExpertCreatedTime(new Date());
        expert.setExpertUpdatedTime(new Date());
        iExpertMapper.insert(expert);
    }

    /**
     * description 删除专家信息
     *
     * @params [userUuid, expertUuid]
     * @return void
     */
    @Override
    @Transactional
    public void deleteExpert(String userUuid, String expertUuid) {

        // 参数校验
        stringIsExist(userUuid, "用户ID为空");
        stringIsExist(expertUuid, "专家ID为空");

        // 检查专家是否存在
        Expert expert = iExpertMapper.selectByUuid(expertUuid);
        if (expert == null) {
            throw new ExpertException("此专家信息不存在");
        }
        // 权限校验
        if (!userUuid.equals(expert.getExpertUserUuid())) {
            throw new ExpertException("无权限删除此专家信息");
        }
        // 删除头像
        if (expert.getExpertHeadshot() != null && !expert.getExpertHeadshot().isEmpty()) {
            iMinioService.deleteImage(expert.getExpertHeadshot());
        }
        // 删除资质证书
        if (expert.getExpertCertificate() != null && !expert.getExpertCertificate().isEmpty()) {
            iMinioService.deleteImage(expert.getExpertCertificate());
        }
        // 删除数据库记录
        try {
            int removed = iExpertMapper.deleteById(expert.getExpertId());
            if (removed == 0) {
                throw new ExpertException("未知原因，专家信息删除失败");
            }
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        }
    }

    /**
     * description 更新专家信息
     *
     * @params [userUuid, expert]
     * @return void
     */
    @Override
    @Transactional
    public void updateExpert(String expertUuid, Expert expert) {

        // 参数校验
        stringIsExist(expertUuid, "专家ID为空");
        stringIsExist(expert.getExpertTitle(), "专家职称不能为空");
        stringIsExist(expert.getExpertSpecialty(), "专家擅长领域不能为空");
        stringIsExist(expert.getExpertIntroduction(), "专家简介不能为空");
        // 检查专家信息是否存在

        Expert existingExpert = iExpertMapper.selectByUuid(expertUuid);
        if (existingExpert == null) {
            throw new ExpertException("此专家信息不存在");
        }
        existingExpert.setExpertUuid(expertUuid);
        // 更新其他字段
        expert.setExpertUpdatedTime(new Date());

        // 更新数据库
        LambdaUpdateWrapper<Expert> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Expert::getExpertUuid, expertUuid)
                .set(Expert::getExpertTitle, expert.getExpertTitle())
                .set(Expert::getExpertSpecialty, expert.getExpertSpecialty())
                .set(Expert::getExpertIntroduction, expert.getExpertIntroduction())
                .set(Expert::getExpertUpdatedTime, expert.getExpertUpdatedTime());

        try {
            int rows = iExpertMapper.update(null, updateWrapper);
            if (rows == 0) {
                throw new ExpertException("专家信息更新失败");
            }
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        }
    }

    /**
     * description 更新专家头像
     *
     * @params [expertUuid, headFile]
     * @return void
     */
    @Override
    @Transactional
    public void updateExpertHeadshot(String expertUuid, MultipartFile headFile) {
        // 参数校验
        stringIsExist(expertUuid, "专家信息ID不能为空");

        // 检查专家是否存在
        Expert expert = iExpertMapper.selectByUuid(expertUuid);
        if (expert == null) {
            throw new ExpertException("专家不存在，无法更新头像");
        }

        // 处理旧头像
        if (expert.getExpertHeadshot() != null && !expert.getExpertHeadshot().isEmpty()) {
            iMinioService.deleteImage(expert.getExpertHeadshot());
        }

        // 上传新头像
        String newHeadshotUrl = iMinioService.uploadImage(
                headFile,
                "headshot/" + expertUuid
        );

        // 更新数据库
        LambdaUpdateWrapper<Expert> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Expert::getExpertUuid, expertUuid)
                .set(Expert::getExpertHeadshot, newHeadshotUrl)
                .set(Expert::getExpertUpdatedTime, new Date());

        try {
            int rows = iExpertMapper.update(null, updateWrapper);
            if (rows == 0) {
                throw new ExpertException("头像更新失败");
            }
        } catch (DbException e) {
            throw new DbException("数据库操作失败: " + e.getMessage());
        }
    }

    /**
     * description 更新专家资质证书
     *
     * @params [expertUuid, certificateFile]
     * @return void
     */
    @Override
    @Transactional
    public void updateExpertCertificate(String expertUuid, MultipartFile certificateFile) {
        // 参数校验
        stringIsExist(expertUuid, "专家信息ID不能为空");
        // 检查专家是否存在
        Expert expert = iExpertMapper.selectByUuid(expertUuid);
        if (expert == null) {
            throw new ExpertException("专家不存在，无法更新资质证书");
        }
        // 处理旧证书
        if (expert.getExpertCertificate() != null && !expert.getExpertCertificate().isEmpty()) {
            iMinioService.deleteImage(expert.getExpertCertificate());
        }
        // 上传新证书
        String newCertificateUrl = iMinioService.uploadImage( certificateFile,
                "certificate/" + expertUuid
        );
        // 更新数据库
        LambdaUpdateWrapper<Expert> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Expert::getExpertUuid, expertUuid)
                .set(Expert::getExpertCertificate, newCertificateUrl)
                .set(Expert::getExpertUpdatedTime, new Date());
        try {
            int rows = iExpertMapper.update(null, updateWrapper);
            if (rows == 0) {
                throw new ExpertException("资质证书更新失败");
            }
        } catch (DbException e) {
            throw new DbException("数据库操作失败: " + e.getMessage());
        }
    }


    /**
     * description 获取自己的专家信息
     *
     * @params [expertUuid]
     * @return com.nefu.project.domain.entity.Expert
     */
    @Override
    public Expert getExpert(String expertUserUuid) {
        // 参数校验
        stringIsExist(expertUserUuid, "专家ID为空");
        // 查询数据库
        Expert expert = iExpertMapper.selectOne(
                new LambdaQueryWrapper<Expert>()
                        .eq(Expert::getExpertUserUuid,expertUserUuid)
        );
        if (Objects.isNull(expert)) {
            throw new ExpertException("该专家信息不存在");
        }
        return expert;
    }

    /**
     * 检查字符串是否存在
     */
    private void stringIsExist(String string, String message) {
        if (string == null || string.trim().isEmpty()) {
            throw new ExpertException(message);
        }
    }
}