package com.kimsehw.myteam.constant.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchType {

    TITLE("제목"), CONTENT("내용"), WRITER("작성자"), TEAM_NAME("팀명");

    private String text;
}
