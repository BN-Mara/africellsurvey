package com.africell.africellsurvey.model;

import java.util.ArrayList;

public class SurveyFormResponse {
    private  Integer totalCount;
    private String next,previous;
    private ArrayList<SurveyForm>items;

    public SurveyFormResponse(Integer totalCount,String next, String previous, ArrayList<SurveyForm>items){
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

    public ArrayList<SurveyForm> getResults() {
        return items;
    }

    public void setResults(ArrayList<SurveyForm> results) {
        this.items = results;
    }
}
