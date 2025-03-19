package com.kimsehw.myteam.dto.team;

import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamFormDto {

    @NotBlank(message = "팀명은 필수 입력 값 입니다.")
    private String teamName;

    @NotNull(message = "활동 지역은 필수 입력 값 입니다.")
    private Region region;

    @NotNull(message = "나이대를 최소 하나 설정해주세요.")
    private AgeRange ageRange;

    private String teamDetail;
}
