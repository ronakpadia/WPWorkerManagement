package com.theinternetcompany.wpworkermanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class WokerDetailActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woker_detail);
    }
}