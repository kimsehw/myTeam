package com.kimsehw.myteam.constant.serch;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchDateType {
    ALL("전체 기간"), TODAY("오늘"), ONE_WEEK("1주"), ONE_MONTH("1개월"), SIX_MONTH("6개월"), ONE_YEAR("1년");

    private String text;
}
