package com.nefu.project.user.service;


import com.nefu.project.domain.entity.Consult;

import java.util.List;

public interface IConsultService {
   void addConsult(String userUuid,Consult consult);
   void deleteConsult(String userUuid,String consultUuid);
   void updateConsult(String userUuid,Consult consult);
   void updateConsultStatus(String consultUuid,String status);
   List<Consult> getConsultListByUserUuid(String userUuid);
   Consult getConsultByConsultUuid(String consultUuid);
}
