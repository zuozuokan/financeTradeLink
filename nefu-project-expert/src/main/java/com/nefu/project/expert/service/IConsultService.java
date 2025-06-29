package com.nefu.project.expert.service;


import com.nefu.project.domain.entity.Consult;

import java.util.List;

public interface IConsultService {
   void updateConsultStatus(String consultUuid,String status);
   List<Consult> getConsultListByUserUuid(String userUuid);
   Consult getConsultByConsultUuid(String consultUuid);
}
