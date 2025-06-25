package com.nefu.project.admin.service;

import com.nefu.project.domain.entity.Knowledge;

public interface IAdminService {

    /**
     * @description: 审核融资项目
     * @param: [loanUuid]
     * @return: boolean
     */
    boolean checkLoan(String loanUuid,String adminAdvice);

    /**
     * @description: 添加知识库（内容管理）
     * @param: [knowledge]
     * @return: boolean
     */
    boolean addKnowledge (Knowledge knowledge);
    boolean deleteKnowledge (String knowledgeUuid);
    boolean updateKnowledge (Knowledge knowledge);

}
