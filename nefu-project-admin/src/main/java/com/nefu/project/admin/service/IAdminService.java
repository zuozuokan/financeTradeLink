package com.nefu.project.admin.service;


import com.nefu.project.admin.dto.PageResult;
import com.nefu.project.domain.entity.Expert;
import com.nefu.project.domain.entity.Knowledge;
import com.nefu.project.domain.entity.LoanApplication;
import com.nefu.project.domain.entity.User;

import java.util.List;
import java.util.Map;

public interface IAdminService {

    /**
     * @description:  添加银行和专家角色
     * @param: [username, password]
     * @return: boolean
     */
    boolean addBank(String username,String password);
    boolean addExpert(String username,String password);

    /**
     * @description: 审核融资项目
     * @param: [loanUuid]
     * @return: boolean
     */
    boolean checkLoan(String loanUuid,String adminAdvice);
    PageResult<LoanApplication> loanList(Map<String, Object> params);
    /**
     * @description: 添加知识库（内容管理）
     * @param: [knowledge]
     * @return: boolean
     */
    boolean addKnowledge (Knowledge knowledge);
    boolean deleteKnowledge (String knowledgeUuid);
    boolean updateKnowledge (Knowledge knowledge);

    /**
     * @description: 获取所有用户
     * @param: []
     * @return: java.util.List<com.nefu.project.domain.entity.User>
     */
    List<User> getAllUser();
    List<User> getAllBank();
    List<User> getAllExpert();
    List<Expert> getExpertsInfo(String userUuid);
    void updateExpertStatus(String userUuid,String expertUuid);
    boolean operationUser(String userUuid,String status);


}
