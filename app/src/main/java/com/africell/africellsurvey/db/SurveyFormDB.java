package com.africell.africellsurvey.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.africell.africellsurvey.model.SurveyForm;

@Database(entities = {SurveyForm.class}, version = 3,exportSchema = false )
public abstract class SurveyFormDB extends RoomDatabase {
    public abstract SurveyFormDao surveyFormDao();
}
