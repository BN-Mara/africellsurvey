package com.africell.africellsurvey.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class FormData {
    private String formId;
    private JSONObject result;
    public FormData(String formId, JSONObject result){
        this.formId = formId;
        this.result = result;
    }
}
