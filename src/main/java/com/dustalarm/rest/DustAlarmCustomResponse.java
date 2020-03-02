package com.dustalarm.rest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DustAlarmCustomResponse {
    private Integer count;
    private String next;
    private String previous;
    private Collection<?> results;

    public DustAlarmCustomResponse() {
    }

    public DustAlarmCustomResponse(Collection<?> totalResults, Collection<?> results) {
        this.count = totalResults.size();
        this.results = results;
    }

    public Integer getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public String getPrevious() {
        return previous;
    }

    public Collection<?> getResults() {
        return results;
    }

    public void setNextPreviousUrl(HttpServletRequest request, Integer pageNo, Collection<?> totalObjects) {
        Map<String, String> pagedLinkHashMap = this.getPagedLink(request, pageNo, totalObjects);
        this.next = pagedLinkHashMap.get("next");
        this.previous = pagedLinkHashMap.get("previous");
    }

    private Map<String, String> getPagedLink(HttpServletRequest request, Integer pageNo, Collection<?> totalObjects) {
        Map<String, String> pagedLinkHashMap = new HashMap<>();
        String requestURL = request.getRequestURL().toString();
        int pageSize = 10;
        String pageQuery = "?page=%d";

        String nextPageQuery = String.format(pageQuery, pageNo + 1);
        String previousPageQuery = String.format(pageQuery, pageNo - 1);


        pagedLinkHashMap.put("next", requestURL + nextPageQuery);
        pagedLinkHashMap.put("previous", requestURL + previousPageQuery);

        if (pageNo == 1) {
            pagedLinkHashMap.replace("previous", null);
        } else {
        }

        if (pageNo * pageSize >= totalObjects.size()) {
            pagedLinkHashMap.replace("next", null);
        } else {
        }

        return pagedLinkHashMap;
    }

}


