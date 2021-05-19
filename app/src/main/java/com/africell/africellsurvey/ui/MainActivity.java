package com.africell.africellsurvey.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

//import android.app.FragmentManager;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.databinding.ActivityMainBinding;
import com.africell.africellsurvey.helper.FusedLocationService;
import com.africell.africellsurvey.helper.SaveSharedPreference;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.ui.fragments.FormFragment;
import com.africell.africellsurvey.ui.fragments.FormsFragment;
import com.africell.africellsurvey.ui.fragments.LoginFragment;
import com.africell.africellsurvey.ui.fragments.SettingFragment;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Normalizer;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private SurveyFormViewModel viewModel;
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.africell.africellsurvey.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "africell.com";
    // The account name
    public static final String ACCOUNT = "africell";
    private static final int REQUEST_FILECHOOSER = 2000;
    private String uri ="";
    private Uri uri2;
    // Instance fields
    Account mAccount;
    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    // A content resolver for accessing the provider
    ContentResolver mResolver;
    public ImageButton imgbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(SaveSharedPreference.getShared(this,"ip")  ==  null){
            SaveSharedPreference.addToShared(this,"ip","169.254.140.47:45455");
        }
        mAccount = CreateSyncAccount(this);
        // Get the content resolver for your app
        mResolver = getContentResolver();
        ContentResolver.setMasterSyncAutomatically(true);
        Bundle settingBundle = new Bundle();

        settingBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingBundle);
        ContentResolver.setSyncAutomatically(mAccount, AUTHORITY, true);

        /*
         * Turn on periodic syncing
         */
        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                10);

     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(10, 0).
                    setSyncAdapter(mAccount, AUTHORITY).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(
                    mAccount,
                    AUTHORITY,
                    Bundle.EMPTY,
                    10);
        }*/
        FusedLocationService  fservice = new FusedLocationService(this);
        Log.i("main_lat",""+fservice.getLatitude());
        Log.i("main_lon",""+fservice.getLongitude());
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Toolbar toolbar =  findViewById(R.id.main_toolbar);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);

        viewModel = new ViewModelProvider(this).get(SurveyFormViewModel.class);
        //viewModel.deleteAll();
        if (viewModel.getSessionSatus()) {
            try {
                Fragment formFrag = (Fragment) FormsFragment.class.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, formFrag)
                        .commit();
                //replaceFragment(FormsFragment.class);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new LoginFragment())
                    .commit();
            //replaceFragment(LoginFragment.class);
        }
        imgbtn = binding.toolbarBtn;

        binding.toolbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                replaceFragment(SettingFragment.class);
                binding.toolbarBtn.setVisibility(View.GONE);

            }
        });
        final Intent intent = getIntent();

        final String action = intent.getAction();
        if(Intent.ACTION_VIEW.equals(action)){
            //uri = intent.getStringExtra("URI");
            uri2 = intent.getData();
            uri = uri2.toString();
            //Toast.makeText(this,uri,Toast.LENGTH_LONG).show();
            File file = new File(uri2.getPath());
            viewModel.loadLocalFile(uri2);

            // now you call whatever function your app uses
            // to consume the txt file whose location you now know
        } else {
            Log.d(TAG, "intent was something else: "+action);

        }


    }


    public void replaceFragment(Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
           /* if(fragment instanceof FormsFragment){
                binding.toolbarBtn.setVisibility(View.VISIBLE);
            }else{
                binding.toolbarBtn.setVisibility(View.GONE);
            }*/
        }catch(Exception e){
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack(fragment.getClass().getName())
                .commit();
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, viewModel.getFragment().toString(), Toast.LENGTH_LONG).show();
        //viewModel = new ViewModelProvider(this).get(SurveyFormViewModel.class);
           FragmentManager fm = getSupportFragmentManager();
           binding.toolbarBtn.setVisibility(View.VISIBLE);
        int backEntryCount = getSupportFragmentManager().getBackStackEntryCount();
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                //viewModel.getForms();
                //notify();
               //getSupportFragmentManager().popBackStack();

               /*Fragment fragment = fm.findFragmentByTag(FormsFragment.class.getName());
               if(fragment instanceof FormsFragment){
                   binding.toolbarBtn.setVisibility(View.VISIBLE);
               }*/
               //fragment.onResume();
                //viewModel.getForms();
                    //fragment.observeData();


                getSupportFragmentManager().popBackStackImmediate();



            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");
                super.onBackPressed();
            }

    }

    /**
     * Create a new placeholder account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
           Log.d(TAG, "CreateSyncAccount: ");
            return newAccount;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* if(requestCode == REQUEST_FILECHOOSER){
            if (resultCode != RESULT_OK) {
                return;
            }
            // Import the file
            Uri fileUri = data.getData();
            File loadFile  =  new File(fileUri.getPath());

        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.toolbar_button){
            replaceFragment(SettingFragment.class);
        }
        return super.onOptionsItemSelected(item);
    }
}