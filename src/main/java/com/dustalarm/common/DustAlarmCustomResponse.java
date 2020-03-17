package com.dustalarm.common;

import java.util.Collection;

public class DustAlarmCustomResponse {
    private Integer pageSize;
    private Integer count;
    private String next;
    private String previous;
    private Collection<?> results;

    public static class Builder {
        private Integer pageSize = 10;
        private Integer count;
        private Collection<?> results;
        private String next;
        private String previous;

        public Builder count(Integer count) {
            this.count = count;
            return this;
        }

        public Builder results(Collection<?> results) {
            this.results = results;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder setNextPreviousUrl(String requestUrl, Integer pageNo) {
            String pageQuery = "?page=%d";
            String nextPageQuery = String.format(pageQuery, pageNo + 1);
            String previousPageQuery = String.format(pageQuery, pageNo - 1);

            // Check Previous Url
            if (pageNo == 1) {
                this.previous = null;
            } else {
                this.previous = requestUrl + previousPageQuery;
            }

            // Check Next Url
            if (pageNo * this.pageSize >= this.count) {
                this.next = null;
            } else {
                this.next = requestUrl + nextPageQuery;
            }
            return this;
        }

        public DustAlarmCustomResponse Build() {
            return new DustAlarmCustomResponse(this);
        }
    }

    public DustAlarmCustomResponse(Builder builder) {
        this.pageSize = builder.pageSize;
        this.count = builder.count;
        this.next = builder.next;
        this.previous = builder.previous;
        this.results = builder.results;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getCount() { return count; }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public Collection<?> getResults() {
        return results;
    }


}


