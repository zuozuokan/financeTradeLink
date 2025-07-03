package com.nefu.project.user.service;


import com.nefu.project.domain.entity.Consult;
import com.nefu.project.domain.entity.Expert;
import com.nefu.project.user.dto.ConsultRequest;
import com.nefu.project.user.dto.ExpertRequest;

import java.util.List;

public interface IConsultService {
   void addConsult(String userUuid,Consult consult);
   void deleteConsult(String userUuid,String consultUuid);
   void updateConsult(String userUuid,Consult consult);
   void updateConsultStatus(String consultUuid,String status);
   List<ConsultRequest> getConsultListByUserUuid(String userUuid);
   ConsultRequest getConsultByConsultUuid(String consultUuid);
   List<ExpertRequest> getExpertListByUserUuid(String userUuid);
   Expert getExpert(String expertUserUuid);
}
