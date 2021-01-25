package com.shamweel.jsontoforms.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListModel {
    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("index_text")
    @Expose
    private String indexText;

    private String  indexValue;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getIndexText() {
        return indexText;
    }

    public void setIndexText(String indexText) {
        this.indexText = indexText;
    }

    public String getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(String indexValue) {
        this.indexValue = indexValue;
    }
}
