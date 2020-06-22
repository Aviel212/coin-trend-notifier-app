package com.cointrendnotifier.android.ui.accountsettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.UnsuccessfulHttpRequestException;
import com.cointrendnotifier.android.api.Users;
import com.cointrendnotifier.android.api.dtos.RegisteredUserDto;
import com.cointrendnotifier.android.ui.about.AboutActivity;
import com.cointrendnotifier.android.ui.login.LoginActivity;
import com.cointrendnotifier.android.ui.password.PasswordActivity;
import com.cointrendnotifier.android.ui.preferences.PreferencesActivity;
import com.cointrendnotifier.android.ui.trends.TrendsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AccountSettingsActivity extends AppCompatActivity {
    private AwesomeTextView errorTextView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.about_from_menu:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.preferences_from_menu:
                startActivity(new Intent(this, PreferencesActivity.class));
                return true;
            case R.id.trends_from_menu:
                startActivity(new Intent(this, TrendsActivity.class));
                return true;
            case R.id.logout_from_menu:
                Users.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        errorTextView = (AwesomeTextView) findViewById(R.id.settingsError);
        insert_user_info();


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
                        if (!email.equals("") && !Username.equals("") && alertLimit >= 0 && !edit3.getText().toString().equals(""))
                            Account_Settings_Change(email, Username, alertLimit);
                        else {
                            if (email.equals(""))
                                printStringError("Email Should be Insert");
                            if (Username.equals(""))
                                printStringError("Username Should be Insert");
                            if (alertLimit < 0)
                                printStringError("Invalid Alert Limit");
                            findViewById(R.id.updateBtn).setEnabled(true);
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
                        insert_user_info();
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

    public void insert_user_info() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final RegisteredUserDto userInfo = Users.getUser();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EditText edit1 = (EditText) findViewById(R.id.email);
                            EditText edit2 = (EditText) findViewById(R.id.Username);
                            EditText edit3 = (EditText) findViewById(R.id.AlertLimit);
                            edit1.setText(userInfo.getEmail());
                            edit2.setText(userInfo.getUsername());
                            edit3.setText(Integer.toString(userInfo.getAlertLimit()));
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
                        printStringError(responseBody.getString("error"));
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
