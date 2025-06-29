package com.nefu.project.user.service.impl;

import cn.hutool.crypto.SmUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.domain.entity.Address;
import com.nefu.project.domain.entity.User;
import com.nefu.project.user.mapper.IAddressMapper;
import com.nefu.project.user.mapper.IUserMapper;
import com.nefu.project.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private IAddressMapper iAddressMapper;
    @Autowired
    private ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder;


    /**
     * @description: 获取当前用户信息
     * @param: [uuid]
     * @return: boolean
     */
    @Override
    public User getCurrentUserInfo(String uuid) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserUuid, uuid);
        User findUser = iUserMapper.selectOne(lambdaQueryWrapper);
        if (findUser == null) {
            throw new DbException("用户不存在");
        }
        return findUser;
    }

    /**
     * @description: 实现修改密码
     * @param: [oldPassword, newPassword]
     * @return: boolean
     */
    @Override
    public boolean updatePassword(String uuid, String newPassword) {

        String password = SmUtil.sm3(newPassword);
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUserUuid, uuid)
                .set(User::getUserPassword, password);

        try{
            int rows = iUserMapper.update(null, lambdaUpdateWrapper);
            if (rows > 0){
                return true;
            }
        }catch (DbException e){
            throw new DbException(e.getMessage());
        }

        return true;
    }
    /**
     * @description: 实现更新昵称
     * @param: [uuid, userName]
     * @return: boolean
     */
    @Override
    public boolean updateName(String uuid, String userName) {

        LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUserUuid, uuid)
                .set(User::getUserName, userName);
        try{
            int rows = iUserMapper.update(null, lambdaUpdateWrapper);
            if (rows > 0) {
                return true;
            }
        }catch (DbException e){
            throw new DbException(e.getMessage());
        }

        return false;
    }
    /**
     * @description:  实现更新手机号
     * @param: [uuid, userPhone]
     * @return: boolean
     */
    @Override
    public boolean updatePhone(String uuid, String userPhone) {
        LambdaUpdateWrapper<User>lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(User::getUserUuid, uuid)
                .set(User::getUserPhone, userPhone);
        try{
            int rows =  iUserMapper.update(null, lambdaUpdateWrapper);
            if (rows > 0) {
                return true;
            }
        }catch (DbException e){
            throw new DbException(e.getMessage());
        }

        return false;
    }

    /**
     * @description:  实现更新地址
     * @param: [uuid, userAddress]
     * @return: boolean
     */
    @Override
    public boolean updateAddress(String addressUuid, Address userAddress) {

        LambdaUpdateWrapper<Address> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Address::getAddressUuid, addressUuid)
                .set(Address::getAddressName, userAddress.getAddressName())
                .set(Address::getAddressPhone, userAddress.getAddressPhone())
                .set(Address::getAddressProvince, userAddress.getAddressProvince())
                .set(Address::getAddressCity, userAddress.getAddressCity())
                .set(Address::getAddressDistrict, userAddress.getAddressDistrict())
                .set(Address::getAddressAddress, userAddress.getAddressAddress())
                .set(Address::getAddressIsDefault, userAddress.getAddressIsDefault())
                .set(Address::getAddressUpdatedTime, new Date());
        // 执行更新操作并获取影响的行数
        try{
            int rows = iAddressMapper.update(null, lambdaUpdateWrapper);
            if (rows > 0) {
                return true;
            }
        }catch (DbException E){
            throw new DbException(E.getMessage());
        }

        return false;
    }

    /**
     * @description: 新增地址
     * @param: [uuid, userAddress]
     * @return: boolean
     */
    @Override
    public boolean addAddress(String uuid, Address userAddress) {
        Address address = Address.builder()
                .addressUuid(IdWorker.getIdStr())
                .addressUserUuid(uuid)
                .addressName(userAddress.getAddressName())
                .addressPhone(userAddress.getAddressPhone())
                .addressAddress(userAddress.getAddressAddress())
                .addressDistrict(userAddress.getAddressDistrict())
                .addressCity(userAddress.getAddressCity())
                .addressProvince(userAddress.getAddressProvince())
                .addressIsDefault(userAddress.getAddressIsDefault())
                .addressCreatedTime(new Date())
                .addressUpdatedTime(new Date())
                .build();
        try{
            int rows =  iAddressMapper.insert(address);
            if (rows > 0){
                return true;
            }
        }catch (DbException e) {
            throw new DbException(e.getMessage());
        }

        return false;
    }


}
