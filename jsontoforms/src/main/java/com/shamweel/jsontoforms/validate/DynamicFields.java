package com.shamweel.jsontoforms.validate;

import android.util.Log;
import android.view.View;
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
            Log.i("CLASSNAME",condition);
            if(!condition.equalsIgnoreCase("")){
                DynamicFieldModel dfm = checkCondition(condition,"");
                if(dfm.isChecked() && !dfm.getClassName().equalsIgnoreCase("")){
                    Log.i("CLASSNAME",dfm.getClassName());
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
        if(!condition.equalsIgnoreCase("")) {
            condition = condition.trim();
            String[] condictionArray = condition.split(" ");
            String cValue = condictionArray[0]; //value to check
            String cAction = condictionArray[1].trim(); //action to perform schow or hide
            cClass = condictionArray[2].trim(); // class of items to show or hide
            if (cValue.equalsIgnoreCase(mValue) && cAction.equalsIgnoreCase("show")) {
                checked=true;
            } else {
                checked=false;
            }
        }

        return new DynamicFieldModel(checked,cClass);
    }
}
