package com.shamweel.jsontoforms.validate;

import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.shamweel.jsontoforms.FormConstants;
import com.shamweel.jsontoforms.models.DynamicFieldModel;
import com.shamweel.jsontoforms.models.JSONModel;
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.viewholder.CheckboxxViewHolder;
import com.shamweel.jsontoforms.viewholder.EditTextViewHolder;
import com.shamweel.jsontoforms.viewholder.RadioViewHolder;
import com.shamweel.jsontoforms.viewholder.SpinnerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DynamicFields {
    public static void initDynamicField(RecyclerView recyclerView, List<JSONModel> jsonModelList){

        for(int i=0; i < jsonModelList.size(); i++){
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            String condition = jsonModelList.get(i).getCondition();
            //String name = jsonModelList.get(i).getName();
            //Log.i("CLASSNAME",recyclerView.toString());
            if(!jsonModelList.get(i).getType().equals(1))
            if(!condition.equalsIgnoreCase("")){
                DynamicFieldModel dfm = checkCondition(condition,"");
                if(dfm.isChecked() && !dfm.getClassName().equalsIgnoreCase("")){
                    //Log.i("CLASSNAME",dfm.getClassName());
                    //viewHolder.itemView.setVisibility(View.GONE);
                    showField(recyclerView,jsonModelList,dfm.getClassName());

                }
                else{
                    Log.i("CLASSNAME_HIDE",dfm.getClassName());
                    hideField(recyclerView,jsonModelList,dfm.getClassName());
                    //viewHolder.itemView.setVisibility(View.GONE);
                }
            }

        }


    }
    public static boolean showField(RecyclerView recyclerView, List<JSONModel> jsonModelList, String fieldName){
        for(int i=0; i < jsonModelList.size(); i++){
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            String name = jsonModelList.get(i).getName();
            if(name.equalsIgnoreCase(fieldName)){
                if(viewHolder != null && viewHolder.itemView != null) {
                    ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    viewHolder.itemView.setLayoutParams(params);

                        viewHolder.itemView.setVisibility(View.VISIBLE);
                }
            }
        }
        return true;
    }
    public static boolean hideField(RecyclerView recyclerView, List<JSONModel> jsonModelList, String fieldName) {
        for (int i = 0; i < jsonModelList.size(); i++) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
           // Log.i("NAME", "" + jsonModelList.size());
            String name = jsonModelList.get(i).getName();
            if (name.equalsIgnoreCase(fieldName)) {
                if (viewHolder != null && viewHolder.itemView != null) {
                    DataValueHashMap.put(jsonModelList.get(i).getId(), "");
                    viewHolder.itemView.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = viewHolder.itemView.getLayoutParams();
                    params.height = 0;
                    viewHolder.itemView.setLayoutParams(params);

                    if (viewHolder instanceof EditTextViewHolder) {
                        ((EditTextViewHolder) viewHolder).layoutEdittext.getEditText().setText("");

                    } else if (viewHolder instanceof RadioViewHolder) {
                        for (int j = 0; j < ((RadioViewHolder) viewHolder).rGroup.getChildCount(); j++) {
                            ((RadioButton) ((RadioViewHolder) viewHolder).rGroup.getChildAt(j)).setChecked(false);
                        }
                    } else if (viewHolder instanceof CheckboxxViewHolder) {
                        for (int j = 0; j < ((CheckboxxViewHolder) viewHolder).chCheck.getChildCount(); j++) {
                            ((CheckBox) ((CheckboxxViewHolder) viewHolder).chCheck.getChildAt(j)).setChecked(false);
                        }
                    } else if (viewHolder instanceof SpinnerViewHolder) {
                        ((SpinnerViewHolder) viewHolder).spinner.setSelection(0);
                    }
                }
            }
        }
        return true;
    }

    public static DynamicFieldModel checkCondition(String condition, String mValue){
        boolean checked = false;
        String cClass = "";
        String[] ccClass=null;
        String cAction = "";
        double a,b;
        String cOperator = null;
        String cValue = null; //value to check >
        cAction = null; //action to perform schow or hide
        cClass = null;
        if(!condition.equalsIgnoreCase("")) {
            condition = condition.trim();
            String[] conditionArray = condition.split(" ");
            //check condition length
            if(conditionArray.length < 5){
                cOperator = conditionArray[0];
                cValue = conditionArray[1]; //value to check >
                cAction = conditionArray[2].trim(); //action to perform schow or hide
                cClass = conditionArray[3];
            }
            else{
                cOperator = conditionArray[1];
                 cValue = conditionArray[2]; //value to check >
                cAction = conditionArray[3].trim(); //action to perform schow or hide
                cClass = conditionArray[4];
            }
             // class of items to show or hide
            //ccClass = cClass.split(",");all.va

            switch (cOperator){
                case ">":
                    if(!cValue.isEmpty() && !mValue.isEmpty() && isNumeric(mValue)) {
                        a = Double.parseDouble(cValue);
                        b = Double.parseDouble(mValue);
                        if (b > a) {
                            checked = true;
                        }
                    }
                    break;
                case">=":
                    if(!cValue.isEmpty() && !mValue.isEmpty() && isNumeric(mValue)) {
                        a = Double.parseDouble(cValue);
                        b = Double.parseDouble(mValue);
                        if (b >= a) {
                            checked = true;

                        }
                    }
                    break;
                case "<":
                    if(!cValue.isEmpty() && !mValue.isEmpty()) {
                        a = Double.parseDouble(cValue);
                        b = Double.parseDouble(mValue);
                        if (b < a) {
                            checked = true;

                        }
                    }
                    break;
                case "<=":
                    if(!cValue.isEmpty() && !mValue.isEmpty() && isNumeric(mValue)) {
                        a = Double.parseDouble(cValue);
                        b = Double.parseDouble(mValue);
                        if (b <= a) {
                            checked = true;

                        }
                    }
                    break;
                case"=":
                    Log.i("CONDITION",cValue+" "+mValue);
                    if(cValue.equalsIgnoreCase(mValue)){
                        checked = true;
                        Log.i("CONDITION","equal");

                    }
                    break;
                case "!=":
                    if(!cValue.equalsIgnoreCase(mValue)){
                        checked  = true;

                    }
                    break;
                case "x":
                    checked=true;
                    break;

                default:
                    break;


            }

            /*if (cValue.equalsIgnoreCase(mValue) && cAction.equalsIgnoreCase("show")) {
                checked=true;
            } else {
                checked=false;
            }*/
        }

        return new DynamicFieldModel(checked,cClass,cAction);
    }
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}
