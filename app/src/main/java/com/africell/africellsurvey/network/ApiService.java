package com.africell.africellsurvey.network;

import com.africell.africellsurvey.model.FormData;
import com.africell.africellsurvey.model.FormDataResponse;
import com.africell.africellsurvey.model.SurveyFormResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("form")
    Observable<SurveyFormResponse> getForms();

    @POST("formData")
    Call<FormDataResponse> sendFormData(@Body FormData formData);

}
