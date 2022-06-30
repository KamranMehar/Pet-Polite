package com.example.petpolite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.petpolite.Classes.User;
import com.example.petpolite.Utilities.ClickedCallback;
import com.example.petpolite.Utilities.DialogShow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView login;
    EditText emailEdt,nameEdt,setPassEDt,confirmPassEDt;
    Button signupBtn;
    String name,email,password;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        changeStatusBar();
        login=findViewById(R.id.LoginTxt);
        emailEdt=findViewById(R.id.emailEDtSignup);
        nameEdt=findViewById(R.id.nameEDt);
        setPassEDt=findViewById(R.id.setPassSignUpEDt);
        confirmPassEDt=findViewById(R.id.confirmPassEDt);
        signupBtn=findViewById(R.id.signuuBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Sign_up.this,Login_Activity.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(view -> {

            if (!(isNetworkConnected())){
                DialogShow.showCustomDialogNoInternet("Check Your Internet Connection",
                        "No Internet", Sign_up.this, new ClickedCallback() {
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
            String setPass,confirmPass;
            email=emailEdt.getText().toString().trim();
            name=nameEdt.getText().toString().trim();
            confirmPass=confirmPassEDt.getText().toString().trim();
            setPass=setPassEDt.getText().toString().trim();
            name=nameEdt.getText().toString().trim();
            if (name.isEmpty()){
                nameEdt.setError("Name Should Not be Empty");
                return;
            }
            else {
                name=nameEdt.getText().toString().trim();
            }
            if (email.isEmpty()){
                emailEdt.setError("Email Should Not Empty");
                return;
            }
            if (!(email.contains("@gmail.com"))){
                emailEdt.setError("Invalid Email Address must contain @gmail.com");
                return;
            }
            else {
                email=emailEdt.getText().toString().trim();
            }
            if (setPass.isEmpty()){
                setPassEDt.setError("Should Not Empty");
                return;
            }
            else {
                setPass=setPassEDt.getText().toString();
            }
            if (setPass.length()<8){
                setPassEDt.setError("Password must be greater than 8 characters");
                return;
            }
            else {
                setPass= setPassEDt.getText().toString();
            } if (setPass.length()<8){
                setPassEDt.setError("Password must be greater than 8 characters");
                return;
            }
            else {
                setPass= setPassEDt.getText().toString();
            }
            if (confirmPass.isEmpty()){
                confirmPassEDt.setError("Should Not Empty ");
                return;
            }
            else {
                confirmPass=confirmPassEDt.getText().toString();
            }
            if (!(confirmPass.equals(setPass))){
                setPassEDt.setError("Password Not Match");
            }
            else {
                password=setPassEDt.getText().toString().trim();
            }
            nameEdt.getText().clear();
            emailEdt.getText().clear();
            setPassEDt.getText().clear();
            confirmPassEDt.getText().clear();

            user=new User();
            user.setEmail_user(email);
            user.setName_user(name);
            user.setPassword_user(setPass);
            createUser(email,password,user);
        });
    }

    private void createUser(String email,String password,User user) {
        try {
            mAuth=FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if(task.isSuccessful()){
                            FirebaseUser u=FirebaseAuth.getInstance().getCurrentUser();
                            String id=u.getUid();

                            user.setUserID(id);

                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference node = database.getReference("/data/Users/"+user.getUserID());
                            node.setValue(user);
                            Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                            //saving app current state
                            saveAppState(task.isSuccessful(),false);
                            Intent intent=new Intent(Sign_up.this,Create_Pet_Profile.class);
                            startActivity(intent);
                            finish();
                        }else{
                            DialogShow.showCustomDialogNoInternet(
                                    task.getException().getMessage(), "Error: \n", Sign_up.this, new ClickedCallback() {
                                        @Override
                                        public void YesClicked() {
                                        }
                                        @Override
                                        public void NoClicked() {
                                        }
                                    });
                        }
                    });
        }catch (Exception e){
            DialogShow.showCustomDialogNoInternet("Signup Error \n" + e.getMessage(), "Signup Error", Sign_up.this, new ClickedCallback() {
                @Override
                public void YesClicked() {

                }

                @Override
                public void NoClicked() {

                }

            });
        }


    }

    private void saveAppState(boolean isSignup,boolean isProfileCreated) {
        SharedPreferences.Editor editor = getSharedPreferences("AppState", MODE_PRIVATE).edit();
        editor.putBoolean("isSignUp",isSignup);
        editor.putBoolean("isProfileCreated",isProfileCreated);
        editor.apply();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;

    }


    void changeStatusBar(){
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.myBlue));
    }

}