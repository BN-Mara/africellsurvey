package com.shamweel.jsontoforms.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shamweel.jsontoforms.FormConstants;
import com.shamweel.jsontoforms.R;
import com.shamweel.jsontoforms.interfaces.JsonToFormClickListener;
import com.shamweel.jsontoforms.models.DynamicFieldModel;
import com.shamweel.jsontoforms.models.JSONModel;
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.validate.DynamicFields;

import java.util.List;


public class RadioViewHolder extends RecyclerView.ViewHolder {

    public TextView txtRadio;
    public RadioGroup rGroup;
    JsonToFormClickListener  mSubmitBtnListener;

    public RadioViewHolder(@NonNull View itemView, List<JSONModel> jsonModelList, JsonToFormClickListener mSubmitBtnListener) {
        super(itemView);
        txtRadio = itemView.findViewById(R.id.txt_radio);
        rGroup = itemView.findViewById(R.id.rGroup);
        this.mSubmitBtnListener = mSubmitBtnListener;
        //Log.i("ADAPTER_POS",""+getAdapterPosition());
        if (getAdapterPosition() != -1) {
            displayFields(jsonModelList.get(getAdapterPosition()).getCondition(), "");
        }

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (getAdapterPosition() == -1) {
                    return;
                }
                if(i == -1)
                    return;

                String rdValue = rGroup.getCheckedRadioButtonId() == -1 ? FormConstants.EMPTY_STRING : ((RadioButton) itemView.findViewById(rGroup.getCheckedRadioButtonId())).getText().toString();


                if(!jsonModelList.get(getAdapterPosition()).getList().get(i).getIndexValue().equalsIgnoreCase("")){
                    rdValue = jsonModelList.get(getAdapterPosition()).getList().get(i).getIndexValue();
                }
                DataValueHashMap.put(
                        jsonModelList.get(getAdapterPosition()).getId(), rdValue );

                Log.i("ADAPTER_POS",""+getAdapterPosition());
                String condition = jsonModelList.get(getAdapterPosition()).getCondition();
                if(!condition.equalsIgnoreCase(""))
                    displayFields(condition, rdValue);

                if (rGroup.getCheckedRadioButtonId() != -1){
                    for (int j=0; j<radioGroup.getChildCount(); j++){
                        ((RadioButton)rGroup.getChildAt(j)).setError(null);

                    }
                }

                if (itemView.getRootView().findFocus() != null) {
                    itemView.getRootView().findFocus().clearFocus();
                }
            }
        });



    }
    public void displayFields(String condition, String mValue){
        DynamicFieldModel dfm = DynamicFields.checkCondition(condition,mValue);

            if (dfm.isChecked() && !dfm.getClassName().equalsIgnoreCase("")) {
                mSubmitBtnListener.showHideField(dfm.getClassName(), "show");
            } else {
                if(!dfm.getClassName().equalsIgnoreCase(""))
                    mSubmitBtnListener.showHideField(dfm.getClassName(), "hide");
            }
        }
}

