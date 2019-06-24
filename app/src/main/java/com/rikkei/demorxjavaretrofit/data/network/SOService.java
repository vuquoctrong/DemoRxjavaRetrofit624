package com.rikkei.demorxjavaretrofit.data.network;

import com.rikkei.demorxjavaretrofit.data.network.model.SOAnswersResponse;

import io.reactivex.Observable;
import io.reactivex.Single;

import retrofit2.http.GET;

public interface SOService {
    @GET("repositories?q=mvp+language:java")
    Single<SOAnswersResponse> fetchAllNotes();

}
