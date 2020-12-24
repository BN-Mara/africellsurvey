package com.africell.africellsurvey.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.adapters.SurveyFormAdapter;
import com.africell.africellsurvey.databinding.FragmentFormsBinding;
import com.africell.africellsurvey.databinding.FragmentLoginBinding;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.model.User;
import com.africell.africellsurvey.ui.MainActivity;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SurveyFormViewModel viewModel;
    private SurveyFormAdapter adapter;
    private FragmentLoginBinding binding;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        //return inflater.inflate(R.layout.fragment_login, container, false);
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyFormViewModel.class);
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.btnLogin.setEnabled(false);
                inAnimation = new AlphaAnimation(0f, 1f);
                inAnimation.setDuration(200);
                binding.progressBarHolder.setAnimation(inAnimation);
                binding.progressBarHolder.setVisibility(View.VISIBLE);

                String username = binding.inputEmail.getText().toString();
                String psswd = binding.inputPassword.getText().toString();

                User user = new User(username,psswd);
                boolean ck = viewModel.setLoggedIn(user);
                if(ck){
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    binding.progressBarHolder.setAnimation(outAnimation);
                    binding.progressBarHolder.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(true);
                    ((MainActivity) requireActivity()).replaceFragment(FormsFragment.class);
                }else{
                    outAnimation = new AlphaAnimation(1f, 0f);
                    outAnimation.setDuration(200);
                    binding.progressBarHolder.setAnimation(outAnimation);
                    binding.progressBarHolder.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(true);
                    Toast.makeText(getContext(),"incorrect credential",Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}