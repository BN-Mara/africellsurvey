package com.africell.africellsurvey.repository;

import androidx.lifecycle.LiveData;

import com.africell.africellsurvey.db.SurveyFormDao;
import com.africell.africellsurvey.model.FormData;
import com.africell.africellsurvey.model.FormDataResponse;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.model.SurveyFormResponse;
import com.africell.africellsurvey.network.ApiService;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class Repository {
    private SurveyFormDao surveyFormDao;
    private ApiService apiService;
    @Inject
    public Repository(SurveyFormDao surveyFormDao, ApiService apiService){
        this.surveyFormDao = surveyFormDao;
        this.apiService = apiService;

    }
    public Observable<SurveyFormResponse> getForms(){
        return apiService.getForms();

    }
    public Observable<FormDataResponse> sendData(FormData formData){
        return apiService.sendFormData(formData);
    }
    public List<SurveyForm> getSurveyForms(){
       return surveyFormDao.getLocalForms();
    }

    public LiveData<List<SurveyForm>> getLocalForms(){
        return surveyFormDao.getAllForm();
    }
    public void deleteForm(String id){
        surveyFormDao.deleteForm(id);
    }
    public void deleteAll(){
        surveyFormDao.deleteAll();
    }
    public void updateForm(SurveyForm form){
        surveyFormDao.updateForm(form);
    }
    public void insertForm(SurveyForm form){
        surveyFormDao.insertForm(form);
    }
}
