package com.cointrendnotifier.android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.UnsuccessfulHttpRequestException;
import com.cointrendnotifier.android.api.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SignupActivity extends AppCompatActivity {
    private BootstrapButton signupButton, cancelButton;
    private BootstrapEditText emailEditText, passwordEditText, usernameEditText, alertLimitEditText;
    private AwesomeTextView errorTextView;
    private String email, password, username;
    private Integer alertLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_signup);

        emailEditText = (BootstrapEditText) findViewById(R.id.email);
        emailEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                email = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        passwordEditText = (BootstrapEditText) findViewById(R.id.password);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        usernameEditText = (BootstrapEditText) findViewById(R.id.username);
        usernameEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                username = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        alertLimitEditText = (BootstrapEditText) findViewById(R.id.alert_limit);
        alertLimitEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                alertLimit = Integer.parseInt(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        errorTextView = (AwesomeTextView) findViewById(R.id.login_error);

        cancelButton = (BootstrapButton) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                SignupActivity.this.startActivity(i);
            }
        });

        signupButton = (BootstrapButton) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButton.setEnabled(false);
                signupButton.setEnabled(false);
                signup(email, username, password, alertLimit);
            }
        });
    }

    protected void printStringError(final String e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorTextView.setBootstrapText(new BootstrapText.Builder(
                        errorTextView.getContext())
                        .addText(e)
                        .build());
                cancelButton.setEnabled(true);
                signupButton.setEnabled(true);
            }
        });
    }

    protected void printExceptionError(final Exception e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorTextView.setBootstrapText(new BootstrapText.Builder(
                        errorTextView.getContext())
                        .addText(e.getMessage())
                        .build());
                cancelButton.setEnabled(true);
                signupButton.setEnabled(true);
            }
        });
    }

    public void signup(final String email, final String username, final String password, final Integer alertLimit) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users.signUp(email, username, password, alertLimit);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                            SignupActivity.this.startActivity(i);
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                    printExceptionError(e);
                } catch (IOException e) {
                    e.printStackTrace();
                    printExceptionError(e);
                } catch (UnsuccessfulHttpRequestException e) {
                    e.printStackTrace();
                    try {
                        JSONObject responseBody = new JSONObject(e.getResponse().body().string());
                        // TODO - deal with array of errors
                        printStringError(responseBody.toString());
                    } catch (IOException | JSONException ex) {
                        ex.printStackTrace();
                        printExceptionError(ex);
                    }
                }
            }
        });
        thread.start();
    }
}
