package com.cointrendnotifier.android.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.UnsuccessfulHttpRequestException;
import com.cointrendnotifier.android.api.Users;
import com.cointrendnotifier.android.ui.trends.TrendsActivity;

import org.json.JSONException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText email_EditText, password_EditText;
    private Button signin_Button;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email_EditText = (EditText) findViewById(R.id.email);
        email_EditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                email = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        password_EditText = (EditText) findViewById(R.id.password);
        password_EditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                password = s.toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        signin_Button = (Button) findViewById(R.id.signin);
        signin_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signin_Button.setEnabled(false);
                    login(email, password);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    signin_Button.setEnabled(true);
                }
            }
        });
    }

    public void login(final String email, final String password) throws InterruptedException {
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
                }

            }
        });
        thread.start();

    }

}
