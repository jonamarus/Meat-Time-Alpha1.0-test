package com.example.android.meat_timealpha10.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.android.meat_timealpha10.Fragments.PasswordRecoveryFragment;
import com.example.android.meat_timealpha10.Fragments.RegisterFragment;
import com.example.android.meat_timealpha10.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends FragmentActivity {
  @BindView(R.id.pwrecovery)
  public Button pwrecovery;

  @BindView(R.id.login_button)
  public LoginButton fbLoginBtn;

  @BindView(R.id.signup)
  public Button signup;

  public Context context;
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

    ButterKnife.bind(this);
    callbackManager = CallbackManager.Factory.create();

    // alertdialoog twee voor registratie
    signup = (Button) findViewById(R.id.signup);

    final View signupview = View.inflate(this, R.layout.signup, null);

    fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      @Override
      public void onSuccess(LoginResult loginResult) {
        //Send to backend
        Log.d("FACEBOOK", loginResult.getAccessToken().getToken());
      }

      @Override
      public void onCancel() {
        // App code
      }

      @Override
      public void onError(FacebookException exception) {

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
    Log.d("TAG", "Sign Up");
    FragmentManager manager = getFragmentManager();

    Fragment frag = manager.findFragmentByTag("fragment_password_recovery");
    if (frag != null) {
      manager.beginTransaction().remove(frag).commit();
    }

    RegisterFragment registerFragment = new RegisterFragment();
    registerFragment.show(manager, "register_fragment");
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
}
