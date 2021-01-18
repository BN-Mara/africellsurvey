package com.africell.africellsurvey.ui.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.databinding.FragmentFormBinding;
import com.africell.africellsurvey.ui.MainActivity;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;

import dagger.hilt.android.AndroidEntryPoint;

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
        binding.descr.setText(viewModel.getCurrentForm().getDescription());
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