package com.kimsehw.myteam.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ScheduleDto {
    private LocalDateTime schedule;
    private String name;
    private String type;
}
