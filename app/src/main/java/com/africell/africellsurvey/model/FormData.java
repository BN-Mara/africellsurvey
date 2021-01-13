package com.africell.africellsurvey.model;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class FormData {
    private String formId;
    private JsonObject values;
    public FormData(String formId, JsonObject values){
        this.formId = formId;
        this.values = values;
    }
}
