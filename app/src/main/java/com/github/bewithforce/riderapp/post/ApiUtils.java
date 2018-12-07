package com.github.bewithforce.riderapp.post;

import retrofit2.Retrofit;

public class ApiUtils {

    public static final String BASE_URL = "eatmealby.com//system//courier//";

    public static CallAPI getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(CallAPI.class);
    }
}
