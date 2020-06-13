
package com.cointrendnotifier.android.ui.accountsettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.UnsuccessfulHttpRequestException;
import com.cointrendnotifier.android.api.Users;
import com.cointrendnotifier.android.ui.password.PasswordActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AccountSettingsActivity extends AppCompatActivity {
    private AwesomeTextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        errorTextView = (AwesomeTextView) findViewById(R.id.settingsError);
        findViewById(R.id.updateBtn).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.updateBtn).setEnabled(false);
                        EditText edit1 = (EditText) findViewById(R.id.email);
                        EditText edit2 = (EditText) findViewById(R.id.Username);
                        EditText edit3 = (EditText) findViewById(R.id.AlertLimit);
                        String email = edit1.getText().toString();
                        String Username = edit2.getText().toString();
                        int alertLimit = Integer.parseInt(edit3.getText().toString());
                        if (email != "" && Username != "" && alertLimit >= 0)
                            Account_Settings_Change(email, Username, alertLimit);
                        else {
                            if (email == "")
                                printStringError("Email Should be Inserted");
                            if (Username == "")
                                printStringError("Username Should be Inserted");
                            if (alertLimit < 0)
                                printStringError("Invalid Alert Limit");
                        }
                    }
                });

        findViewById(R.id.BackToPasswordBtn).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(AccountSettingsActivity.this, PasswordActivity.class);
                                AccountSettingsActivity.this.startActivity(i);
                            }
                        });
                    }
                });
        findViewById(R.id.resetBtn).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.email).setContentDescription("example@domain.com");
                        findViewById(R.id.Username).setContentDescription("");
                        findViewById(R.id.AlertLimit).setContentDescription("0");
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
            }
        });
    }

    public void Account_Settings_Change(final String email, final String Username, final int alertLimit) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users.updateUser(email, Username, alertLimit);
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
                        printStringError(responseBody.getString("error"));
                    } catch (IOException | JSONException ex) {
                        ex.printStackTrace();
                        printExceptionError(ex);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.updateBtn).setEnabled(true);
                    }
                });
            }
        });
        thread.start();
    }
}
