package com.shamweel.jsontoforms.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.jolly.checkboxgroup.CheckboxGroup;
import com.shamweel.jsontoforms.FormConstants;
import com.shamweel.jsontoforms.R;
import com.shamweel.jsontoforms.interfaces.JsonToFormClickListener;
import com.shamweel.jsontoforms.models.DynamicFieldModel;
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.models.JSONModel;
import com.shamweel.jsontoforms.validate.DynamicFields;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CheckboxxViewHolder extends RecyclerView.ViewHolder {
    public TextView txtCheckbox;
    public CheckboxGroup chCheck;
    public JsonToFormClickListener jsonToFormClickListener;
    public CheckboxxViewHolder(@NonNull View itemView,  List<JSONModel> jsonModelList, JsonToFormClickListener jsonToFormClickListener) {
        super(itemView);
        txtCheckbox = itemView.findViewById(R.id.txt_checkboxx);
        chCheck = itemView.findViewById(R.id.chGroup);
        List<String> checked = new LinkedList<>();
        this.jsonToFormClickListener = jsonToFormClickListener;
        chCheck.onCheckBoxListener(new CheckboxGroup.onSelected() {
            @Override
            public void itemSelected(CheckboxGroup group, int pos, int[] checkedArray) {

                if (getAdapterPosition() == -1) {
                    return;
                }
                //Log.i("ITEMSELECTED",""+pos);
                String chValue = checkedArray[pos] == -1 ? FormConstants.EMPTY_STRING : ((CheckBox) itemView.findViewById(pos)).getText().toString();

                if(!jsonModelList.get(getAdapterPosition()).getList().get(pos).getIndexValue().equalsIgnoreCase("")){
                    chValue = jsonModelList.get(getAdapterPosition()).getList().get(pos).getIndexValue();
                }

                if(checkedArray[pos] == 1)
                    checked.add(chValue);
                else
                    checked.remove(chValue);
                DataValueHashMap.put(
                        jsonModelList.get(getAdapterPosition()).getId(),  listToString(checked));

                String condition = jsonModelList.get(getAdapterPosition()).getCondition();
                if(!condition.equalsIgnoreCase(""))
                    displayFields(condition, chValue);

                if (itemView.getRootView().findFocus() != null) {
                    itemView.getRootView().findFocus().clearFocus();
                }

            }

        });

    }

    //comma separated value
    public String listToString(List<String> list){
        StringBuilder csv = new StringBuilder();

        for (int i=0;i<list.size();i++)
        {
            csv.append(list.get(i));
            if(i < list.size()-1){
                csv.append(",");
            }

        }


        return csv.toString();
    }
    public void displayFields(String condition, String mValue){
        DynamicFieldModel dfm = DynamicFields.checkCondition(condition,mValue);

        if (dfm.isChecked() && !dfm.getClassName().equalsIgnoreCase("")) {
            jsonToFormClickListener.showHideField(dfm.getClassName(), dfm.getAction());
        } else {
            if(!dfm.getClassName().equalsIgnoreCase(""))
                jsonToFormClickListener.showHideField(dfm.getClassName(), dfm.getAction());
        }
    }
}
