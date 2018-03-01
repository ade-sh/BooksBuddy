package com.adesh.projbk;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Registration_activity extends AppCompatActivity {
    public View RegisterFormview, ProgressBarView;
    EditText Username, Email, Password, Phone, ConPass;
    Button btnRegis;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserRegisterTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RegisterFormview = findViewById(R.id.mRegisterForm);
        ProgressBarView = findViewById(R.id.pbRegis);
        Username = (EditText) findViewById(R.id.etReg_name);
        Email = (EditText) findViewById(R.id.etReg_Email);
        Password = (EditText) findViewById(R.id.etReg_Password);
        Phone = (EditText) findViewById(R.id.etReg_Phoneno);
        ConPass = (EditText) findViewById(R.id.etReg_cofrmPass);
        btnRegis = (Button) findViewById(R.id.btn_regUser);
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });

    }

    //For Validation checks
    public void attemptRegistration() {
        if (mAuthTask != null) {
            return;
        }
        //Reset Errors
        Email.setError(null);
        Password.setError(null);
        ConPass.setError(null);
        Phone.setError(null);

        //Values for Registration
        String sUsername = Username.getText().toString();
        String sPassword = Password.getText().toString();
        String sEmail = Email.getText().toString();
        String sPhone = Phone.getText().toString();
        String sCnPass = ConPass.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(sPassword) && !isPasswordValid(sPassword)) {
            Password.setError("Password is too short");
            focusView = Password;
            cancel = true;
        }
        if (!isPasswordmatchConform(sPassword, sCnPass)) {
            ConPass.setError("Password Does not match");
            focusView = ConPass;
            cancel = true;
        }
        if (TextUtils.isEmpty(sPassword)) {
            Password.setError("This field is required");
            focusView = Password;
            cancel = true;
        }
        if (TextUtils.isEmpty(sEmail)) {
            Email.setError("This field is required");
            focusView = Email;
            cancel = true;
        }
        if (TextUtils.isEmpty(sPhone)) {
            Phone.setError("This field is required");
            focusView = Phone;
            cancel = true;
        }
        if (!isEmailValid(sEmail)) {
            Email.setError(getString(R.string.error_invalid_email));
            focusView = Email;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserRegisterTask(sUsername, sEmail, sPassword, sPhone);
            mAuthTask.execute((Void) null);
        }

    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    public boolean isPasswordValid(String ssPass) {
        return ssPass.length() > 2;
    }

    public boolean isPasswordmatchConform(String ssPass, String sscnPass) {
        return sscnPass.matches(ssPass);
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            RegisterFormview.setVisibility(show ? View.GONE : View.VISIBLE);
            RegisterFormview.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    RegisterFormview.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            ProgressBarView.setVisibility(show ? View.VISIBLE : View.GONE);
            ProgressBarView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ProgressBarView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            ProgressBarView.setVisibility(show ? View.VISIBLE : View.GONE);
            RegisterFormview.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class UserRegisterTask extends AsyncTask<Object, Object, String> {
        private final String username, email, password, phone;

        UserRegisterTask(String sUsername, String sEmail, String sPassword, String sPhone) {
            username = sUsername;
            email = sEmail;
            password = sPassword;
            phone = sPhone;
        }

        @Override
        protected String doInBackground(Object... params) {

            OkHttpClient httpClient = new OkHttpClient();
            RequestBody parameter = new FormBody.Builder().add("username", username).add("password", password).add("email", email).add("phone", phone).build();
            okhttp3.Request request = new Request.Builder().url(getString(R.string.httpUrl) + "/register.inc.php").post(parameter).build();
            String MyResult = "";
            try {
                Response response = httpClient.newCall(request).execute();
                MyResult = response.body().string();
                Log.d("okhttp regis", MyResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return MyResult;
        }

        @Override
        protected void onPostExecute(String result) {
            mAuthTask = null;
            showProgress(false);
            if (result.contains("true")) {
                SharedPreferences sp = getSharedPreferences("UserLogin", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("UserName", email);
                editor.putString("Password", password);
                editor.putBoolean("IsLogged", true);
                editor.apply();
                SharedPreferences settingPreference = PreferenceManager.getDefaultSharedPreferences(Registration_activity.this);
                SharedPreferences.Editor stEdit = settingPreference.edit();
                stEdit.putString("display_name", username);
                stEdit.apply();
                finish();
                Intent smp = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(smp);
            } else {
                Log.e("php excep", result);
                Toast.makeText(getApplicationContext(), "cannot register now" + result, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
