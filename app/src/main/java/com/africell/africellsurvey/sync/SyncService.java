package com.africell.africellsurvey.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.africell.africellsurvey.repository.Repository;

import javax.inject.Inject;

import dagger.hilt.EntryPoint;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.internal.migration.InjectedByHilt;

@AndroidEntryPoint
public class SyncService extends Service {
    // Storage for an instance of the sync adapter



    SyncAdapter sSyncAdapter;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        /*synchronized (sSyncAdapterLock) {
            
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);

            }
        }*/
        sSyncAdapter.getContext();


    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
