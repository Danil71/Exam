package com.software.software_development.web.dto.pagination;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDto<D>  {
    private List<D> items = new ArrayList<>();
    private int itemsCount;
    private int currentPage;
    private int currentSize;
    private int totalPages;
    private long totalItems;
    private boolean isFirst;
    private boolean isLast;
    private boolean hasNext;
    private boolean hasPrevious;
}
