package com.africell.africellsurvey.ui.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
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
import com.africell.africellsurvey.helper.DateConverter;
import com.africell.africellsurvey.helper.ImageConverter;
import com.africell.africellsurvey.helper.ImagePicker;
import com.africell.africellsurvey.helper.LocationFinder;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

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
    private String DATA_JSON_PATH;
    private int imagePosition;
    private static final int PICK_IMAGE_ID = 234;
    private static final int REQUEST_LOCATION = 123;
    double longitude =  0.0, latitude  = 0.0;
    String startTime, endTime;
    private int itemPosition = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    double longitudeX = 0.0, latitudeX = 0.0;


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
        binding = FragmentFillupBinding.inflate(inflater, container, false);
        setLocation();
        mFusedLocationClient = getFusedLocationProviderClient(getActivity());

        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SurveyFormViewModel.class);
        getActivity().setTitle(viewModel.getCurrentForm().getTitle());
        mFusedLocationClient = getFusedLocationProviderClient(getActivity());
        DataValueHashMap.init();
        initRecyclerView();
        fetchData();
    }

    private void fetchData() {
        //Intent intent = getIntent();
        DATA_JSON_PATH = viewModel.getCurrentForm().getSchema_path();
        String json = CommonUtils.loadJSONFromAsset(getContext(), DATA_JSON_PATH);

        List<JSONModel> jsonModelList1 = new ArrayList<>();
        jsonModelList1 = new Gson().fromJson(json, new TypeToken<List<JSONModel>>() {
        }.getType());
        jsonModelList.addAll(jsonModelList1);
        for( JSONModel jsonModel : jsonModelList1){
            DataValueHashMap.put(jsonModel.getId(),"");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initRecyclerView() {
        itemPosition = 0;
        startTime = new DateConverter().getTimeStamp();
        setLocation();
        mAdapter = new FormAdapter(jsonModelList, getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);


        binding.recyclerview2.setLayoutManager(layoutManager);
        binding.recyclerview2.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerview2.setAdapter(mAdapter);
       /* binding.backBtn.setVisibility(View.INVISIBLE);
        binding.submitBtn.setVisibility(View.GONE);
        if(mAdapter.getItemCount()  > 1){
            binding.nextBtn.setVisibility(View.VISIBLE);
        }
        binding.recyclerview2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!binding.recyclerview2.canScrollHorizontally(1))
                {
                    binding.nextBtn.setVisibility(View.GONE);
                    binding.submitBtn.setVisibility(View.VISIBLE);

                }else{
                    binding.nextBtn.setVisibility(View.VISIBLE);
                    binding.submitBtn.setVisibility(View.GONE);
                }
                if(!binding.recyclerview2.canScrollHorizontally(-1)){

                    binding.backBtn.setVisibility(View.INVISIBLE);
                }else{
                    binding.backBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.recyclerview2.post(new Runnable() {
                    @Override
                    public void run() {
                        itemPosition += 1;
                        if(itemPosition < mAdapter.getItemCount()) {
                            binding.recyclerview2.smoothScrollToPosition(itemPosition);
                        }else{

                        }
                    }
                });

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPosition -= 1;
                if(itemPosition >= 0) {
                    binding.recyclerview2.smoothScrollToPosition(itemPosition);
                }else{

                }
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });*/

    }

    @Override
    public void onAddAgainButtonClick(int position) {
        //Toast.makeText(getContext(), "Add again button click", Toast.LENGTH_SHORT).show();
        imagePosition = position;
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(requireActivity());
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

    }

    public void setLocation() {
        //googleMap.setMyLocationEnabled(true);
        //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(getContext().LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,

                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, REQUEST_LOCATION);
            // return;
        } else {

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

            mFusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,null).addOnSuccessListener(getActivity(),location -> {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    //txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("MapDemoActivity", "Error trying to get last GPS location");
                    e.printStackTrace();
                }
            });
            LocationFinder finder;
            finder = new LocationFinder(getContext());
            if (finder.canGetLocation()) {
                latitudeX = finder.getLatitude();
                longitudeX = finder.getLongitude();
                //Toast.makeText(getContext(), "lat-lng " + latitude + " — " + longitude, Toast.LENGTH_LONG).show();
            } else {
                finder.showSettingsAlert();
            }
           /*mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                if (location != null) {
                    latitude = location.getLatitude();
                     longitude = location.getLongitude();
                    //txtLocation.setText(String.format(Locale.US, "%s -- %s", wayLatitude, wayLongitude));
                }
            }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Log.i("MapDemoActivity", "Error trying to get last GPS location");
                   e.printStackTrace();
               }
           });*/


        }
    }

    @Override
    public void onSubmitButtonClick() {
        handleSubmit();
    }

    public void handleSubmit() {
        if (!CheckFieldValidations.isFieldsValidated(binding.recyclerview2, jsonModelList)) {
            Toast.makeText(getContext(), "Validation Failed", Toast.LENGTH_SHORT).show();
            return;
        }

        //longitude = locationManager.getL;
        //DataValueHashMap.put("latitude",latitude);
        //DataValueHashMap.put()

        endTime = new DateConverter().getTimeStamp();

        //Combined Data

        JSONObject jsonObject = new JSONObject(DataValueHashMap.dataValueHashMap);
        try {
            if(longitude == 0.0 && latitude == 0.0){
                longitude = longitudeX;
                latitude  = latitudeX;
            }
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("startTime", startTime);
            jsonObject.put("endTime", endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("onSubmitButtonClick: ", jsonObject.toString());


        //If single value required for corresponding _id's
       /* for (Map.Entry<String, String> hashMap : DataValueHashMap.dataValueHashMap.entrySet()){
            String key = hashMap.getKey(); //  _id of the JSONOModel provided
            String value = hashMap.getValue(); //value entered for the corresponding _id
            Log.d(key, value);

        }*/
        Log.i("LatitudeX",Double.toString(latitudeX));
        Log.i("LongitudeX",Double.toString(longitudeX));
        Log.i("Latitude",Double.toString(latitude));
        Log.i("longitude",Double.toString(longitude));

        Toast.makeText(getContext(), "success!", Toast.LENGTH_SHORT).show();
        try {
            viewModel.saveFormData(viewModel.getCurrentForm(), jsonObject.toString());
            //fetchData();
            //setLocation();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                DataValueHashMap.init();
                initRecyclerView();
                //fetchData();

            }
        }
    }


}