package com.pbw.application.custom;

import lombok.Getter;

@Getter
public class PaginationResponse<T> {
    private boolean success;
    private T data;
    private int currentPage;
    private int itemsPerPage;
    private int totalItems;

    public PaginationResponse(boolean success, T data, int currentPage, int itemsPerPage, int totalItems) {
        this.data = data;
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.totalItems = totalItems;
    }


}
