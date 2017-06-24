package com.example.android.meat_timealpha10.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.meat_timealpha10.Fragments.PasswordRecoveryFragment;
import com.example.android.meat_timealpha10.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends Activity implements PasswordRecoveryFragment.UserNameListener {
  @BindView(R.id.pwrecovery)
  Button pwrecovery;

  Button signup;
  Context context;

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

    // alertdialoog twee voor registratie
    signup = (Button) findViewById(R.id.signup);

    final View signupview = View.inflate(this, R.layout.signup, null);

    signup.setOnClickListener(new View.OnClickListener()

    {
      @Override
      public void onClick(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //Eerste knop in de custom Alert
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {

            dialog.dismiss();


          }

        });
// tweede knop in de custom alert
        builder.setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {

            dialog.dismiss();
          }
        });

        builder.setView(signupview);

        AlertDialog dialog = builder.create();
        dialog.show();
      }

    });

  }

  @OnClick(R.id.pwrecovery)
  public void startPwRecovery(View view) {
    Log.d("TAG", "Password Recovery");
    FragmentManager manager = getFragmentManager();
    Fragment frag = manager.findFragmentByTag("fragment_edit_name");
    if (frag != null) {
      manager.beginTransaction().remove(frag).commit();
    }

    PasswordRecoveryFragment editNameDialog = new PasswordRecoveryFragment();
    editNameDialog.show(manager, "fragment_edit_name");
  }

  @OnClick(R.id.signup)
  public void startSignUp(View view) {
    Log.d("TAG", "Sign Up");
    FragmentManager manager = getFragmentManager();
    Fragment frag = manager.findFragmentByTag("fragment_edit_name");
    if (frag != null) {
      manager.beginTransaction().remove(frag).commit();
    }

    PasswordRecoveryFragment editNameDialog = new PasswordRecoveryFragment();
    editNameDialog.show(manager, "fragment_edit_name");
  }

  @Override
  public void onFinishUserDialog(String user) {
    Toast.makeText(this, "Hello, " + user, Toast.LENGTH_SHORT).show();
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
