package com.cointrendnotifier.android.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapText;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.UnsuccessfulHttpRequestException;
import com.cointrendnotifier.android.api.Users;
import com.cointrendnotifier.android.ui.trends.TrendsActivity;
import org.json.JSONException;
import java.io.IOException;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.AwesomeTextView;

public class MainActivity extends AppCompatActivity {
    private BootstrapEditText emailEditText, passwordEditText;
    private BootstrapButton signinButton, signupButton;
    private AwesomeTextView errorTextView;
    private String email, password;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

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

        signupButton = (BootstrapButton) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignupActivity.class);
                MainActivity.this.startActivity(i);
            }
        });

        signinButton = (BootstrapButton) findViewById(R.id.signin);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinButton.setEnabled(false);
                signupButton.setEnabled(false);
                login(email, password, context);
            }
        });

        errorTextView = (AwesomeTextView) findViewById(R.id.login_error);
    }

    public void login(final String email, final String password, final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users.login(email, password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, TrendsActivity.class);
                            MainActivity.this.startActivity(i);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                } catch (UnsuccessfulHttpRequestException e) {
                    e.printStackTrace();

                    //ERROR - NOT WORKING FOLLOWING CODE
                    errorTextView.setBootstrapText(new BootstrapText.Builder(
                            errorTextView.getContext())
                            .addText((CharSequence) e.getMessage())
                            .build());

                    signinButton.setEnabled(true);
                    signupButton.setEnabled(true);
                }

            }
        });
        thread.start();

    }

}
