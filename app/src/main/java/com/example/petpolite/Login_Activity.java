package com.example.petpolite;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petpolite.Utilities.ClickedCallback;
import com.example.petpolite.Utilities.DialogShow;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {
    EditText emailEdt, passwordEdt;
    Button loginBtn;
    String email, password;
    FirebaseAuth mAuth;
    TextView gotoSignupBtn, loginAsGuestUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBar();
        loginBtn = findViewById(R.id.LoginBtn);
        emailEdt = findViewById(R.id.emailEDtLogin);
        passwordEdt = findViewById(R.id.passwordLoginEDt);
        gotoSignupBtn = findViewById(R.id.gotoSignupBtn);
        loginAsGuestUser = findViewById(R.id.loginAsGuestUser);
        loginAsGuestUser.setOnClickListener(v -> {
            saveAppState(false, false, true);
            Intent intent=new Intent(Login_Activity.this,MainActivity.class);
            startActivity(intent);
        });
        loginBtn.setOnClickListener(view -> {
            if (!(isNetworkConnected())) {
                DialogShow.showCustomDialogLogout("CHeck Your Internet Connection !", "No Internet", Login_Activity.this, new ClickedCallback() {
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

            email = emailEdt.getText().toString();
            password = passwordEdt.getText().toString();

            if (email.isEmpty()) {
                emailEdt.setError("Email should not Empty !");
                return;
            } else {
                email = emailEdt.getText().toString();
            }
            if (!(email.contains("@gmail.com"))) {
                emailEdt.setError("Invalid Email !!");
                return;
            } else {
                email = emailEdt.getText().toString();
            }
            if (password.isEmpty()) {
                passwordEdt.setError("Password Should Not Empty");
            } else {
                password = passwordEdt.getText().toString();
            }
            signInUSer(email, password);
        });

        gotoSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_Activity.this, Sign_up.class);
                startActivity(intent);
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void signInUSer(String email, String password) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                        saveAppState(task.isSuccessful(), true, false);
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        DialogShow.showCustomDialogNoInternet("Error :\n" + task.getException().getMessage(),
                                "Error To Login"
                                , Login_Activity.this, new ClickedCallback() {
                                    @Override
                                    public void YesClicked() {
                                    }

                                    @Override
                                    public void NoClicked() {
                                    }
                                });
                    }
                });
        loginAsGuestUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void saveAppState(boolean isSignup, boolean isProfileCreated, boolean isGuest) {
        SharedPreferences.Editor editor = getSharedPreferences("AppState", MODE_PRIVATE).edit();
        editor.putBoolean("isSignUp", isSignup);
        editor.putBoolean("isProfileCreated", isProfileCreated);
        editor.putBoolean("isGuest", isGuest);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatusBar() {
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.myBlue));
    }
}