package com.africell.africellsurvey.di;

import android.app.Application;
import android.content.Context;

import com.africell.africellsurvey.helper.SaveSharedPreference;
import com.africell.africellsurvey.network.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {
   // static String ip = SaveSharedPreference.getShared(,"ip");

    @Provides
    @Singleton
    public static ApiService provideApiService(Application context){
        String ip = SaveSharedPreference.getShared(context,"ip");
        return new Retrofit.Builder()
                //10.0.2.2
                //192.168.43.45
                //10.100.26.106
                .baseUrl("http://"+ip+"/api/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(ApiService.class);
    }

}
