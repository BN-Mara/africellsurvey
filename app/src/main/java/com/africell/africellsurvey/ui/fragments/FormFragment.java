package com.africell.africellsurvey.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.databinding.FragmentFormBinding;
import com.africell.africellsurvey.helper.DateConverter;
import com.africell.africellsurvey.helper.FileManip;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.ui.MainActivity;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;


import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import dagger.hilt.android.AndroidEntryPoint;

import static androidx.core.content.FileProvider.getUriForFile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class FormFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentFormBinding binding;
    private SurveyFormViewModel viewModel;

    public FormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormFragment newInstance(String param1, String param2) {
        FormFragment fragment = new FormFragment();
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
        //return inflater.inflate(R.layout.fragment_form, container, false);
        binding = FragmentFormBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyFormViewModel.class);

        getActivity().setTitle("Form detail");
        //getActivity().findViewById(R.id.frameLayout).setBackgroundColor(500158);
        ColorDrawable cd = new ColorDrawable(getResources().getColor(R.color.bggray));
        getActivity().getWindow().setBackgroundDrawable(cd);
        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                FileManip fileManip = new FileManip(getActivity().getApplication());
                SurveyForm sf = viewModel.getCurrentForm();
                String fname = sf.getTitle()+""+sf.getVersion()+DateConverter.getNow()+".txt";


                    String json = viewModel.loadJSONFromFile(sf.getSchema_path(),'R');

                    if(json != null && !json.equalsIgnoreCase("[]")) {
                        Log.i("JSONNOTNULL",json);

                        try {
                            JSONArray ja = new JSONArray(json);
                            if(ja.length() > 0) {

                                JsonNode jsonTree = new ObjectMapper().readTree(new File(getContext().getFilesDir() + "/data/" + sf.getSchema_path()));
                                CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();
                                JsonNode firstObject = jsonTree.elements().next();
                                firstObject.fieldNames().forEachRemaining(fieldName -> {
                                    csvSchemaBuilder.addColumn(fieldName);
                                });
                                File dir = new File(getContext().getExternalFilesDir(null) + "/data/");
                                if (!dir.exists())
                                    dir.mkdir();
                                //File fjson = new File(getContext().getExternalFilesDir(null) + "/data/"+fname+".json");
                                //fileManip.createExternalStoragePrivateFile(json,".json",'R');
                                CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();
                                CsvMapper csvMapper = new CsvMapper();
                                csvMapper.writerFor(JsonNode.class)
                                        .with(csvSchema)
                                        .writeValue(new File(getContext().getExternalFilesDir(null) + "/data/"+fname), jsonTree);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //create file
                        //fileManip.createExternalStoragePrivateFile(ja.toString(), fname, 'R');
                        //viewModel.saveSharedFile(json,fname);

                        File file = fileManip.getExternalFile("/data/"+fname);
                        //Uri.parse(file.getAbsolutePath());
                        if(file != null) {
                            Uri contentUri = getUriForFile(getContext(), "com.africell.africellsurvey.fileprovider", file);
                            Log.i("URI",contentUri.toString());

                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                            shareIntent.setType("text/plain");
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            shareIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                        }else
                            Toast.makeText(getContext(),"No file to share",Toast.LENGTH_LONG).show();
                    }else
                        Toast.makeText(getContext(),"No file to share",Toast.LENGTH_LONG).show();

            }
        });


        displayDetails();

    }
    public void displayInfo(){
        int[] dataCount = viewModel.getDataCount();

        binding.totalLocal.setText(""+dataCount[0]);
        binding.totalRemote.setText(""+dataCount[1]);
    }

    public void displayDetails(){
        displayInfo();
        binding.fdate.setText(viewModel.getCurrentForm().getDownloadDate());
        binding.descr.setText("Survey: "+viewModel.getCurrentForm().getSurveyTitle()+"\n\n"+viewModel.getCurrentForm().getDescription());
        binding.ftitleVersion.setText(viewModel.getCurrentForm().getTitle()+" "+viewModel.getCurrentForm().getVersion());
        //binding.buttonBk.setVisibility(View.INVISIBLE);
        if(viewModel.getCurrentForm().getIsDownloaded() == 0){
            binding.buton.setVisibility(View.INVISIBLE);
        }else{
            binding.buton.setVisibility(View.VISIBLE);
        }
        binding.buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"go to form",Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getContext(), FormActivity.class);
                //intent.putExtra("path",viewModel.getCurrentForm().getSchema_path());
                //startActivity(intent);
                binding.progressBarCyclic.setVisibility(View.VISIBLE);
                binding.buton.setText("...Loading");
                binding.buton.setEnabled(false);
                ((MainActivity) requireActivity()).replaceFragment(FillupFragment.class);
            }
        });
        /*binding.buttonBk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) requireActivity()).replaceFragment(FormsFragment.class);
            }
        });
        /*requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity) requireActivity()).replaceFragment(FormsFragment.class);
            }
        });*/



    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        displayInfo();
    }
}