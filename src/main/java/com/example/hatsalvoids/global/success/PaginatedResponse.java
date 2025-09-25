package com.example.hatsalvoids.global.success;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedResponse<T> {
    private int size;
    private int page;
    private int currentCounts;
    private long totalCounts;
    private int totalPages;
    private List<T> content;

    public static <T> PaginatedResponse<T> from(Page<T> page) {
        return new PaginatedResponse<>(
                page.getSize(),
                page.getNumber(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getContent()
        );
    }

    public static <T> PaginatedResponse<T> of(int size, int page, long totalCounts, int totalPages, List<T> content) {
        return new PaginatedResponse<>(size, page, content.size(), totalCounts, totalPages, content);
    }
}
