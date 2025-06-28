package com.nefu.project.expert.service;

import com.nefu.project.domain.entity.Expert;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IExpertService {
    void addExpert(String userUuid,  MultipartFile headfile, MultipartFile certificatefile, Expert expert);
    void deleteExpert(String userUuid, String expertUuid);
    void updateExpert(String expertUuid,  Expert expert);
    void updateExpertHeadshot(String expertUuid, MultipartFile headFile);
    void updateExpertCertificate(String expertUuid,MultipartFile certificateFile);
    List<Expert> getExperts(String userUuid);
    Expert getExpert(String expertUuid);
}