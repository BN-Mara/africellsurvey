package com.africell.africellsurvey.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.databinding.ActivityFormBinding;
import com.africell.africellsurvey.databinding.ActivityMainBinding;
import com.africell.africellsurvey.helper.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shamweel.jsontoforms.adapters.FormAdapter;
import com.shamweel.jsontoforms.interfaces.JsonToFormClickListener;
import com.shamweel.jsontoforms.models.JSONModel;
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.validate.CheckFieldValidations;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FormActivity extends AppCompatActivity implements JsonToFormClickListener {
    TextView txtJSON;
    //RecyclerView recyclerView;
    FormAdapter mAdapter;
    List<JSONModel> jsonModelList = new ArrayList<>();
    ActivityFormBinding binding;
    private  String DATA_JSON_PATH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_form);
        //recyclerView = binding.recyclerview;
        binding = ActivityFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DataValueHashMap.init();
        initRecyclerView();
        fetchData();
    }
    private void fetchData(){
        Intent intent = getIntent();
        DATA_JSON_PATH = intent.getStringExtra("path");
        String json =  CommonUtils.loadJSONFromAsset(getApplicationContext(), DATA_JSON_PATH);

        List<JSONModel> jsonModelList1 = new ArrayList<>();
        jsonModelList1 = new Gson().fromJson(json, new TypeToken<List<JSONModel>>(){}.getType());
        jsonModelList.addAll(jsonModelList1);
        mAdapter.notifyDataSetChanged();
    }



    private void initRecyclerView() {
        mAdapter = new FormAdapter(jsonModelList, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerview.setLayoutManager(layoutManager);
        binding.recyclerview.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onAddAgainButtonClick(int position) {
        Toast.makeText(this, "Add again button click", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSubmitButtonClick() {
        if (!CheckFieldValidations.isFieldsValidated(binding.recyclerview, jsonModelList)){
            Toast.makeText(this, "Validation Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        //Combined Data
        JSONObject jsonObject = new JSONObject(DataValueHashMap.dataValueHashMap);
        Log.d("onSubmitButtonClick: ", jsonObject.toString());


        //If single value required for corresponding _id's
        for (Map.Entry<String, String> hashMap : DataValueHashMap.dataValueHashMap.entrySet()){
            String key = hashMap.getKey(); //  _id of the JSONOModel provided
            String value = hashMap.getValue(); //value entered for the corresponding _id
            Log.d(key, value);
        }


    }
}