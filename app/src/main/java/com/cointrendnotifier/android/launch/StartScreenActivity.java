package com.cointrendnotifier.android.launch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.Api;
import com.cointrendnotifier.android.ui.login.MainActivity;
import com.cointrendnotifier.android.ui.trends.TrendsActivity;

public class StartScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(3000);
                        Intent intent;
                        if (Api.isLoggedIn()) {
                            intent = new Intent(StartScreenActivity.this, TrendsActivity.class);
                        } else {
                            intent = new Intent(StartScreenActivity.this, MainActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

