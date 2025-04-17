package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.constant.post.PostType;
import com.kimsehw.myteam.constant.search.SearchDateType;
import com.kimsehw.myteam.constant.search.SearchType;
import lombok.Data;

@Data
public class PostSearchDto {

    private PostType postType;

    private String fromDate;

    private String toDate;

    private SearchDateType searchDateType;

    private SearchType searchType;

    private String searchQuery;

}
