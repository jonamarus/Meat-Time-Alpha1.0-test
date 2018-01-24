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

public class RegisterFragment extends DialogFragment implements Validator.ValidationListener {
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

  public RegisterFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    restService = RestClient.getClient().create(RestService.class);
    context = getActivity().getApplicationContext();

    validator = new Validator(this);
    validator.setValidationListener(this);

    setStyle(DialogFragment.STYLE_NORMAL, R.style.cust_dialog);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_register, container);
    ButterKnife.bind(this, view);

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

    // set this instance as callback for editor action
    getDialog().getWindow().setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    getDialog().setTitle(getString(R.string.register));

    return view;
  }

  @OnClick(R.id.send)
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

          dismiss();
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
