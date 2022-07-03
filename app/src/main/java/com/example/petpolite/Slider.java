package com.example.petpolite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.petpolite.databinding.ActivitySliderBinding;

import java.util.ArrayList;
import java.util.List;

public class Slider extends AppCompatActivity {
ActivitySliderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Full Screen
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        changeStatusBar();
        List<SlideModel> slideModels=new ArrayList<>();
                slideModels.add(new SlideModel(R.drawable.community_post_slid,ScaleTypes.CENTER_INSIDE));
                slideModels.add(new SlideModel(R.drawable.profiles_slide,ScaleTypes.CENTER_INSIDE));
                slideModels.add(new SlideModel(R.drawable.notification_slide,ScaleTypes.CENTER_INSIDE));
                slideModels.add(new SlideModel(R.drawable.categories_slide,ScaleTypes.CENTER_INSIDE));

                binding.imageSlider.setImageList(slideModels);

                SharedPreferences.Editor editor = getSharedPreferences("AppState", MODE_PRIVATE).edit();
                editor.putBoolean("isFirstTime",true);
                editor.apply();
                binding.skip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(Slider.this,Sign_up.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
    void changeStatusBar(){
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.myBlue));
    }

}