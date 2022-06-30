package com.example.petpolite;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;

import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.petpolite.Utilities.ClickedCallback;
import com.example.petpolite.Utilities.DialogShow;
import com.example.petpolite.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Splash_Screen extends AppCompatActivity {
ActivitySplashScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        //Full Screen
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.getRoot());
        Handler handler=new Handler();

        if (isNetworkConnected()){
            SharedPreferences prefs = getSharedPreferences("AppState", MODE_PRIVATE);
            boolean isSignup = prefs.getBoolean("isSignUp", false);
            boolean isProfileCreated=prefs.getBoolean("isProfileCreated",false);
            boolean isGuest=prefs.getBoolean("isGuest",false);
       /* handler.postDelayed(()->{*/
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Intent intent;
            if (!isGuest){
                if (user != null) {

                    if (!isSignup){
                        intent = new Intent(Splash_Screen.this, Sign_up.class);
                        startActivity(intent);
                        finish();
                    }else if (isSignup && !isProfileCreated){
                        intent = new Intent(Splash_Screen.this, Create_Pet_Profile.class);
                        startActivity(intent);
                        finish();
                    }else if (isProfileCreated && isSignup){
                        intent = new Intent(Splash_Screen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                else {
                    intent = new Intent(Splash_Screen.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                }
            }else {
                intent = new Intent(Splash_Screen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
       /* },500);*/
    }else {
            DialogShow.showCustomDialogNoInternet("No Internet To run the App", "No Internet", Splash_Screen.this, new ClickedCallback() {
                @Override
                public void YesClicked() {
                    finishAffinity();
                    System.exit(0);
                }

                @Override
                public void NoClicked() {

                }
            });
        }
        }

    private  boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}