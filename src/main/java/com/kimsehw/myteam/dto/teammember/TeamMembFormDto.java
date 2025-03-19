package com.kimsehw.myteam.dto.teammember;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeamMembFormDto {

    @NotBlank(message = "회원 아이디를 입력해주세요.")
    private String email;

}
