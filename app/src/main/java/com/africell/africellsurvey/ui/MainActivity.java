package com.africell.africellsurvey.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

//import android.app.FragmentManager;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.databinding.ActivityMainBinding;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.ui.fragments.FormFragment;
import com.africell.africellsurvey.ui.fragments.FormsFragment;
import com.africell.africellsurvey.ui.fragments.LoginFragment;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;

import java.text.Normalizer;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity" ;
    private ActivityMainBinding binding;
private SurveyFormViewModel viewModel;
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.africell.africellsurvey.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "africell.com";
    // The account name
    public static final String ACCOUNT = "africell";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mAccount = CreateSyncAccount(this);
        // Get the content resolver for your app
       mResolver = getContentResolver();
        Bundle settingBundle=new Bundle();
        settingBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        settingBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED,true);
        ContentResolver.requestSync(mAccount,AUTHORITY,settingBundle);
        ContentResolver.setSyncAutomatically(mAccount,AUTHORITY,true);
        /*
         * Turn on periodic syncing
         */
        ContentResolver.addPeriodicSync(
                mAccount,
                AUTHORITY,
                Bundle.EMPTY,
                30);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(SurveyFormViewModel.class);
        //viewModel.deleteAll();
        if(viewModel.getSessionSatus()){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new FormsFragment()).addToBackStack(null)
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new LoginFragment()).addToBackStack(null)
                    .commit();
        }


    }
    public void replaceFragment(Class fragmentClass){
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        }catch(Exception e){
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).addToBackStack(null)
                .commit();
    }
    /*public void download(SurveyForm surveyForm){
        Toast.makeText(getBaseContext(),surveyForm.getTitle(),Toast.LENGTH_LONG).show();



    }*/

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, viewModel.getFragment().toString(), Toast.LENGTH_LONG).show();

           // FragmentManager fm = getFragmentManager();
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                //viewModel.getForms();
               getSupportFragmentManager().popBackStack();

                    FormsFragment fragment = new FormsFragment();
                viewModel.getForms();
                    //fragment.observeData();







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
}