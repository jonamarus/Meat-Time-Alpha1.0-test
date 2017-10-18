package com.example.android.meat_timealpha10.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by faby on 18/10/17.
 */

public class TokenHelper {

  public static void setToken(String token, Context context) {
    SharedPreferences.Editor editor = context.getSharedPreferences("MeatTime", 0).edit();
    editor.putString("token", token);
    editor.commit();
  }

  public static String getToken(Context context) {
    SharedPreferences settings = context.getSharedPreferences("MeatTime", 0);
    String token = settings.getString("token", "");
    return token;
  }

}
