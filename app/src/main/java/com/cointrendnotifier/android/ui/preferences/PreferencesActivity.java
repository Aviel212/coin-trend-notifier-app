package com.cointrendnotifier.android.ui.preferences;

import android.os.Bundle;
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
import com.cointrendnotifier.android.api.dtos.SetPreferenceDto;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.coins_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
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
