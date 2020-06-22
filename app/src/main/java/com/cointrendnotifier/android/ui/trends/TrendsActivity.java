package com.cointrendnotifier.android.ui.trends;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.Users;
import com.cointrendnotifier.android.ui.about.AboutActivity;
import com.cointrendnotifier.android.ui.accountsettings.AccountSettingsActivity;
import com.cointrendnotifier.android.ui.login.LoginActivity;
import com.cointrendnotifier.android.ui.preferences.PreferencesActivity;

public class TrendsActivity extends AppCompatActivity {

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
            case R.id.account_settings_from_menu:
                startActivity(new Intent(this, AccountSettingsActivity.class));
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
        setContentView(R.layout.activity_trends);
    }
}
