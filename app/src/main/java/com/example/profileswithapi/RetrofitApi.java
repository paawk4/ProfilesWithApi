package com.example.profileswithapi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface RetrofitApi {


    @POST("Personal_Inf")
    Call<Profile> createPost(@Body Profile profileModel);

    @PUT("Personal_Inf/")
    Call<Profile> updateData(@Query("id") int Id, @Body Profile profileModel);

    @DELETE("Personal_Inf/")
    Call<Profile> deleteData(@Query("id") int Id);
}
