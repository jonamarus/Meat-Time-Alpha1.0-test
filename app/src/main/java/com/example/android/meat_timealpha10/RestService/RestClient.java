package com.example.android.meat_timealpha10.RestService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by faby on 27/06/17.
 */

public class RestClient {

  public static final String BASE_URL = "";
  private static Retrofit retrofit = null;

  public static Retrofit getClient() {
    if (retrofit==null) {
      Gson gson = new GsonBuilder()
              .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
              .create();

      retrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create(gson))
              .build();
    }
    return retrofit;
  }
}
