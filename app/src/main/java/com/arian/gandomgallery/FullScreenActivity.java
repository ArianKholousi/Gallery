package com.arian.gandomgallery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        String arg = getIntent().getStringExtra(MainFragment.EXTRA_IMAGE_URL);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_full_screen, FullScreenFragment.newInstance(arg)).commit();

    }
}
