package com.kimsehw.myteam.constant.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostType {
    MEMBER_RECRUIT("팀원 모집"), MATCH_RECRUIT("매치 모집"), FREE("자유글");

    private String typeName;
}
