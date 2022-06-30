package com.example.petpolite.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petpolite.Classes.Post_pets;
import com.example.petpolite.R;
import com.example.petpolite.ShowPostImage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    Context context;
    boolean isGuest;
    Boolean isLiked = false;
    String UserId;
    List<Post_pets> post_petsList;

    DatabaseReference likeReference = FirebaseDatabase.getInstance().getReference("/data/Likes/");

    public PostAdapter(List<Post_pets> post_petsList, Context context, Boolean isGuest, String UserId) {
        this.context = context;
        this.isGuest = isGuest;
        this.UserId = UserId;
        this.post_petsList = post_petsList;
    }


    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        Post_pets model=post_petsList.get(position);
        holder.description.setText(model.getPostDescription());
        holder.petNamePostItem.setText(model.getName_Pet());
        Glide.with(context).load(model.getPostImageURL()).placeholder(R.drawable.loading_).into(holder.postImage);
        Glide.with(context).load(model.getProfilePhoto_pet()).into(holder.profilePic);

        if (!isGuest) {
            likeReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(model.postID).hasChild(UserId)){
                        holder.likeImage.setImageResource(R.drawable.like_ic);
                        int count=  (int)snapshot.child(model.postID).getChildrenCount();
                        holder.likesNo.setText(count+" Likes");
                    }else {
                        holder.likeImage.setImageResource(R.drawable.no_like_ic);
                        int count=  (int)snapshot.child(model.postID).getChildrenCount();
                        holder.likesNo.setText(count+" Likes");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ShowPostImage.class);
                    intent.putExtra("description", model.getPostDescription());
                    intent.putExtra("photo", model.getPostImageURL());
                    intent.putExtra("postId", model.getPostID());
                    intent.putExtra("petName", model.getName_Pet());
                    intent.putExtra("userDp", model.getProfilePhoto_pet());
                    intent.putExtra("userId",UserId);
                    intent.putExtra("isGuest",isGuest);
                    context.startActivity(intent);
            });

            holder.likeImage.setOnClickListener(v -> {
                isLiked=true;

                likeReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (isLiked){
                            if (snapshot.child(model.postID).hasChild(UserId)){
                                likeReference.child(model.postID).child(UserId).removeValue();
                                isLiked=false;
                            }else {
                                likeReference.child(model.postID).child(UserId).setValue(UserId);
                                isLiked=false;
                                makeSound(context);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            });
        } else {
            holder.likeImage.setImageResource(R.drawable.no_like_ic);
            likeReference.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count=  (int)snapshot.child(model.postID).getChildrenCount();
                    holder.likesNo.setText(count+" Likes");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            holder.likeImage.setOnClickListener(v -> Toast.makeText(context, "Guest User Can't Like post !", Toast.LENGTH_SHORT).show());
        }

    }

    @Override
    public int getItemCount() {
        return post_petsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView likeImage, postImage, profilePic;
        TextView likesNo, description, petNamePostItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postItemImage);
            description = itemView.findViewById(R.id.descriptionPostItem);
            profilePic = itemView.findViewById(R.id.postProfilePicItem);
            petNamePostItem = itemView.findViewById(R.id.petNamePostItem);
            likeImage = itemView.findViewById(R.id.like);
            likesNo = itemView.findViewById(R.id.likeNo);
        }
    }


    public static void makeSound(Context context) {
        final MediaPlayer mp = MediaPlayer.create(context, R.raw.like_sound);
        mp.start();
    }
}