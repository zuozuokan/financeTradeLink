package com.nefu.project.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.domain.entity.LoanApplication;
import com.nefu.project.domain.entity.User;
import com.nefu.project.user.mapper.ILoanApplicationMapper;
import com.nefu.project.user.mapper.IUserMapper;
import com.nefu.project.user.service.ILoanApplicationService;
import com.nefu.project.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 融资管理
 * 融资申请
 * 融资删除
 * 融资状态的修改（审核中、已批准、已拒绝、已删除）
 * 融资还款计划修改
 * 融资用途修改
 * 融资金额修改
 * 查看我的融资
 * 查看某一具体融资
 */

@Slf4j
@Service
public class LoanApplicationImpl implements ILoanApplicationService {

    @Autowired
    ILoanApplicationMapper iLoanApplicationMapper;

    @Autowired
    IUserMapper iUserMapper;

    /**
     * description 发起融资申请
     *
     * @params [userUuid, loanApplication]
     * @return void
     */

    @Override
    public void addLoanApplication(String userUuid, LoanApplication loanApplication) {


          log.debug("userUuid:{},loanApplication:{}", userUuid, loanApplication);
          stringIsExist(userUuid,"用户ID为空");
          //融资申请表的用途非空约束
          stringIsExist(loanApplication.getLoanApplicationPurpose(),"融资申请表的用途不能为空");
          //融资申请表的还款计划非空约束
          stringIsExist(loanApplication.getLoanApplicationRepayPlan(),"融资申请表的还款计划不能为空");
          // 融资金额非空约束
          if (loanApplication.getLoanApplicationAmount() == null || loanApplication.getLoanApplicationAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new LoanApplicationException("融资金额必须大于0");
          }
          // 查找用户，关联用户约束
          User user = iUserMapper.selectOne(
                 new LambdaQueryWrapper<User>()
                        .eq(User::getUserUuid, userUuid)
          );
          if (Objects.isNull(user)) {
            throw new UserException("用户不存在,申请融资表失败");
          }
          //生成唯一标识
          loanApplication.setLoanApplicationUuid(String.valueOf(IdWorker.getId()));
          //关联用户
          loanApplication.setLoanApplicationUserUuid(userUuid);
          loanApplication.setLoanApplicationStatus("pending");
          loanApplication.setLoanApplicationCreatedTime(new Date());
          loanApplication.setLoanApplicationUpdatedTime(new Date());
          iLoanApplicationMapper.insert(loanApplication);
    }

    /**
     * description 融资表删除
     *
     * @params [userUuid, loanApplicationUuid]
     * @return void
     */
    @Override
    public void deteleLoanApplication(String userUuid,String loanApplicationUuid) {

         //传入的参数不能为空值，用户ID
         stringIsExist(userUuid,"用户ID为空");
         stringIsExist(loanApplicationUuid,"融资申请表ID为空");

         //关联性查验
         LoanApplication loanApplication = iLoanApplicationMapper.selectByUuid(loanApplicationUuid);
         if (loanApplication == null) {
             throw new LoanApplicationException("此融资申请不存在");
         }
         if(userUuid.equals(loanApplication.getLoanApplicationUserUuid())){
             throw new LoanApplicationException("无权限删除此融资申请");
         }

        // 删除融资申请
        try{
            int removed = iLoanApplicationMapper.deleteById(loanApplication.getLoanApplicationId()) ;
            if (removed == 0 ) {
                throw new LoanApplicationException("未知原因融资表删除失败");
            }
        } catch (DbException e) {
            throw new DbException(e.getMessage());
        }

    }

    /**
     * description 更新融资表状态，撤回融资申请
     *
     * @params [loanApplicatinUuid, loanApplicationNewstatus]
     * @return void
     */
    @Override
    public void updateLoanApplicationStatus(String loanApplicatinUuid, String loanApplicationNewstatus) {

        stringIsExist(loanApplicatinUuid,"融资申请id为空");
        stringIsExist(loanApplicationNewstatus,"更新状态为空");
        loanApplicationNewstatus = loanApplicationNewstatus.toLowerCase();
        if(!loanApplicationNewstatus.equals("cancelled")){
            throw new LoanApplicationException("用户仅支持取消改融资申请表");
        }
        LambdaUpdateWrapper<LoanApplication> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(LoanApplication::getLoanApplicationUuid, loanApplicatinUuid)
                .set(LoanApplication::getLoanApplicationStatus, loanApplicationNewstatus);
        try{
            int rows = iLoanApplicationMapper.update(null, lambdaUpdateWrapper);
            if (rows == 0) {
                throw new LoanApplicationException("没有该融资申请表，状态更新失败");
            }
        }catch (DbException e){
            throw new DbException(e.getMessage());
        }
    }
    /**
     * description 更新融资申请还款计划
     *
     * @params [loanApplicatinUuid, loanApplicationRepayPlan]
     * @return void
     */
    @Override
    public void updateLoanApplicationRepayPlan(String loanApplicatinUuid, String loanApplicationRepayPlan) {

        stringIsExist(loanApplicatinUuid,"融资申请id为空");
        stringIsExist(loanApplicationRepayPlan,"还款计划为空");
        LambdaUpdateWrapper<LoanApplication> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(LoanApplication::getLoanApplicationUuid, loanApplicatinUuid)
                .set(LoanApplication::getLoanApplicationRepayPlan, loanApplicationRepayPlan);
        try{
            int rows = iLoanApplicationMapper.update(null, lambdaUpdateWrapper);
            if (rows == 0) {
                throw new LoanApplicationException("没有该融资申请表，还款计划更新失败");
            }
        }catch (DbException e){
            throw new DbException(e.getMessage());
        }
    }
    /**
     * description 更新融资申请的金额
     *
     * @params [loanApplicatinUuid, loanApplicationAmount]
     * @return void
     */
    @Override
    public void updateLoanApplicationAmount(String loanApplicatinUuid, BigDecimal loanApplicationAmount) {

        stringIsExist(loanApplicatinUuid,"融资申请id为空");
        if (loanApplicationAmount == null || loanApplicationAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new LoanApplicationException("融资金额必须大于0");
        }
        LambdaUpdateWrapper<LoanApplication> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(LoanApplication::getLoanApplicationUuid, loanApplicatinUuid)
                .set(LoanApplication::getLoanApplicationAmount, loanApplicationAmount);
        try{
            int rows = iLoanApplicationMapper.update(null, lambdaUpdateWrapper);
            if (rows == 0) {
                throw new LoanApplicationException("没有该融资申请表，还款计划更新失败");
            }
        }catch (DbException e){
            throw new DbException(e.getMessage());
        }
    }


    /**
     * description 更新融资申请的用途
     *
     * @params [loanApplicatinUuid, loanApplicationPurpose]
     * @return void
     */

    @Override
    public void updateLoanApplicationPurpose(String loanApplicatinUuid, String loanApplicationPurpose) {

        stringIsExist(loanApplicatinUuid,"融资申请id为空");
        stringIsExist(loanApplicationPurpose,"该融资描述为空");

        //写入数据库
        LambdaUpdateWrapper<LoanApplication> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(LoanApplication::getLoanApplicationUuid, loanApplicatinUuid)
                .set(LoanApplication::getLoanApplicationPurpose, loanApplicationPurpose);
        try{
            int rows = iLoanApplicationMapper.update(null, lambdaUpdateWrapper);
            if (rows==0) {
                throw new LoanApplicationException("没有该融资申请表，还款计划更新失败");
            }
        }catch (DbException e){
            throw new DbException(e.getMessage());
        }
    }
    /**
     * description 获取该用户的融资申请表
     *
     * @params [userUuid]
     * @return java.util.List<com.nefu.project.domain.entity.LoanApplication>
     */
    @Override
    public List<LoanApplication> getLoanApplicationList(String userUuid) {

        //传入值为空
        stringIsExist(userUuid,"无法正常获取用户id");
        //查询数据库
        try{
            List<LoanApplication> loanApplicationList = iLoanApplicationMapper.getLoanApplicationListByUserUuid(userUuid);
            if (loanApplicationList.size()==0) {
                throw new LoanApplicationException("该用户没有任何融资申请表");

            }
            log.debug("loanApplicationList.size()="+loanApplicationList);
            return loanApplicationList;
        }catch (DbException e){
            throw new DbException(e.getMessage());
        }
    }

    /**
     * description 查看这个融资申请表的详细内容
     *
     * @params [loanApplicationUuid]
     * @return com.nefu.project.domain.entity.LoanApplication
     */
    @Override
    public LoanApplication getLoanApplication(String loanApplicationUuid) {

        stringIsExist(loanApplicationUuid,"融资申请id为空");
        LoanApplication loanApplication = iLoanApplicationMapper.selectByUuid(loanApplicationUuid);
        return iLoanApplicationMapper.selectById(loanApplication.getLoanApplicationId());
    }

    /**
     * description 判断字符是否有值
     *
     * @params [string, message]
     * @return void
     */
    public void stringIsExist(String string, String message) {

        if (string == null || string.trim().isEmpty()) {
            throw new LoanApplicationException(message);
        }
    }


}
