package com.africell.africellsurvey.model;

import java.util.ArrayList;

public class FormDataResponse {
    private  Integer totalCount;
    private String next,previous;
    private ArrayList<FormData> items;

    public FormDataResponse(Integer totalCount,String next, String previous, ArrayList<FormData>items){
        this.totalCount =totalCount;
        this.next = next;
        this.previous = previous;
        this.items =items;

    }

    public Integer getCount() {
        return totalCount;
    }

    public void setCount(Integer count) {
        this.totalCount = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public ArrayList<FormData> getResults() {
        return items;
    }

    public void setResults(ArrayList<FormData> results) {
        this.items = results;
    }
}
