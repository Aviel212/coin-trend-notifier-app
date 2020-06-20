
package com.cointrendnotifier.android.ui.password;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.UnsuccessfulHttpRequestException;
import com.cointrendnotifier.android.api.Users;
import com.cointrendnotifier.android.ui.accountsettings.AccountSettingsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PasswordActivity extends AppCompatActivity {
    private AwesomeTextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        errorTextView = (AwesomeTextView) findViewById(R.id.passwordError);
        findViewById(R.id.changePasswordBtn).setOnClickListener
                (new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.changePasswordBtn).setEnabled(false);
                        EditText edit1 = (EditText) findViewById(R.id.oldPassword);
                        EditText edit2 = (EditText) findViewById(R.id.newPassword);
                        EditText edit3 = (EditText) findViewById(R.id.confirmedPassword);
                        String oldPassword = edit1.getText().toString();
                        String newPassword = edit2.getText().toString();
                        String confirmedPassword = edit3.getText().toString();


                        if (confirmedPassword.equals(newPassword) && newPassword.length() >= 6 && !confirmedPassword.equals("") && !oldPassword.equals(""))
                            password_change(oldPassword, newPassword);
                        else {
                            if (oldPassword.equals(""))
                                printStringError("Please insert old password");
                            if (newPassword.equals(""))
                                printStringError("Please insert new password");
                            if (newPassword.length() < 6)
                                printStringError("Password must be over 6 characters");
                            if (confirmedPassword.equals(""))
                                printStringError("Please insert confirmed password");
                            if (!confirmedPassword.equals(newPassword))
                                printStringError("Passwords are not the same");
                            findViewById(R.id.changePasswordBtn).setEnabled(true);
                        }
                    }
                });

        findViewById(R.id.BackToSettingsBtn).setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(PasswordActivity.this, AccountSettingsActivity.class);
                                PasswordActivity.this.startActivity(i);
                            }
                        });
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

    public void password_change(final String oldPassword, final String newPassword) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Users.changePassword(oldPassword, newPassword);
                    Toast.makeText(PasswordActivity.this, "Password changed!", Toast.LENGTH_LONG).show();
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
                        findViewById(R.id.changePasswordBtn).setEnabled(true);
                    }
                });
            }
        });
        thread.start();
    }
}

