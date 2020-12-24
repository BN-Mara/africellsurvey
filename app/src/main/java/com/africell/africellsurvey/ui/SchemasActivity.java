package com.africell.africellsurvey.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.africell.africellsurvey.databinding.ActivityMainBinding;
import com.africell.africellsurvey.databinding.ActivitySchemasBinding;
import com.africell.africellsurvey.helper.CommonUtils;

public class SchemasActivity extends AppCompatActivity {
ActivitySchemasBinding binding;
private static final String DATA_JSON_PATH = "test.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_schemas);
        binding = ActivitySchemasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showSchema();
    }

    private void showSchema() {
        String json =  CommonUtils.loadJSONFromAsset(getApplicationContext(), DATA_JSON_PATH);
        binding.textView.setText(json);
    }

}