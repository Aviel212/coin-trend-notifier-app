package com.cointrendnotifier.android.ui.trend;

import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.Events;
import com.cointrendnotifier.android.api.dtos.EventDto;

import org.json.JSONException;

import java.io.IOException;
import java.text.Format;
import java.util.Date;

public class TrendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                String id = intent.getStringExtra("id");
                assert id != null;
                try {
                    final EventDto trend = Events.getById(id);

                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            boolean rise = trend.getProbability() > 0;
                            BootstrapBrand green = DefaultBootstrapBrand.SUCCESS;
                            BootstrapBrand red = DefaultBootstrapBrand.DANGER;

                            final TextView dateView = findViewById(R.id.date);
                            final BootstrapLabel symbolStringView = findViewById(R.id.symbol_string);
                            final BootstrapLabel percentageView = findViewById(R.id.percentage);
                            final TextView trendContentView = findViewById(R.id.trend_content);

                            symbolStringView.setBootstrapBrand(rise ? green : red);
                            percentageView.setBootstrapBrand(rise ? green : red);

                            final String percentage = Math.round(Math.abs(trend.getProbability()) * 100) + "%";
                            final String content = String.format("According to our calculation there is a %s chance that any assets of %s will %s in value."
                                    , percentage, trend.getBaseAssetName(), rise ? "rise" : "drop");

                            Format formatter = new SimpleDateFormat("dd/MM/yyyy");
                            final String date = formatter.format(new Date(trend.getFiredAt()));

                            final String symbolString = String.format("%s/%s", trend.getBaseAssetName(), trend.getQuoteAssetName());

                            dateView.setText(date);
                            symbolStringView.setText(symbolString);
                            percentageView.setText(percentage);
                            trendContentView.setText(content);

                            dateView.setTypeface(null, Typeface.BOLD);
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
