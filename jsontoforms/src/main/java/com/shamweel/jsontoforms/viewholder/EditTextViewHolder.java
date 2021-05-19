package com.shamweel.jsontoforms.viewholder;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.textfield.TextInputLayout;
import com.shamweel.jsontoforms.R;
import com.shamweel.jsontoforms.interfaces.JsonToFormClickListener;
import com.shamweel.jsontoforms.models.DynamicFieldModel;
import com.shamweel.jsontoforms.models.JSONModel;
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.validate.DynamicFields;

import java.util.List;


public class EditTextViewHolder extends RecyclerView.ViewHolder {

    public TextInputLayout layoutEdittext;
    public JsonToFormClickListener jsonToFormClickListener;


    public EditTextViewHolder(@NonNull View itemView, List<JSONModel> jsonModelList,JsonToFormClickListener jsonToFormClickListener) {
        super(itemView);
        layoutEdittext = itemView.findViewById(R.id.layout_edittext);
        this.jsonToFormClickListener = jsonToFormClickListener;


        layoutEdittext.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (getAdapterPosition() == -1) {
                    return;
                }

                DataValueHashMap.put(
                        jsonModelList.get(getAdapterPosition()).getId(),
                        editable.toString());

                String condition = jsonModelList.get(getAdapterPosition()).getCondition();
                String thisclass = jsonModelList.get(getAdapterPosition()).getName();



                if(!condition.equalsIgnoreCase("")){
                    displayFields(condition, editable.toString());

                }
                /*if(condition.split(" ").length > 4)
                    checkAllFieldCondition(jsonModelList,editable.toString(), thisclass);
                    
                 */


                if (editable.length() > 0 && layoutEdittext.isErrorEnabled()){
                    layoutEdittext.setErrorEnabled(false);
                }

            }
        });


    }
    public void checkAllFieldCondition(List<JSONModel> jsonModelList, String cValue, String myClass){
        for(JSONModel model : jsonModelList){
            if(!model.getCondition().equalsIgnoreCase("")){
                String condition = model.getCondition().trim();
                String[] conditionArray = condition.split(" ");
                //check condition length
                String cval = conditionArray[0];
                String cOperator = conditionArray[1];
                String ccValue = conditionArray[2]; //value to check >
                String cAction = conditionArray[3].trim(); //action to perform schow or hide
                String cClass = conditionArray[4];

                if(cval.length() > 4 && cval.substring(ccValue.length()-4).equalsIgnoreCase(".val")
                && cval.contains(myClass)) {

                    //displayFields(model.getCondition(), cValue);
                }
            }

        }

    }

    public void displayFields(String condition, String mValue){
        DynamicFieldModel dfm = DynamicFields.checkCondition(condition,mValue);

        if (dfm.isChecked() && !dfm.getClassName().equalsIgnoreCase("")) {
            String[] arClass = dfm.getClassName().split(",");
            if(condition.equalsIgnoreCase("x")){
                int ln = Integer.parseInt(mValue);
                for(int k=0; k<ln; k++){
                    jsonToFormClickListener.showHideField(arClass[k], dfm.getAction());
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
