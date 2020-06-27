package com.cointrendnotifier.android.ui.trends;

import android.content.Intent;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
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

        /*
        final TableLayout trendsTableBody = (TableLayout) findViewById(R.id.trends_table_body);

        BootstrapButton moreButton = new BootstrapButton(this);
        TableRow.LayoutParams moreButton_lp = new TableRow.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        trendsTableBody.addView(eventView);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        render();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private TableRow createStaticEvent(final EventDto event) {
        // Row container
        TableRow eventView = new TableRow(TrendsActivity.this);
        eventView.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        eventView.setWeightSum(100);

        // RowIndex container
        LinearLayout rowIndexContainer = new LinearLayout(TrendsActivity.this);
        TableRow.LayoutParams rowIndexContainer_lp = new TableRow.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        rowIndexContainer_lp.weight = 5;
        rowIndexContainer.setLayoutParams(rowIndexContainer_lp);
        rowIndexContainer.setBackgroundResource(R.drawable.border);

        // Row Index
        AwesomeTextView rowIndex = new AwesomeTextView(TrendsActivity.this);
        LinearLayout.LayoutParams rowIndex_lp = new LinearLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, 85);
        rowIndex.setLayoutParams(rowIndex_lp);
        rowIndex.setGravity(Gravity.CENTER_HORIZONTAL);
        rowIndex.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        rowIndex.setMarkdownText("1"); // TODO - needs to be dynamically
        rowIndex.setTextSize(R.dimen.bootstrap_h4_text_size);
        rowIndex.setMinWidth(70);
        rowIndex.setMaxHeight(100);

        rowIndexContainer.addView(rowIndex);

        // ExpectedTo container
        LinearLayout expectedToContainer = new LinearLayout(TrendsActivity.this);
        TableRow.LayoutParams wrapContent1 = new TableRow.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        wrapContent1.weight = 30f;
        expectedToContainer.setLayoutParams(wrapContent1);

        // ExpectedTo
        AwesomeTextView expectedTo = new AwesomeTextView(TrendsActivity.this);
        LinearLayout.LayoutParams expectedTo_lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 85);
        expectedTo.setLayoutParams(expectedTo_lp);
        expectedTo.setRotation(45f); // TODO - needs to be dynamically
        expectedTo.setText("");
        expectedTo.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        expectedTo.setFontAwesomeIcon(FontAwesome.FA_ARROW_UP);
        expectedTo.setMinWidth(423);
        expectedToContainer.addView(expectedTo);

        // Asset container
        LinearLayout assetContainer = new LinearLayout(TrendsActivity.this);
        TableRow.LayoutParams assetContainer_lp = new TableRow.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        assetContainer_lp.weight = 25;
        assetContainer.setLayoutParams(assetContainer_lp);
        assetContainer.setBackgroundResource(R.drawable.border);

        // Asset
        AwesomeTextView asset = new AwesomeTextView(TrendsActivity.this);
        LinearLayout.LayoutParams asset_lp = new TableRow.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 85);
        asset.setLayoutParams(asset_lp);
        asset.setText("BTC/USDT"); // TODO - needs to be dynamically
        asset.setTextSize(R.dimen.bootstrap_h4_text_size);
        asset.setGravity(Gravity.CENTER_HORIZONTAL);
        asset.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        assetContainer.addView(asset);

        // probability container
        LinearLayout probabilityContainer = new LinearLayout(TrendsActivity.this);
        TableRow.LayoutParams probabilityContainer_lp = new TableRow.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        assetContainer_lp.weight = 26;
        assetContainer.setLayoutParams(assetContainer_lp);
        assetContainer.setBackgroundResource(R.drawable.border);

        // probability
        TextView probability = new TextView(TrendsActivity.this);
        LinearLayout.LayoutParams probability_lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 85);
        probability.setLayoutParams(probability_lp);
        probability.setText("10%"); // TODO - needs to be dynamically
        probability.setTextSize(R.dimen.bootstrap_h4_text_size);
        probability.setGravity(Gravity.CENTER_HORIZONTAL);
        probability.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        probability.setMinWidth(367);

        probabilityContainer.addView(probability);

        // moreButton container
        LinearLayout moreButtonContainer = new LinearLayout(TrendsActivity.this);
        TableRow.LayoutParams wrapContent4 = new TableRow.LayoutParams(
                0, TableRow.LayoutParams.WRAP_CONTENT);
        moreButtonContainer.setLayoutParams(wrapContent4);
        wrapContent4.weight = 14f;
        // moreButton


        BootstrapButton moreButton = new BootstrapButton(TrendsActivity.this);
        LinearLayout.LayoutParams moreButton_lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 85);
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

        moreButtonContainer.addView(moreButton);

        eventView.addView(rowIndexContainer);
        eventView.addView(expectedToContainer);
        eventView.addView(assetContainer);
        eventView.addView(probabilityContainer);
        eventView.addView(moreButtonContainer);

        return eventView;
    }

    private void render() {
        final TableLayout trendsTableBody = (TableLayout) findViewById(R.id.trends_table_body);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<EventDto> eventsDtos = Events.getEvents(15);
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
