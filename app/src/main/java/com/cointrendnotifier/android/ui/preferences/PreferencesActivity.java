package com.cointrendnotifier.android.ui.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.Preferences;
import com.cointrendnotifier.android.api.Users;
import com.cointrendnotifier.android.api.dtos.SetPreferenceDto;
import com.cointrendnotifier.android.ui.about.AboutActivity;
import com.cointrendnotifier.android.ui.accountsettings.AccountSettingsActivity;
import com.cointrendnotifier.android.ui.login.LoginActivity;
import com.cointrendnotifier.android.ui.trends.TrendsActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {
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
            case R.id.trends_from_menu:
                startActivity(new Intent(this, TrendsActivity.class));
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
        setContentView(R.layout.activity_preferences);
        BootstrapButton addPreference = (BootstrapButton) findViewById(R.id.add_preference);
        addPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PreferencesActivity.this, AddPreferenceActivity.class);
                PreferencesActivity.this.startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    private LinearLayout createStaticPreference(final SetPreferenceDto preference) {
        LinearLayout.LayoutParams wrapContent1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        wrapContent1.weight = 1;
        LinearLayout.LayoutParams wrapContent3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        wrapContent3.weight = 3;

        // container
        LinearLayout preferenceView = new LinearLayout(this);
        preferenceView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // delete
        BootstrapButton delete = new BootstrapButton(this);
        delete.setLayoutParams(wrapContent1);
        delete.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
        delete.setBootstrapSize(DefaultBootstrapSize.LG);
        delete.setButtonMode(ButtonMode.REGULAR);
        delete.setFontAwesomeIcon(FontAwesome.FA_TRASH);
        delete.setRounded(true);
        delete.setShowOutline(true);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Preferences.deletePreference(preference.getBaseAssetName(), preference.getQuoteAssetName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        render();
                    }
                }).start();
            }
        });

        // assets
        BootstrapLabel assets = new BootstrapLabel(this);
        assets.setLayoutParams(wrapContent3);
        assets.setText(String.format("%s/%s", preference.getBaseAssetName(), preference.getQuoteAssetName()));
        assets.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        assets.setRounded(true);

        // probability
        BootstrapLabel probability = new BootstrapLabel(this);
        probability.setLayoutParams(wrapContent3);
        probability.setText(String.format("%d%%", Math.round(preference.getProbability() * 100)));
        probability.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        probability.setRounded(true);

        preferenceView.addView(delete);
        preferenceView.addView(assets);
        preferenceView.addView(probability);

        return preferenceView;
    }

    private void render() {
        final LinearLayout preferences = (LinearLayout) findViewById(R.id.preferences);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<SetPreferenceDto> setPreferenceDtos = Preferences.getPreferences();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            preferences.removeAllViews();
                            for (SetPreferenceDto preference : setPreferenceDtos) {
                                preferences.addView(createStaticPreference(preference));
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
