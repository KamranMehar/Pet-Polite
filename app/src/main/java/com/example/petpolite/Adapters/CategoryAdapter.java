package com.example.petpolite.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petpolite.Classes.PetCategories;
import com.example.petpolite.R;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    Context context;
    List<PetCategories> categoryList;

    public CategoryAdapter(Context context, List<PetCategories> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        PetCategories petCategories=categoryList.get(position);
        holder.sleep_need.setText(petCategories.getSleep_need());
        holder.description.setText(petCategories.getDescription());
        holder.housing.setText(petCategories.getHousing());
        holder.handling.setText(petCategories.getHandling());
        holder.breedName.setText(petCategories.getBreedName());
        holder.grooming.setText(petCategories.getGrooming());
        holder.feeding.setText(petCategories.getFeeding());
        Glide.with(context).load(petCategories.getPhoto()).into(holder.petImageItem);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView breedName,description,feeding,grooming,handling,housing,sleep_need;
        ImageView petImageItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            breedName=itemView.findViewById(R.id.breedNameItem);
            description=itemView.findViewById(R.id.descriptionItem);
            feeding=itemView.findViewById(R.id.feedingItem);
            grooming=itemView.findViewById(R.id.groomingItem);
            handling=itemView.findViewById(R.id.handlingItem);
            housing=itemView.findViewById(R.id.housingItem);
            sleep_need=itemView.findViewById(R.id.sleep_needItem);
            petImageItem=itemView.findViewById(R.id.petImageItem);
        }
    }
}
