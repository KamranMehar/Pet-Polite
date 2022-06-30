package com.example.petpolite;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.petpolite.databinding.ActivityShowAlbumDataBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowAlbumData extends AppCompatActivity {
    ActivityShowAlbumDataBinding binding;
    String postId;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowAlbumDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeStatusBar();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        postId = getIntent().getStringExtra("postId");
        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ShowAlbumData.this)
                        .setTitle("Delete Photo")
                        .setMessage("Are You Sure To delete ?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference referencePosts = FirebaseDatabase
                                        .getInstance().getReference("data/Posts/");
                                referencePosts.child(postId).removeValue();

                                DatabaseReference referenceAlbum = FirebaseDatabase
                                        .getInstance().getReference("data/specificUserPets/"+userId+"/Album_Photos");
                                referenceAlbum.child(postId).removeValue();

                                DatabaseReference referenceLikes = FirebaseDatabase
                                        .getInstance().getReference("data/Likes");
                                referenceLikes.child(postId).removeValue().addOnSuccessListener(unused -> {
                                    Toast.makeText(ShowAlbumData.this, "Post Deleted Successfully ", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(ShowAlbumData.this,MainActivity.class);
                                    intent.putExtra("refresh","refresh");
                                    startActivity(intent);
                                    finish();
                                });
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Glide.with(this).load(getIntent().getStringExtra("photo")).into(binding.postImage);
        binding.description.setText(getIntent().getStringExtra("description"));
        DatabaseReference db = FirebaseDatabase
                .getInstance().getReference("data/Likes");
        db.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(postId)) {
                    binding.likesText.setText((int) snapshot.getChildrenCount()+" Likes");
                }else {
                    binding.likesText.setText("0 Likes");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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