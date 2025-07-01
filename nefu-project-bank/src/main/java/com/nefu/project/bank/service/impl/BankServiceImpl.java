package com.nefu.project.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nefu.project.bank.mapper.IBankMapper;
import com.nefu.project.bank.mapper.ILoanApplicationMapper;
import com.nefu.project.bank.mapper.IUserMapper;
import com.nefu.project.bank.service.IBankService;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.common.exception.user.LoanApplicationException;
import com.nefu.project.common.exception.user.UserException;
import com.nefu.project.domain.entity.LoanApplication;
import com.nefu.project.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BankServiceImpl implements IBankService {

    @Autowired
    private IBankMapper iBankMapper;
    @Autowired
    private ILoanApplicationMapper iLoanApplicationMapper;
    @Autowired
    private IUserMapper iUserMapper;
    /**
     * @description: 更新银行的管理意见
     * @param: [loanUuid, adminAdvice]
     * @return: boolean
     */
    @Override
    public boolean checkLoan(String loanUuid,String BankAdvice) {
        LambdaUpdateWrapper<LoanApplication> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(LoanApplication::getLoanApplicationUuid,loanUuid)
                .set(LoanApplication::getLoanApplicationStatus,BankAdvice);
        try{
            int rows = iBankMapper.update(null,lambdaUpdateWrapper);
            if (rows > 0) {
                return true;
            }
        }catch (DbException e){
            throw new DbException("数据库操作异常");
        }

        return false;
    }
    /**
     * description 获取该用户的融资申请表
     *
     * @params
     * @return java.util.List<com.nefu.project.domain.entity.LoanApplication>
     */
    @Override
    public List<LoanApplication> getLoanApplicationList(String userUuid) {

        //传入值为空
        stringIsExist(userUuid,"无法正常获取用户id");
        // 检查用户是否存在
        User user = iUserMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUserUuid, userUuid)
                        .eq(User::getUserStatus,"1")
                        .eq(User::getUserRole,"BANK")

        );
        if (Objects.isNull(user)) {
            throw new UserException("用户不存在或无权限进行此操作");
        }
        //查询数据库
        try{
            List<LoanApplication> loanApplicationList = iLoanApplicationMapper.getLoanApplicationListByUserUuid();
            if (loanApplicationList.size()==0) {
                throw new LoanApplicationException("暂时没有任何融资申请表");

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
        if (loanApplication==null) {
            throw new LoanApplicationException("没有该融资申请");
        }
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
