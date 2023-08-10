package com.guet.demo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SharedActivity extends AppCompatActivity {
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
    }
    public String getId() {
        return id;
    }
}