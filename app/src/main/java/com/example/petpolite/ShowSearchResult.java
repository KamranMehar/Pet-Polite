package com.example.petpolite;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.petpolite.databinding.ActivityShowSearchResultBinding;


public class ShowSearchResult extends AppCompatActivity {
    ActivityShowSearchResultBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShowSearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeStatusBar();
        binding.breedNameItem.setText(getIntent().getStringExtra("breed"));
        binding.descriptionItem.setText(getIntent().getStringExtra("description"));
        binding.feedingItem.setText(getIntent().getStringExtra("feeding"));
        binding.groomingItem.setText(getIntent().getStringExtra("grooming"));
        binding.handlingItem.setText(getIntent().getStringExtra("handling"));
        binding.sleepNeedItem.setText(getIntent().getStringExtra("sleep"));
        binding.housingItem.setText(getIntent().getStringExtra("house"));
        Glide.with(this).load(getIntent().getStringExtra("photo")).into(binding.petImageItem);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    void changeStatusBar() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.myBlue));
    }
}