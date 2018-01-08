package com.example.android.meat_timealpha10.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.meat_timealpha10.Fragments.PasswordRecoveryFragment;
import com.example.android.meat_timealpha10.Models.TokenModel;
import com.example.android.meat_timealpha10.R;
import com.example.android.meat_timealpha10.RestService.RestClient;
import com.example.android.meat_timealpha10.RestService.RestService;
import com.example.android.meat_timealpha10.helpers.TokenHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends FragmentActivity implements Validator.ValidationListener{
  @BindView(R.id.pwrecovery)
  public Button pwrecovery;

  @BindView(R.id.login_button)
  public LoginButton fbLoginBtn;

  @BindView(R.id.signup)
  public Button signup;

  @BindView(R.id.login_email)
  @Email
  public EditText email;

  @BindView(R.id.login_password)
  @Password()
  public EditText password;

  public RestService restService;
  public Context context;
  public Validator validator;
  public CallbackManager callbackManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //set up notitle
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    //set up full screen
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.activity_login);

    context = getApplicationContext();

    ButterKnife.bind(this);
    restService = RestClient.getClient().create(RestService.class);

    validator = new Validator(this);
    validator.setValidationListener(this);

    callbackManager = CallbackManager.Factory.create();

    fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        facebookLogin(loginResult.getAccessToken().toString());
        Log.d("FACEBOOK", loginResult.getAccessToken().getToken());
      }

      @Override
      public void onCancel() {

      }

      @Override
      public void onError(FacebookException exception) {

      }
    });
  }

  public void facebookLogin(String accessToken){
    Call<TokenModel> call = restService.facebookLogin(accessToken);

    call.enqueue(new Callback<TokenModel>() {
      @Override
      public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
        if (response.isSuccessful()) {
          TokenHelper.setToken(response.body().getToken(), context);
          Log.d("TOKEN", response.body().getToken());
          Intent intent = new Intent(context, MainActivity.class);
          startActivity(intent);
        }else
          Log.d("FAILURE", "Failed to login");
      }

      @Override
      public void onFailure(Call<TokenModel> call, Throwable t) {
        Log.d("CallBack", " Throwable is " + t);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    callbackManager.onActivityResult(requestCode, resultCode, data);
  }

  @OnClick(R.id.pwrecovery)
  public void startPwRecovery(View view) {
    Log.d("LoginActivity", "Password Recovery");

    FragmentManager manager = getFragmentManager();
    Fragment frag = manager.findFragmentByTag("fragment_password_recovery");
    if (frag != null) {
      manager.beginTransaction().remove(frag).commit();
    }

    PasswordRecoveryFragment passwordRecoveryDialog = new PasswordRecoveryFragment();
    passwordRecoveryDialog.show(manager, "fragment_password_recovery");
  }

  @OnClick(R.id.signup)
  public void startSignUp(View view) {
    Intent intent = new Intent(context, RegisterActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.sign_in)
  public void submitLogin(){
    validator.validate();
  }

  public void login(){
    Log.d("LOG IN", "Logging in ");
    Call<TokenModel> call = restService.login(email.getText().toString(), password.getText().toString());
    call.enqueue(new Callback<TokenModel>() {
      @Override
      public void onResponse(Call<TokenModel> call, Response<TokenModel> response) {
        if (response.isSuccessful()) {
          TokenHelper.setToken(response.body().getToken(), context);
          Log.d("TOKEN", response.body().getToken());
          Intent intent = new Intent(context, MainActivity.class);
          startActivity(intent);
        }else
          Log.d("FAILURE", "Failed to login");
      }

      @Override
      public void onFailure(Call<TokenModel> call, Throwable t) {
        Log.d("CallBack", " Throwable is " + t);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_login, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onValidationSucceeded() {
    login();
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
