package com.example.android.meat_timealpha10.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.meat_timealpha10.R;
import com.example.android.meat_timealpha10.RestService.RestClient;
import com.example.android.meat_timealpha10.RestService.RestService;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PasswordRecoveryFragment extends DialogFragment implements Validator.ValidationListener{

  @BindView(R.id.pwrecovery_email)
  @Email
  public EditText email;

  public Context context;
  public RestService restService;
  public Validator validator;

  // Empty constructor required for DialogFragment
  public PasswordRecoveryFragment() {}

  public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);

    restService = RestClient.getClient().create(RestService.class);
    context = getActivity().getApplicationContext();

    validator = new Validator(this);
    validator.setValidationListener(this);

    setStyle(DialogFragment.STYLE_NORMAL, R.style.cust_dialog);
  }

  @OnClick(R.id.send)
  public void submitForm(){
    validator.validate();
  }

  public void sendEmail(){
    Log.d("PASSWORD_RECOVERY", email.getText().toString());

    Call<String> call = restService.resetPassword(email.getText().toString());
    call.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if (response.isSuccessful()){
          Toast.makeText(context, "An email has been sent to this emailadres with a reset token",
                  Toast.LENGTH_LONG);
          PasswordResetFragment nextFrag= new PasswordResetFragment();

          nextFrag.show(getFragmentManager(), "password_reset_fragment");
          dismiss();
        }else {
          email.setError("User with this email not found");
        }
        Log.d("CallBack", " response is " + response);
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        //Toast.makeText(context, "A Network error occured", Toast.LENGTH_LONG).show();
        Log.d("CallBack", " Throwable is " + t);
      }
    });
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_password_recovery, container);

    ButterKnife.bind(this, view);

    email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          submitForm();
          return true;
        }
        return false;
      }
    });

    // set this instance as callback for editor action
    getDialog().getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    getDialog().setTitle(getString(R.string.pwreset));

    return view;
  }

  @Override
  public void onValidationSucceeded() {
    sendEmail();
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