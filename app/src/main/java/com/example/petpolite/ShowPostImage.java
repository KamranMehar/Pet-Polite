package com.example.petpolite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.petpolite.databinding.ActivityShowPostImageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowPostImage extends AppCompatActivity {
    ActivityShowPostImageBinding binding;
    String postId, userID;
    DatabaseReference likeReference = FirebaseDatabase.getInstance().getReference("/data/Likes/");
    boolean isGuest;
    Boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowPostImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        postId = getIntent().getStringExtra("postId");
        userID = getIntent().getStringExtra("userId");
        isGuest = getIntent().getBooleanExtra("isGuest", false);
        binding.backButton.setOnClickListener(v -> {
            finish();
        });
        binding.descriptionPostItem.setText(getIntent().getStringExtra("description"));
        binding.petNamePostItem.setText(getIntent().getStringExtra("petName"));
        Glide.with(ShowPostImage.this).load(getIntent().getStringExtra("photo")).into(binding.postItemImage);
        Glide.with(ShowPostImage.this).load(getIntent().getStringExtra("userDp")).into(binding.postProfilePicItem);
        if (!isGuest) {
            likeReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(postId).hasChild(userID)) {
                        binding.like.setImageResource(R.drawable.like_ic);
                        int count = (int) snapshot.child(postId).getChildrenCount();
                        binding.likeNo.setText(count + " Likes");
                    } else {
                        binding.like.setImageResource(R.drawable.no_like_ic);
                        int count = (int) snapshot.child(postId).getChildrenCount();
                        binding.likeNo.setText(count + " Likes");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            binding.like.setOnClickListener(v -> {
                isLiked = true;
                likeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isLiked) {
                            if (snapshot.child(postId).hasChild(userID)) {
                                likeReference.child(postId).child(userID).removeValue();
                                isLiked = false;
                            } else {
                                likeReference.child(postId).child(userID).setValue(userID);
                                isLiked = false;
                                makeSound(ShowPostImage.this);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            });


        } else {
            binding.like.setImageResource(R.drawable.no_like_ic);
            likeReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = (int) snapshot.child(postId).getChildrenCount();
                    binding.likeNo.setText(count + " Likes");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            binding.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ShowPostImage.this, "Guest User Can't Like post !", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public static void makeSound(Context context) {
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.like_sound);
        mp.start();
    }
}