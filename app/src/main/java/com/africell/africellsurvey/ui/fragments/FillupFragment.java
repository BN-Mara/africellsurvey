package com.africell.africellsurvey.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.databinding.ActivityFormBinding;
import com.africell.africellsurvey.databinding.FragmentFillupBinding;
import com.africell.africellsurvey.databinding.FragmentFormBinding;
import com.africell.africellsurvey.helper.CommonUtils;
import com.africell.africellsurvey.helper.ImageConverter;
import com.africell.africellsurvey.helper.ImagePicker;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shamweel.jsontoforms.adapters.FormAdapter;
import com.shamweel.jsontoforms.interfaces.JsonToFormClickListener;
import com.shamweel.jsontoforms.models.JSONModel;
import com.shamweel.jsontoforms.sigleton.DataValueHashMap;
import com.shamweel.jsontoforms.validate.CheckFieldValidations;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FillupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class FillupFragment extends Fragment implements JsonToFormClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SurveyFormViewModel viewModel;
    private FragmentFillupBinding binding;
    TextView txtJSON;
    //RecyclerView recyclerView;
    FormAdapter mAdapter;
    List<JSONModel> jsonModelList = new ArrayList<>();
    private  String DATA_JSON_PATH;
    private int imagePosition;
    private static final int PICK_IMAGE_ID = 234;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FillupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FillupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FillupFragment newInstance(String param1, String param2) {
        FillupFragment fragment = new FillupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_fillup, container, false);
        binding = FragmentFillupBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyFormViewModel.class);
        getActivity().setTitle(viewModel.getCurrentForm().getTitle());
        DataValueHashMap.init();
        initRecyclerView();
        fetchData();
    }
    private void fetchData(){
        //Intent intent = getIntent();
        DATA_JSON_PATH = viewModel.getCurrentForm().getSchema_path();
        String json =  CommonUtils.loadJSONFromAsset(getContext(), DATA_JSON_PATH);

        List<JSONModel> jsonModelList1 = new ArrayList<>();
        jsonModelList1 = new Gson().fromJson(json, new TypeToken<List<JSONModel>>(){}.getType());
        jsonModelList.addAll(jsonModelList1);
        mAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        mAdapter = new FormAdapter(jsonModelList, getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerview2.setLayoutManager(layoutManager);
        binding.recyclerview2.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview2.setAdapter(mAdapter);
    }

    @Override
    public void onAddAgainButtonClick(int position) {
        //Toast.makeText(getContext(), "Add again button click", Toast.LENGTH_SHORT).show();
        imagePosition = position;
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(requireActivity());
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

    }

    @Override
    public void onSubmitButtonClick() {
        if (!CheckFieldValidations.isFieldsValidated(binding.recyclerview2, jsonModelList)){
            Toast.makeText(getContext(), "Validation Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        //Combined Data
        JSONObject jsonObject = new JSONObject(DataValueHashMap.dataValueHashMap);
        Log.d("onSubmitButtonClick: ", jsonObject.toString());


        //If single value required for corresponding _id's
       /* for (Map.Entry<String, String> hashMap : DataValueHashMap.dataValueHashMap.entrySet()){
            String key = hashMap.getKey(); //  _id of the JSONOModel provided
            String value = hashMap.getValue(); //value entered for the corresponding _id
            Log.d(key, value);

        }*/
        Toast.makeText(getContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
        try {
            viewModel.saveFormData(viewModel.getCurrentForm(),jsonObject.toString());
            //fetchData();
            DataValueHashMap.init();
            initRecyclerView();
            //fetchData();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //if(data != null)

        if (requestCode == PICK_IMAGE_ID && resultCode == Activity.RESULT_OK) {

            Bitmap bitmap = ImagePicker.getImageFromResult(requireActivity(), resultCode, data);
            // TODO use bitmap
            //Toast.makeText(this,bitmap.toString(),Toast.LENGTH_LONG).show();
            String base64String = ImageConverter.convert(bitmap);
            //Toast.makeText(requireActivity(), base64String, Toast.LENGTH_LONG).show();
            DataValueHashMap.put(jsonModelList.get(imagePosition).getId(), base64String);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}