package com.nefu.project.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpertRequest {
    String expertUuid;
    String expertName;
    String expertphone;
    String expertTitle;
    String expertHeadshotUrl;
    String expertIntroduction;
    String expertCertificateUrl;
    String expertSpecialty;
}
