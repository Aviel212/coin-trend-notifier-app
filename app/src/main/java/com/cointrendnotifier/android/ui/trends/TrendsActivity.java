package com.cointrendnotifier.android.ui.trends;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.ButtonMode;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.cointrendnotifier.android.R;
import com.cointrendnotifier.android.api.Events;
import com.cointrendnotifier.android.api.Users;
import com.cointrendnotifier.android.api.dtos.EventDto;
import com.cointrendnotifier.android.ui.about.AboutActivity;
import com.cointrendnotifier.android.ui.accountsettings.AccountSettingsActivity;
import com.cointrendnotifier.android.ui.login.LoginActivity;
import com.cointrendnotifier.android.ui.preferences.PreferencesActivity;
import com.cointrendnotifier.android.ui.trend.TrendActivity;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

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

        final TableLayout trendsTableBody = (TableLayout) findViewById(R.id.trends_table_body);

        BootstrapButton moreButton = new BootstrapButton(this);
        LinearLayout.LayoutParams moreButton_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        moreButton.setLayoutParams(moreButton_lp);
        moreButton.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
        moreButton.setBootstrapSize(DefaultBootstrapSize.SM);
        moreButton.setButtonMode(ButtonMode.REGULAR);
        moreButton.setRounded(true);
        moreButton.setShowOutline(false);
        moreButton.setText("More");
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrendsActivity.this, TrendActivity.class);
                intent.putExtra("id", "5e9b4f6a48cdb0002ab99f78"); // TODO - needs to be dynamically
                TrendsActivity.this.startActivity(intent);
            }
        });

        TableRow eventView = new TableRow(TrendsActivity.this);
        eventView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        eventView.setWeightSum(100);

        eventView.addView(moreButton);
        trendsTableBody.addView(eventView);

/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<EventDto> eventDtos = Events.getEvents(1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            trendsTableBody.addView(createStaticEvent(eventDtos.get(0)));

                            BootstrapButton moreButton = new BootstrapButton(TrendsActivity.this);
                            LinearLayout.LayoutParams moreButton_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            moreButton.setLayoutParams(moreButton_lp);
                            moreButton.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                            moreButton.setBootstrapSize(DefaultBootstrapSize.SM);
                            moreButton.setButtonMode(ButtonMode.REGULAR);
                            moreButton.setRounded(true);
                            moreButton.setShowOutline(false);
                            moreButton.setText("More");
                            moreButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(TrendsActivity.this, TrendActivity.class);
                                    intent.putExtra("id", "5e9b4f6a48cdb0002ab99f78"); // TODO - needs to be dynamically
                                    TrendsActivity.this.startActivity(intent);
                                }
                            });

                            trendsTableBody.addView(moreButton);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    private TableRow createStaticEvent(final EventDto event) {
        // create LayoutParams for image and button's Linear Layouts (elements goes from 0 to 4)
        LinearLayout.LayoutParams wrapContent1 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        wrapContent1.weight = 30f;

        LinearLayout.LayoutParams wrapContent4 = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        wrapContent4.weight = 14f;

        // container
        TableRow eventView = new TableRow(TrendsActivity.this);
        eventView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        eventView.setWeightSum(100);

        // Row Index
        TextView rowIndex = new TextView(TrendsActivity.this);
        LinearLayout.LayoutParams rowIndex_lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 5);
        rowIndex.setLayoutParams(rowIndex_lp);
        rowIndex.setGravity(Gravity.CENTER_HORIZONTAL);
        rowIndex.setBackgroundResource(R.drawable.border);
        rowIndex.setText("1"); // TODO - needs to be dynamically
        rowIndex.setTextSize(R.dimen.bootstrap_h4_text_size);

        // arrow (in LinearLayout - LayoutParams1)
        AwesomeTextView expectedTo = new AwesomeTextView(TrendsActivity.this);
        LinearLayout.LayoutParams expectedTo_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        expectedTo.setLayoutParams(expectedTo_lp);
        expectedTo.setRotation(45f); // TODO - needs to be dynamically
        expectedTo.setText("");
        expectedTo.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        expectedTo.setFontAwesomeIcon(FontAwesome.FA_ARROW_UP);

        LinearLayout expectedToContainer = new LinearLayout(TrendsActivity.this);
        expectedToContainer.setLayoutParams(wrapContent1);
        expectedToContainer.addView(expectedTo);

        // asset
        TextView asset = new TextView(TrendsActivity.this);
        LinearLayout.LayoutParams asset_lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 25f);
        asset.setLayoutParams(asset_lp);
        asset.setBackgroundResource(R.drawable.border);
        asset.setText("BTC/USDT"); // TODO - needs to be dynamically
        asset.setTextSize(R.dimen.bootstrap_h4_text_size);
        asset.setGravity(Gravity.CENTER_HORIZONTAL);

        // probability
        TextView probability = new TextView(TrendsActivity.this);
        LinearLayout.LayoutParams probability_lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 26f);
        probability.setLayoutParams(probability_lp);
        probability.setBackgroundResource(R.drawable.border);
        probability.setText("10%"); // TODO - needs to be dynamically
        probability.setTextSize(R.dimen.bootstrap_h4_text_size);
        probability.setGravity(Gravity.CENTER_HORIZONTAL);

        // moreButton
        BootstrapButton moreButton = new BootstrapButton(TrendsActivity.this);
        LinearLayout.LayoutParams moreButton_lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        moreButton.setLayoutParams(moreButton_lp);
        moreButton.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
        moreButton.setBootstrapSize(DefaultBootstrapSize.SM);
        moreButton.setButtonMode(ButtonMode.REGULAR);
        moreButton.setRounded(true);
        moreButton.setShowOutline(false);
        moreButton.setText("More");
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrendsActivity.this, TrendActivity.class);
                intent.putExtra("id", "5e9b4f6a48cdb0002ab99f78"); // TODO - needs to be dynamically
                TrendsActivity.this.startActivity(intent);
            }
        });

        LinearLayout moreButtonContainer = new LinearLayout(TrendsActivity.this);
        moreButtonContainer.setLayoutParams(wrapContent4);
        moreButtonContainer.addView(moreButton);

        eventView.addView(rowIndex);
        eventView.addView(expectedToContainer);
        eventView.addView(asset);
        eventView.addView(probability);
        eventView.addView(moreButtonContainer);

        return eventView;
    }

    private void render() {
        final TableLayout trendsTableBody = (TableLayout) findViewById(R.id.trends_table_body);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<EventDto> eventsDtos = Events.getEvents(1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // trendsTableBody.removeAllViews();
                            for (EventDto event : eventsDtos) {
                                trendsTableBody.addView(createStaticEvent(event));
                            }
                            Log.d("Yarden", String.valueOf(trendsTableBody.getChildCount()));
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
