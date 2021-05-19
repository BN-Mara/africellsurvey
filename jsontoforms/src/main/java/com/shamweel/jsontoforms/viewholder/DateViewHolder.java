package com.shamweel.jsontoforms.viewholder;

import android.app.DatePickerDialog;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shamweel.jsontoforms.R;
import com.shamweel.jsontoforms.interfaces.JsonToFormClickListener;
import com.shamweel.jsontoforms.models.DynamicFieldModel;
import com.shamweel.jsontoforms.models.JSONModel;
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.validate.DynamicFields;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class DateViewHolder extends RecyclerView.ViewHolder {

    public TextInputLayout layoutDate;
    public TextInputEditText etdate;
    JsonToFormClickListener jsonToFormClickListener;


    public DateViewHolder(@NonNull final View itemView, List<JSONModel> jsonModelList, JsonToFormClickListener jsonToFormClickListener) {
        super(itemView);
        layoutDate = (TextInputLayout)itemView.findViewById(R.id.layout_date);
        etdate = (TextInputEditText)itemView.findViewById(R.id.et_date);
        this.jsonToFormClickListener = jsonToFormClickListener;

        layoutDate.setOnClickListener(view ->
                onDateViewClick(jsonModelList, getAdapterPosition()));
        etdate.setOnClickListener(view -> onDateViewClick(jsonModelList, getAdapterPosition()));

    }



    void onDateViewClick(List<JSONModel> jsonModelList, int position) {

        if (getAdapterPosition() == -1) {
            return;
        }

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yyyy"; // your format
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

            layoutDate.getEditText().setText(sdf.format(myCalendar.getTime()));
            DataValueHashMap.put(jsonModelList.get(position).getId(), sdf.format(myCalendar.getTime()));
            String condition = jsonModelList.get(position).getCondition();
            if(!condition.equalsIgnoreCase(""))
                displayFields(condition, sdf.format(myCalendar.getTime()));


        };
        new DatePickerDialog(itemView.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();

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
