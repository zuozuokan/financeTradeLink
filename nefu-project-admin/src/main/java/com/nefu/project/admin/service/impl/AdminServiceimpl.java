package com.nefu.project.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nefu.project.admin.mapper.IAdminLogMapper;
import com.nefu.project.admin.mapper.IKnowledgeMapper;
import com.nefu.project.admin.mapper.ILoanApplicationMapper;
import com.nefu.project.admin.mapper.IUserMapper;
import com.nefu.project.admin.service.IAdminService;
import com.nefu.project.common.exception.user.DbException;
import com.nefu.project.domain.entity.Knowledge;
import com.nefu.project.domain.entity.LoanApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
}
