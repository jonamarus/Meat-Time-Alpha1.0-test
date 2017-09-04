package com.example.android.meat_timealpha10.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.meat_timealpha10.R;
import com.example.android.meat_timealpha10.RestService.RestClient;
import com.example.android.meat_timealpha10.RestService.RestService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PasswordRecoveryFragment extends DialogFragment implements TextView.OnEditorActionListener{

  @BindView(R.id.pwrecovery_email)
  EditText email;

  public RestService restService;

  public interface UserNameListener {
    void onFinishUserDialog(String user);
  }

  // Empty constructor required for DialogFragment
  public PasswordRecoveryFragment() {}

  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);

    restService = RestClient.getClient().create(RestService.class);

    setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
  }

  @OnClick(R.id.send)
  public void sendEmail(){
    Log.d("PASSWORD_RECOVERY", email.getText().toString());
    restService.resetPassword(email.getText().toString());

    Call<String> call = restService.resetPassword(email.getText().toString());;
    call.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        Log.d("CallBack", " response is " + response);
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        Log.d("CallBack", " Throwable is " +t);
      }
    });

    Log.d("PASSWORD_RECOVERY", "Send email to backend");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_password_recovery, container);

    ButterKnife.bind(this, view);

    // set this instance as callback for editor action
    getDialog().getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    getDialog().setTitle("Password Recovery");

    return view;
  }

  @Override
  public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
    // Return input text to activity
    UserNameListener activity = (UserNameListener) getActivity();
    activity.onFinishUserDialog("FINISH");
    this.dismiss();
    return true;
  }
}