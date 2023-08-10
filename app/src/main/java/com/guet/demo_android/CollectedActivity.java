package com.guet.demo_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.guet.demo_android.databinding.ActivityCollectedBinding;

public class CollectedActivity extends AppCompatActivity {

    ActivityCollectedBinding binding;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCollectedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
    }

    public String getId() {
        return id;
    }
}