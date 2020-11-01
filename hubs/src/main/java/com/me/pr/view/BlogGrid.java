package com.me.pr.view;

import java.util.List;

import com.me.pr.model.entities.Blog;

public class BlogGrid {
    private int totalPages;
    private int currentPage;
    private long totalRecords;
    private List<Blog> blogData;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<Blog> getBlogData() {
        return blogData;
    }

    public void setBlogData(List<Blog> contactData) {
        this.blogData = contactData;
    }
}