package com.example.petpolite.Classes;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User extends Pet_Profile{
    String name_user;
    String email_user;
    String password_user;
    String petProfilesNo;
    String userID;
    public static String getUserNameFromDatabase_="";

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName_user() {
        return name_user;
    }


    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getPassword_user() {
        return password_user;
    }

    public void setPassword_user(String password_user) {
        this.password_user = password_user;
    }


    public String getPetProfilesNo() {
        return petProfilesNo;
    }

    public void setPetProfilesNo(String petProfilesNo) {
        this.petProfilesNo = petProfilesNo;
    }

    public static String getUserNameFromDataBase() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = firebaseUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference node = database.getReference("/data/Users/" + userUid);
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                getUserNameFromDatabase_ = u.getName_user();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return getUserNameFromDatabase_;
    }
}
