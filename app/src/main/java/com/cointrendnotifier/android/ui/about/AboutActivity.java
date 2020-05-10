package com.cointrendnotifier.android.ui.about;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cointrendnotifier.android.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView view = (TextView) findViewById(R.id.about_content);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
