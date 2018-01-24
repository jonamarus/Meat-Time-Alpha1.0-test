package com.example.android.meat_timealpha10.RestService;

import com.example.android.meat_timealpha10.Models.RegisterModel;
import com.example.android.meat_timealpha10.Models.TokenModel;
import com.example.android.meat_timealpha10.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by faby on 24/06/17.
 */

public interface RestService {

  @POST("auth/register")
  Call<User> register(@Body RegisterModel register);

  @FormUrlEncoded
  @POST("auth/password-reset")
  Call<String> resetPassword(@Field("email") String email);

  @FormUrlEncoded
  @POST("auth/login")
  Call<TokenModel> login(@Field("email") String email, @Field("password") String password);

  @GET("auth/facebook")
  Call<TokenModel> facebookLogin(@Field("accessToken") String accessToken);

}