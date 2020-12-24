package com.africell.africellsurvey.di;

import android.app.Application;

import androidx.room.Room;

import com.africell.africellsurvey.db.SurveyFormDB;
import com.africell.africellsurvey.db.SurveyFormDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public static SurveyFormDB provideSurveyFormDB(Application application){
        return Room.databaseBuilder(application, SurveyFormDB.class, "form_database")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

    }
    @Provides
    @Singleton
    public static SurveyFormDao provideSurveyDao(SurveyFormDB surveyFormDB){
        return surveyFormDB.surveyFormDao();
    }
}
