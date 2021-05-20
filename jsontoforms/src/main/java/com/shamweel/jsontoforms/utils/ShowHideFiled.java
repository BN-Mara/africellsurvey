package com.shamweel.jsontoforms.utils;

import android.util.Log;

import com.shamweel.jsontoforms.interfaces.JsonToFormClickListener;
import com.shamweel.jsontoforms.models.DynamicFieldModel;
import com.shamweel.jsontoforms.validate.DynamicFields;

public class ShowHideFiled {
    public JsonToFormClickListener jsonToFormClickListener;
    //String condition, mvalue;
    public ShowHideFiled(JsonToFormClickListener jsonToFormClickListener){
        this.jsonToFormClickListener = jsonToFormClickListener;

    }

    public void displayFields(String condition, String mValue){
        String [] conditions = condition.trim().split(";");
        Log.d("conditions",conditions.length+"");

        for(int c = 0; c < conditions.length; c++){
            Log.d("conditions",conditions[c]);
            DynamicFieldModel dfm = DynamicFields.checkCondition(conditions[c],mValue);
            if (dfm.isChecked() && !dfm.getClassName().equalsIgnoreCase("")) {
                String[] arClass = dfm.getClassName().split(",");
                if(conditions[c].trim().charAt(0)=='x' || conditions[c].charAt(0)=='X'){
                    int ln;
                    if(DynamicFields.isNumeric(mValue))
                    {
                        ln = Integer.parseInt(mValue);
                        for(int k=0; k<ln; k++){
                            jsonToFormClickListener.showHideField(arClass[k], dfm.getAction());
                        }

                    }
                }else {
                    for (String aClass1 : arClass)
                        jsonToFormClickListener.showHideField(aClass1, dfm.getAction());
                }
            } else {
                if(!dfm.getClassName().equalsIgnoreCase("")){
                    String[] arClass = dfm.getClassName().split(",");
                    for (String aClass1 : arClass)
                        jsonToFormClickListener.showHideField(aClass1, "hide");
                }

            }

        }

    }
}
