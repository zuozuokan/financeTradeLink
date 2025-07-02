package com.nefu.project.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j

public class ConsultRequest {
    String consultUuid;
    String consultTitle;
    String consultType;
    String consultStatus;
    String consultExpertUserUuid;
    String consultExpertName;
    String consultDescription;
    Date consultAppointTime;
    Date consultCreateTime;
    Date consultUpdateTime;
}
