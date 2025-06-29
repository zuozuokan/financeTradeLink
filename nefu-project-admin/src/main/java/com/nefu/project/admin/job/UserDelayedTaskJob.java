package com.nefu.project.admin.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.nefu.project.admin.mapper.*;
import com.nefu.project.domain.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDelayedTaskJob implements Job {

    @Autowired
    private IUserMapper iUserMapper;

    @Autowired
    private IInvestmentRecordMapper iInvestmentRecordMapper;

    @Autowired
    private IAddressMapper iAddressMapper;

    @Autowired
    private IProductMapper iProductMapper;

    @Autowired
    private ICartMapper iCartMapper;

    @Autowired
    private IDemandMapper iDemandMapper;

    @Autowired
    private IOrderMapper iOrderMapper;

    @Autowired
    private IDiscussMapper iDiscussMapper;

    @Autowired
    private IKnowledgeMapper iKnowledgeMapper;

    @Autowired
    private ILoanApplicationMapper iLoanApplicationMapper;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String userUuid = dataMap.getString("userUuid");
        // TODO: 执行你实际的业务逻辑
        // 删除改用户关联的所有信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserUuid, userUuid);
        int rows = iUserMapper.delete(queryWrapper);
        if (rows > 0) {
            log.info("删除用户表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<Address> addressQueryWrapper = new LambdaQueryWrapper<>();
        addressQueryWrapper.eq(Address::getAddressUserUuid, userUuid);
        int addressRows = iAddressMapper.delete(addressQueryWrapper);
        if (addressRows > 0) {
            log.info("删除地址表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<LoanApplication> loanApplicationQueryWrapper = new LambdaQueryWrapper<>();
        loanApplicationQueryWrapper.eq(LoanApplication::getLoanApplicationUuid, userUuid);
        int loanApplicationRows = iLoanApplicationMapper.delete(loanApplicationQueryWrapper);
        if (loanApplicationRows > 0) {
            log.info("删除贷款申请表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<Cart> cartQueryWrapper = new LambdaQueryWrapper<>();
        cartQueryWrapper.eq(Cart::getCartUserUuid, userUuid);
        int cartRows = iCartMapper.delete(cartQueryWrapper);
        if (cartRows > 0) {
            log.info("删除购物车表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<Order> orderQueryWrapper = new LambdaQueryWrapper<>();
        orderQueryWrapper.eq(Order::getOrderUserUuid, userUuid);
        int orderRows = iOrderMapper.delete(orderQueryWrapper);
        if (orderRows > 0) {
            log.info("删除订单表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<Product> productQueryWrapper = new LambdaQueryWrapper<>();
        productQueryWrapper.eq(Product::getProductUserUuid, userUuid);
        int productRows = iProductMapper.delete(productQueryWrapper);
        if (productRows > 0) {
            log.info("删除产品表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<Demand> demandQueryWrapper = new LambdaQueryWrapper<>();
        demandQueryWrapper.eq(Demand::getDemandUserUuid, userUuid);
        int demandRows = iDemandMapper.delete(demandQueryWrapper);
        if (demandRows > 0) {
            log.info("删除需求表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<Discuss> discussQueryWrapper = new LambdaQueryWrapper<>();
        discussQueryWrapper.eq(Discuss::getDiscussUserUuid, userUuid);
        int discussRows = iDiscussMapper.delete(discussQueryWrapper);
        if (discussRows > 0) {
            log.info("删除讨论表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<Knowledge> knowledgeQueryWrapper = new LambdaQueryWrapper<>();
        knowledgeQueryWrapper.eq(Knowledge::getKnowledgeAuthorUuid, userUuid);
        int knowledgeRows = iKnowledgeMapper.delete(knowledgeQueryWrapper);
        if (knowledgeRows > 0) {
            log.info("删除知识表中用户成功，用户UUID: {}", userUuid);
        }
        LambdaQueryWrapper<InvestmentRecord> investmentRecordQueryWrapper = new LambdaQueryWrapper<>();
        investmentRecordQueryWrapper.eq(InvestmentRecord::getInvestmentRecordInvestorUuid, userUuid);
        int investmentRecordRows = iInvestmentRecordMapper.delete(investmentRecordQueryWrapper);
        if (investmentRecordRows > 0) {
            log.info("删除投资记录表中用户成功，用户UUID: {}", userUuid);
        }
        log.debug("该封禁/注销用户关联的所有信息已全部删除完毕");


    }
}

