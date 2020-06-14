package com.cointrendnotifier.android.ui.preferences;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.Preferences;
import com.cointrendnotifier.android.api.dtos.SetPreferenceDto;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class AddPreferenceActivity extends AppCompatActivity {
    private String baseAssetChoice;
    private String quoteAssetChoice;
    private String percentageChoice;
    private List<SetPreferenceDto> currentPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preference);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    currentPreferences = Preferences.getPreferences();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        baseAssetChoice = "BTC";
        quoteAssetChoice = "BTC";
        percentageChoice = "10%";

        final Spinner baseAsset = (Spinner) findViewById(R.id.base_asset);
        ArrayAdapter<CharSequence> baseAssetAdapter = ArrayAdapter.createFromResource(this,
                R.array.coins_array, android.R.layout.simple_spinner_item);
        baseAssetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        baseAsset.setAdapter(baseAssetAdapter);
        baseAsset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                baseAssetChoice = (String) parent.getItemAtPosition(position);
                validate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner quoteAsset = (Spinner) findViewById(R.id.quote_asset);
        ArrayAdapter<CharSequence> quoteAssetAdapter = ArrayAdapter.createFromResource(this,
                R.array.coins_array, android.R.layout.simple_spinner_item);
        quoteAssetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quoteAsset.setAdapter(quoteAssetAdapter);
        quoteAsset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quoteAssetChoice = (String) parent.getItemAtPosition(position);
                validate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner percentage = (Spinner) findViewById(R.id.add_preference_percentage);
        ArrayAdapter<CharSequence> percentageAdapter = ArrayAdapter.createFromResource(this,
                R.array.percentage_array, android.R.layout.simple_spinner_item);
        percentageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        percentage.setAdapter(percentageAdapter);
        percentage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                percentageChoice = (String) parent.getItemAtPosition(position);
                validate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        BootstrapButton add = (BootstrapButton) findViewById(R.id.add_preference);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    private void validate() {
        AwesomeTextView error = (AwesomeTextView) findViewById(R.id.add_preference_error);
        error.setBootstrapText(new BootstrapText.Builder(error.getContext()).addText(" ").build());
        BootstrapButton add = (BootstrapButton) findViewById(R.id.add_preference);
        add.setEnabled(true);

        if (baseAssetChoice.equals(quoteAssetChoice)) {
            error.setBootstrapText(new BootstrapText.Builder(error.getContext())
                    .addText("Base asset can not be the same as quote asset").build());
            add.setEnabled(false);
            return;
        }

        for (SetPreferenceDto setPreferenceDto :
                currentPreferences) {
            if (setPreferenceDto.getBaseAssetName().equals(baseAssetChoice) &&
                    setPreferenceDto.getQuoteAssetName().equals(quoteAssetChoice)) {
                error.setBootstrapText(new BootstrapText.Builder(error.getContext())
                        .addText("This preference already exists with percentage of "
                                + Math.round(setPreferenceDto.getProbability() * 100) + "%")
                        .build());
                add.setEnabled(false);
                return;
            }
        }
    }

    private void save() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Preferences.setPreference(baseAssetChoice, quoteAssetChoice, getPercentage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private double getPercentage() {
        return Integer.parseInt(percentageChoice.substring(0, 2)) / 100.0;
    }
}
