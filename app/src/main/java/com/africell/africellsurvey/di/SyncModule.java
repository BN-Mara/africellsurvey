package com.africell.africellsurvey.di;

import com.africell.africellsurvey.db.SurveyFormDB;
import com.africell.africellsurvey.db.SurveyFormDao;
import com.africell.africellsurvey.sync.SyncAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class SyncModule {

}
