package com.shamweel.jsontoforms.viewholder;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shamweel.jsontoforms.R;
import com.shamweel.jsontoforms.interfaces.JsonToFormClickListener;
import com.shamweel.jsontoforms.models.DynamicFieldModel;
import com.shamweel.jsontoforms.models.JSONModel;
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.validate.DynamicFields;

import java.util.List;


public class SpinnerViewHolder extends RecyclerView.ViewHolder {

    public TextView txtSpinner;
    public Spinner spinner;
    public JsonToFormClickListener jsonToFormClickListener;


    public SpinnerViewHolder(@NonNull View itemView, List<JSONModel> jsonModelList,JsonToFormClickListener jsonToFormClickListener) {
        super(itemView);
        txtSpinner = itemView.findViewById(R.id.txt_spinner);
        spinner = itemView.findViewById(R.id.spinner);
        this.jsonToFormClickListener = jsonToFormClickListener;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (getAdapterPosition() == -1) {
                    return;
                }

                String selectedValue = spinner.getSelectedItem().toString();
                selectedValue = jsonModelList.get(getAdapterPosition()).getList().get(i).getIndexValue();
                DataValueHashMap.put(
                        jsonModelList.get(getAdapterPosition()).getId(),
                        selectedValue);

                String condition = jsonModelList.get(getAdapterPosition()).getCondition();
                if(!condition.equalsIgnoreCase(""))
                    displayFields(condition, selectedValue);

                if (itemView.getRootView().findFocus() != null) {
                   itemView.getRootView().findFocus().clearFocus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void displayFields(String condition, String mValue){
        DynamicFieldModel dfm = DynamicFields.checkCondition(condition,mValue);

        if (dfm.isChecked() && !dfm.getClassName().equalsIgnoreCase("")) {
            jsonToFormClickListener.showHideField(dfm.getClassName(), dfm.getAction());
        } else {
            if(!dfm.getClassName().equalsIgnoreCase(""))
                jsonToFormClickListener.showHideField(dfm.getClassName(), "hide");
        }
    }
}

