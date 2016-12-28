package com.example.android.xyztouristattractions.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.xyztouristattractions.R;

/**
 * Created by Dawid on 2016-12-28.
 */
public class MapAllActivity extends AppCompatActivity {
    private static final String EXTRA_ATTRACTION = "attraction";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String attraction = getIntent().getStringExtra(EXTRA_ATTRACTION);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MapAllFragment.createInstance(attraction))
                    .commit();
        }
    }
}
