package com.example.profileswithapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitApi {


    @POST("Personal_Inf")
    Call<Profile> createPost(@Body Profile profileModel);

    @PUT("Personal_Inf")
    Call<Profile> updateData(@Body Profile profileModel);

    @DELETE("api/Values/")
    Call<Profile> deleteData(@Body Profile profileModel);
}
