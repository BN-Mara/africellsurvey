package com.africell.africellsurvey.model;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

public class FormDataResponse {
    //private  Integer totalCount;
    private String id,formId;
    private JsonObject values;

    public FormDataResponse(String id, String formId, JsonObject values){
        this.id =id;
        this.formId = formId;
        this.values = values;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormId() {
        return formId;
    }

    public void setValues(JsonObject values) {
        this.values = values;
    }

    public JsonObject getValues() {
        return values;
    }
}
