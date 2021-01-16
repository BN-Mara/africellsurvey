package com.shamweel.jsontoforms.validate;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.shamweel.jsontoforms.models.JSONModel;

import java.util.List;

public class DynamicFields {
    public static boolean showField(RecyclerView recyclerView, List<JSONModel> jsonModelList, String fieldName){
        for(int i=0; i < jsonModelList.size(); i++){
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            String name = jsonModelList.get(i).getName();
            if(name.equalsIgnoreCase(fieldName)){
                if(viewHolder != null) {
                        viewHolder.itemView.setVisibility(View.VISIBLE);
                }
            }
        }
        return true;
    }
    public static boolean hideField(RecyclerView recyclerView, List<JSONModel> jsonModelList, String fieldName){
        for(int i=0; i < jsonModelList.size(); i++){
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            Log.i("NAME",""+jsonModelList.size());
            String name = jsonModelList.get(i).getName();
            Log.i("NAME",name);
            if(name.equalsIgnoreCase(fieldName)){
                if(viewHolder != null) {
                    viewHolder.itemView.setVisibility(View.GONE);
                }
            }
        }
        return true;
    }
}
