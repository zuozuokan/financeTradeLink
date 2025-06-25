package com.nefu.project.admin.service.impl;

import cn.hutool.crypto.SmUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.admin.mapper.IAdminLogMapper;
import com.nefu.project.admin.mapper.IKnowledgeMapper;
import com.nefu.project.admin.mapper.ILoanApplicationMapper;
import com.nefu.project.admin.mapper.IUserMapper;
import com.nefu.project.admin.service.IAdminService;
import com.nefu.project.common.exception.user.AdminException;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.UserRegistryException;
import com.nefu.project.domain.entity.Knowledge;
import com.nefu.project.domain.entity.LoanApplication;
import com.nefu.project.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AdminServiceimpl implements IAdminService {

    @Autowired
    private IKnowledgeMapper iKnowledgeMapper;

    @Autowired
    private IAdminLogMapper iAdminLogMapper;

    @Autowired
    private ILoanApplicationMapper iLoanApplicationMapper;

    @Autowired
    private IUserMapper iUserMapper;
   
    /** 
     * @description: 更新管理员的管理意见 
     * @param: [loanUuid, adminAdvice] 
     * @return: boolean 
     */
    @Override
    public boolean checkLoan(String loanUuid,String adminAdvice) {
        LambdaUpdateWrapper<LoanApplication> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(LoanApplication::getLoanApplicationUuid,loanUuid)
                .set(LoanApplication::getLoanApplicationStatus,adminAdvice);
        try{
            int rows = iLoanApplicationMapper.update(null,lambdaUpdateWrapper);
            if (rows > 0) {
                return true;
            }
        }catch (DbException e){
            throw new DbException("数据库操作异常");
        }
        
        return false;
    }

    /** 
     * @description: 新增内容
     * @param: [knowledge] 
     * @return: boolean 
     */
    @Override
    public boolean addKnowledge(Knowledge knowledge) {

        // 雪花算法生成uuid
        knowledge.setKnowledgeUuid(IdWorker.getIdStr());
        try{
            int rows = iKnowledgeMapper.insert(knowledge);
            if (rows > 0) {
                return true;
            }
        }catch (DbException e){
            throw new DbException("数据库操作异常");
        }
        return false;
    }

    /**
     * @description: 删除内容
     * @param: [knowledgeUuid]
     * @return: boolean
     */
    @Override
    public boolean deleteKnowledge(String knowledgeUuid) {
        LambdaUpdateWrapper<Knowledge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Knowledge::getKnowledgeUuid,knowledgeUuid);
        return iKnowledgeMapper.delete(lambdaUpdateWrapper) > 0;
    }



    /**
     * @description: 修改内容
     * @param: [knowledge]
     * @return: boolean
     */
    @Override
    public boolean updateKnowledge(Knowledge knowledge) {
        // 先通过id把对象找出来，然后获取它的uuid
        LambdaUpdateWrapper<Knowledge> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Knowledge::getKnowledgeId,knowledge.getKnowledgeId());
        Knowledge knowledgeSelected = iKnowledgeMapper.selectOne(lambdaUpdateWrapper);
        String uuid = knowledgeSelected.getKnowledgeUuid();
        knowledge.setKnowledgeUuid(uuid);

        try{
            int rows = iKnowledgeMapper.updateById(knowledge);
            if (rows > 0) {
                return true;
            }
        }catch (DbException e){
            throw new DbException("数据库操作异常");
        }

        return false;
    }

    /**
     * @description: 获取所有用户
     * @param: []
     * @return: java.util.List<com.nefu.project.domain.entity.User>
     */
    @Override
    public List<User> getAllUser() {
        //查找所有role为user的用户
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserRole, User.Role.USER.toString());
        try {
            List<User> users = iUserMapper.selectList(lambdaQueryWrapper);
            return users;
        }catch (DbException e) {
            throw new DbException("数据库操作异常");
        }

    }
    /**
     * @description: 获取所有银行角色
     * @param: []
     * @return: java.util.List<com.nefu.project.domain.entity.User>
     */
    @Override
    public List<User> getAllBank() {
        //查找所有role为bank的用户
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserRole, User.Role.BANK.toString());
        try {
            List<User> users = iUserMapper.selectList(lambdaQueryWrapper);
            return users;
        }catch (DbException e) {
            throw new DbException("数据库操作异常");
        }
    }

    /**
     * @description: 获取所有专家角色
     * @param: []
     * @return: java.util.List<com.nefu.project.domain.entity.User>
     */
    @Override
    public List<User> getAllExpert() {
        //查找所有role为expert的用户
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserRole, User.Role.EXPERT.toString());
        try {
            List<User> users = iUserMapper.selectList(lambdaQueryWrapper);
            return users;
        }catch (DbException e) {
            throw new DbException("数据库操作异常");
        }
    }

    /**
     * @description: 管理用户状态 
     * @param: [userUuid, status] 
     * @return: boolean 
     */
    @Override
    public boolean operationUser(String userUuid, String status) {
        if (Objects.isNull(status) || Objects.isNull(userUuid)) {
            throw new AdminException("上传参数不能为空");
        }
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUserUuid, userUuid);
        User user = iUserMapper.selectOne(lambdaUpdateWrapper);
        if (Objects.isNull(user)) {
            throw new AdminException("用户不存在");
        }
        // 修改用户状态(DISABLE:封禁，ENABLE:启用，DISAPPER:注销)
        if (!(status.equals("0") || status.equals("1") || status.equals("2"))) {
            throw new AdminException("状态参数错误");
        }
        lambdaUpdateWrapper.eq(User::getUserUuid, userUuid)
                    .set(User::getUserStatus,status);
        try {
            int rows = iUserMapper.update(null, lambdaUpdateWrapper);
            if (rows > 0) {
                return true;
            }
        }catch (DbException e) {
            throw new DbException("数据库操作异常");
        }

        return false;
    }

    /**
     * @description: 添加银行和专家角色
     * @param: [username, password]
     * @return: boolean
     */
    @Override
    public boolean addBank(String userName, String password) {
        // 密码加密
        String newPassWord = SmUtil.sm3(password);
        // 构建用户
        User newUser = User.builder()
                .userName("银行人员")
                .userUserName(userName)
                .userPassword(newPassWord)
                .userPhone("")
                .userStatus("1")
                .userRole(User.Role.BANK.toString())
                .userUuid(IdWorker.getIdStr()) // 雪花算法生成uuid
                .userCreateTime(new Date())
                .userUpdateTime(new Date())
                .build();
        // 将用户添加到数据库中
        try{
            iUserMapper.insert(newUser);
        }
        catch (Exception e){
            throw new UserRegistryException("银行注册异常，该账号已存在");
        }
        // 返回注册结果
        return true;
    }

    @Override
    public boolean addExpert(String userName, String password) {
        // 密码加密
        String newPassWord = SmUtil.sm3(password);
        // 构建用户
        User newUser = User.builder()
                .userName("专家")
                .userUserName(userName)
                .userPassword(newPassWord)
                .userPhone("")
                .userStatus("1")
                .userRole(User.Role.EXPERT.toString())
                .userUuid(IdWorker.getIdStr()) // 雪花算法生成uuid
                .userCreateTime(new Date())
                .userUpdateTime(new Date())
                .build();
        // 将用户添加到数据库中
        try{
            iUserMapper.insert(newUser);
        }
        catch (Exception e){
            throw new UserRegistryException("专家注册异常，该账号已存在");
        }
        // 返回注册结果
        return true;
    }
}
