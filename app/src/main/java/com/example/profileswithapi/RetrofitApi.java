package com.example.profileswithapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitApi {

    @POST("users")
    Call<Profile> createPost(@Body Profile profileModel);
}
