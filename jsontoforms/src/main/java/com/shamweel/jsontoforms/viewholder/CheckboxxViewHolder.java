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
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.models.JSONModel;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class CheckboxxViewHolder extends RecyclerView.ViewHolder {
    public TextView txtCheckbox;
    public CheckboxGroup chCheck;
    public CheckboxxViewHolder(@NonNull View itemView,  List<JSONModel> jsonModelList) {
        super(itemView);
        txtCheckbox = itemView.findViewById(R.id.txt_checkboxx);
        chCheck = itemView.findViewById(R.id.chGroup);
        List<String> checked = new LinkedList<>();
        chCheck.onCheckBoxListener(new CheckboxGroup.onSelected() {
            @Override
            public void itemSelected(CheckboxGroup group, int pos, int[] checkedArray) {

                if (getAdapterPosition() == -1) {
                    return;
                }
                Log.i("ITEMSELECTED",""+pos);
                String chValue = checkedArray[pos] == -1 ? FormConstants.EMPTY_STRING : ((CheckBox) itemView.findViewById(pos)).getText().toString();
                if(checkedArray[pos] == 1)
                    checked.add(chValue);
                else
                    checked.remove(chValue);
                DataValueHashMap.put(
                        jsonModelList.get(getAdapterPosition()).getId(),  listToString(checked));

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
}
