package com.africell.africellsurvey.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.africell.africellsurvey.model.SurveyForm;

import java.util.List;

@Dao
public interface SurveyFormDao {
    @Insert
    void insertForm(SurveyForm form);
    @Query("DELETE FROM form_table WHERE id = :id")
    void deleteForm(String id);
    @Query("DELETE FROM form_table")
    void deleteAll();
    @Query("SELECT * FROM form_table")
    LiveData<List<SurveyForm>> getAllForm();

    @Query("SELECT * FROM form_table")
    List<SurveyForm> getLocalForms();


    @Update
    void updateForm(SurveyForm form);
}
