package com.example.android.meat_timealpha10.RestService;

import com.example.android.meat_timealpha10.Models.RegisterModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by faby on 24/06/17.
 */

public interface RestService {

  @POST("/auth/register")
  Call<String> register(@Body RegisterModel register);

  @FormUrlEncoded
  @POST("/auth/password-reset")
  Call<String> resetPassword(@Field("email") String email);

}