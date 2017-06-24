package com.example.android.meat_timealpha10.RestService;

import com.example.android.meat_timealpha10.Models.RegisterModel;

import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by faby on 24/06/17.
 */

public interface RestService {

  @POST("/auth/register")
  void register(@Body RegisterModel register);

}
