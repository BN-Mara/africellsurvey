package com.africell.africellsurvey.ui.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.adapters.SurveyFormAdapter;
import com.africell.africellsurvey.databinding.FragmentFormsBinding;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.ui.MainActivity;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class FormsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FormsFragment";
    private FragmentFormsBinding binding;
    private SurveyFormViewModel viewModel;
    private SurveyFormAdapter adapter;
    private ArrayList<SurveyForm> formList;
    private boolean isDataLoading;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FormsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FormsFragment newInstance(String param1, String param2) {
        FormsFragment fragment = new FormsFragment();
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
        //return inflater.inflate(R.layout.fragment_forms, container, false);
        binding = FragmentFormsBinding.inflate(inflater,container,false);
        getActivity().setTitle("Survey  forms");
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyFormViewModel.class);
        initRecyclerView();
        observeData();
        setUpItemTouchHelper();
        viewModel.getForms();


    }

   /* @Override
    public void onResume() {
        super.onResume();

    }*/

    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int swipedFormPosition = viewHolder.getAdapterPosition();
                SurveyForm form = adapter.getFormAt(swipedFormPosition);

                viewModel.setCurrentForm(form);
                adapter.notifyDataSetChanged();
                ((MainActivity) requireActivity()).replaceFragment(FormFragment.class);

                // Toast.makeText(getContext(),"downloading schema...",Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.formRV);

    }

    public void observeData(){

        adapter.updateList(viewModel.getLocalList());
        viewModel.getFormList().observe(getViewLifecycleOwner(),new Observer<ArrayList<SurveyForm>>() {
            @Override
            public void onChanged(ArrayList<SurveyForm> surveyForms) {
                adapter.updateList(surveyForms);

            }
        });
        //adapter.notifyDataSetChanged();
    }

   /* @Override
    public void onStart() {
        super.onStart();
        initRecyclerView();
        observeData();
        setUpItemTouchHelper();
        viewModel.getForms();

    }*/

   /* @Override
    public void onDestroy() {

        super.onDestroy();
    }*/

    private void initRecyclerView(){
        binding.formRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SurveyFormAdapter(getContext(), new SurveyFormAdapter.MyOnclikListner() {
            @Override
            public void setOnclickLister(SurveyForm sf, int position) {
                //sf.setIsDownloaded(1);
                viewModel.insertForm(sf);
            }

            @Override
            public void deleteOnClick(SurveyForm sf, int position) {
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Confirm deletion")
                        .setMessage("Are you sure to delete this?")
                        .setPositiveButton("Yes, Delete", (dialog, which) -> viewModel.deleteForm(sf.getId()))
                        .setNegativeButton("No, Cancel", (dialog, which) -> {

                        }).show();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void itemOnclick(SurveyForm sf, int position) {
                viewModel.setCurrentForm(sf);
                adapter.notifyDataSetChanged();
                ((MainActivity) requireActivity()).replaceFragment(FormFragment.class);
            }
        },formList);
        viewModel.getForms();
        binding.formRV.setAdapter(adapter);
    }

}