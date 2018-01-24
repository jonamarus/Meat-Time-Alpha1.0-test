package com.example.android.meat_timealpha10.Fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
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
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordResetFragment extends DialogFragment implements Validator.ValidationListener {
  public RestService restService;
  public Context context;
  public Validator validator;

  @NotEmpty
  @Password(min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
  @BindView(R.id.pwreset_password)
  public EditText password;

  @NotEmpty
  @ConfirmPassword
  @BindView(R.id.pwreset_password_repeat)
  public EditText passwordRepeat;

  @NotEmpty
  @BindView(R.id.pwreset_token)
  public EditText resetToken;

  public PasswordResetFragment() {
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
    View view = inflater.inflate(R.layout.fragment_password_reset, container);

    ButterKnife.bind(this, view);

    passwordRepeat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
    resetPassword();
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

  @OnClick(R.id.send)
  public void submitForm(){
    validator.validate();
  }

  public void resetPassword() {

  }
}
