package com.example.yp01;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @GET("dishes.php")
    Call<List<Dishes>> getDishes();

    @GET("login.php")
    Call<List<Users>> getUsers();

    @GET("category.php")
    Call<List<Category>> getCategorys();

    @FormUrlEncoded
    @POST("regin.php")
    Call<Users> regin(
            @Field("login") String name,
            @Field("password") String password,
            @Field("email") String email,
            @Field("telephone") String telephone
    );
}