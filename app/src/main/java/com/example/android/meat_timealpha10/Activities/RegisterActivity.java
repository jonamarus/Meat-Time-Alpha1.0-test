package com.example.android.meat_timealpha10.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.meat_timealpha10.Models.RegisterModel;
import com.example.android.meat_timealpha10.Models.User;
import com.example.android.meat_timealpha10.R;
import com.example.android.meat_timealpha10.RestService.RestClient;
import com.example.android.meat_timealpha10.RestService.RestService;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class RegisterActivity extends FragmentActivity implements Validator.ValidationListener{
  public RestService restService;
  public Context context;
  public Validator validator;

  @NotEmpty
  @BindView(R.id.first_name)
  public EditText firstName;

  @NotEmpty
  @BindView(R.id.last_name)
  public EditText lastName;

  @NotEmpty
  @Email
  @BindView(R.id.register_mail)
  public EditText email;

  @NotEmpty
  @Password(scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
  @BindView(R.id.register_pw)
  public EditText password;

  @NotEmpty
  @ConfirmPassword
  @BindView(R.id.register_pw_check)
  public EditText passwordCheck;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //set up notitle
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    //set up full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.fragment_register);

    context = this.getApplicationContext();

    ButterKnife.bind(this);

    passwordCheck.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          submitForm();
          return true;
        }
        return false;
      }
    });

    restService = RestClient.getClient().create(RestService.class);
    validator = new Validator(this);
    validator.setValidationListener(this);
  }


  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

  }

  @OnClick(R.id.register_btn)
  public void submitForm(){
    validator.validate();
  }

  public void register(){
    Log.d("REGISTER", "Register a new user");

    RegisterModel registerModel =
            new RegisterModel(
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    email.getText().toString(),
                    password.getText().toString());

    Call<User> call = restService.register(registerModel);
    call.enqueue(new Callback<User>(){

      @Override
      public void onResponse(Call<User> call, Response<User> response) {
        if (response.isSuccessful()){
          Log.d("REGISTER", "User created successfuly");
          Toast.makeText(context, "Account successfully created",
                  Toast.LENGTH_LONG);
          Intent intent = new Intent(context, LoginActivity.class);
          startActivity(intent);
        }else {
          Log.d("REGISTER", response.message());
        }

        Log.d("CallBack", " response is " + response);
      }

      @Override
      public void onFailure(Call<User> call, Throwable t) {
        Log.d("CallBack", " Throwable is " + t);
      }
    });
  }

  @Override
  public void onValidationSucceeded() {
    register();
  }

  @Override
  public void onValidationFailed(List<ValidationError> errors) {
    for (ValidationError error : errors) {
      View view = error.getView();
      String message = error.getCollatedErrorMessage(context);

      // Display error messages ;)
      if (view instanceof EditText) {
        ((EditText) view).setError(message);
      } else {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
      }
    }
  }
}
