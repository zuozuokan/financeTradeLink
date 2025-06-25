package com.nefu.project.user.service;

import com.nefu.project.domain.entity.Address;
import com.nefu.project.domain.entity.User;

import java.util.List;

public interface IUserService {
    /**
     * @description: 个人信息（修改密码、修改昵称、修改电话号码、修改收货地址|新增收货地址）
     * @param:
     * @return:
     */
    boolean updatePassword(String uuid,String newPassword);
    boolean updateName(String uuid,String userName);
    boolean updatePhone(String uuid,String userPhone);
    boolean updateAddress(String addressUuid, Address userAddress);
    boolean addAddress(String uuid,Address userAddress);

}
