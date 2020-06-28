package com.cointrendnotifier.android.ui.trends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    private LinearLayout createSpace() {
        LinearLayout space = new LinearLayout(TrendsActivity.this);
        space.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                5
        ));
        space.setOrientation(LinearLayout.HORIZONTAL);
        return space;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private LinearLayout createStaticEvent(final EventDto event, int i) {
        // Table row
        LinearLayout eventView = new LinearLayout(TrendsActivity.this);
        eventView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                85
        ));
        eventView.setOrientation(LinearLayout.HORIZONTAL);
        eventView.setWeightSum(100);
        eventView.setBackgroundResource(R.drawable.border);

        BootstrapLabel rowIndex = new BootstrapLabel(this);
        LinearLayout.LayoutParams rowIndex_lp = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 10f);
        rowIndex.setGravity(Gravity.CENTER_HORIZONTAL);
        rowIndex.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rowIndex.setLayoutParams(rowIndex_lp);
        rowIndex.setText(i + ""); // TODO - needs to be dynamically
        rowIndex.setTextSize(R.dimen.bootstrap_h4_text_size);
        rowIndex.setBootstrapBrand(event.getProbability() > 0 ?
                DefaultBootstrapBrand.SUCCESS :
                DefaultBootstrapBrand.DANGER);

        eventView.addView(rowIndex);

        // ExpectedTo container
        LinearLayout expectedToContainer = new LinearLayout(TrendsActivity.this);
        LinearLayout.LayoutParams wrapContent1 = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT);
        wrapContent1.weight = 25f;
        expectedToContainer.setLayoutParams(wrapContent1);
        expectedToContainer.setBackgroundResource(R.drawable.border);

        // ExpectedTo
        AwesomeTextView expectedTo = new AwesomeTextView(TrendsActivity.this);
        LinearLayout.LayoutParams expectedTo_lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        expectedTo.setLayoutParams(expectedTo_lp);
        double prob = event.getProbability();
        expectedTo.setRotation(prob > 0 ?
                45f : 135f);
        expectedTo.setText("");
        expectedTo.setBootstrapBrand(
                event.getProbability() > 0 ?
                        DefaultBootstrapBrand.SUCCESS : DefaultBootstrapBrand.DANGER);
        expectedTo.setFontAwesomeIcon(FontAwesome.FA_ARROW_UP);

        expectedToContainer.addView(expectedTo);
        eventView.addView(expectedToContainer);

        BootstrapLabel asset = new BootstrapLabel(this);
        //TextView asset = new TextView(TrendsActivity.this);
        LinearLayout.LayoutParams asset_lp = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 25f);
        asset.setLayoutParams(asset_lp);
        asset.setText(String.format("%s/%s", event.getBaseAssetName(), event.getQuoteAssetName()));
        asset.setTextSize(R.dimen.bootstrap_h4_text_size);
        asset.setGravity(Gravity.CENTER_HORIZONTAL);
        asset.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        asset.setBootstrapBrand(event.getProbability() > 0 ?
                DefaultBootstrapBrand.SUCCESS :
                DefaultBootstrapBrand.DANGER);
        eventView.addView(asset);

        BootstrapLabel probability = new BootstrapLabel(TrendsActivity.this);
        LinearLayout.LayoutParams probability_lp = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 26f);
        probability.setLayoutParams(probability_lp);
        probability.setText(Math.round(Math.abs(event.getProbability()) * 100) + "%");
        probability.setTextSize(R.dimen.bootstrap_h4_text_size);
        probability.setGravity(Gravity.CENTER_HORIZONTAL);
        probability.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        probability.setBootstrapBrand(event.getProbability() > 0 ?
                DefaultBootstrapBrand.SUCCESS :
                DefaultBootstrapBrand.DANGER);

        eventView.addView(probability);

        // moreButton container
        LinearLayout moreButtonContainer = new LinearLayout(TrendsActivity.this);
        LinearLayout.LayoutParams wrapContent4 = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 14f);
        moreButtonContainer.setOrientation(LinearLayout.HORIZONTAL);
        moreButtonContainer.setLayoutParams(wrapContent4);

        // moreButton
        BootstrapLabel moreButton = new BootstrapLabel(TrendsActivity.this);
        LinearLayout.LayoutParams moreButton_lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        moreButton.setLayoutParams(moreButton_lp);
        moreButton.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
        moreButton.setRounded(true);
        moreButton.setText("More");
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrendsActivity.this, TrendActivity.class);
                intent.putExtra("id", event.get_id());
                TrendsActivity.this.startActivity(intent);
            }
        });

        moreButtonContainer.addView(moreButton);
        eventView.addView(moreButtonContainer);

        return eventView;
    }

    private void render() {
        final LinearLayout trendsTableBody = (LinearLayout) findViewById(R.id.trends_table_body);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<EventDto> eventsDtos = Events.getEvents(20);
                    runOnUiThread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void run() {
                            trendsTableBody.removeAllViews();
                            int i = 0;
                            for (EventDto event : eventsDtos) {
                                i++;
                                trendsTableBody.addView(createStaticEvent(event, i));
                                trendsTableBody.addView(createSpace());
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