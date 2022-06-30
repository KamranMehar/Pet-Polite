package com.example.petpolite.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.petpolite.Classes.Post_pets;
import com.example.petpolite.R;
import com.example.petpolite.ShowAlbumData;


import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    List<Post_pets> post_petsList;
    Context context;

    public AlbumAdapter(List<Post_pets> post_petsList, Context context) {
        this.post_petsList = post_petsList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        Post_pets post_pets=post_petsList.get(position);
        Glide.with(context).load(post_pets.getPostImageURL()).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, ShowAlbumData.class);
            intent.putExtra("description",post_pets.getPostDescription());
            intent.putExtra("photo",post_pets.getPostImageURL());
            intent.putExtra("postId",post_pets.getPostID());
            intent.putExtra("petName",post_pets.getName_Pet());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return post_petsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.album_item_Image);
        }
    }
}
