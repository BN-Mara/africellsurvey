package com.africell.africellsurvey.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.africell.africellsurvey.repository.Repository;

import javax.inject.Inject;

import dagger.hilt.EntryPoint;
import dagger.hilt.android.AndroidEntryPoint;

//@AndroidEntryPoint
public class SyncService extends Service {
    // Storage for an instance of the sync adapter

    private static SyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    //@Inject
    //public Repository repository;
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
        synchronized (sSyncAdapterLock) {
            
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
