package com.kimsehw.myteam.dto;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class CustomPage<T> {
    private List<T> content;
    private int number;
    private int totalPages;
    private long totalElements;
    private boolean last;

    public CustomPage(Page<T> page) {
        this.content = page.getContent();
        this.number = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.last = page.isLast();
    }
}
