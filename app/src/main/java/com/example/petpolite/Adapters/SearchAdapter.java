package com.example.petpolite.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.petpolite.ShowSearchResult;

import java.util.List;

public class SearchAdapter extends  RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    Context context;
    List<PetCategories> categoryList;
    List<PetCategories> filteredList;

    public SearchAdapter(Context context, List<PetCategories> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }
    public void setFilteredList(List<PetCategories> list){
        filteredList=list;
        categoryList=filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_rv_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        PetCategories petCategories=categoryList.get(position);
        holder.nameAnimal.setText(petCategories.getBreedName());
        Glide.with(context).load(petCategories.getPhoto()).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            openShowItemActivity(petCategories);

        });
    }

    private void openShowItemActivity(PetCategories petCategories) {
        Intent intent=new Intent(context, ShowSearchResult.class);
        intent.putExtra("breed",petCategories.getBreedName());
        intent.putExtra("photo",petCategories.getPhoto());
        intent.putExtra("description",petCategories.getDescription());
        intent.putExtra("feeding",petCategories.getFeeding());
        intent.putExtra("grooming",petCategories.getGrooming());
        intent.putExtra("handling",petCategories.getHandling());
        intent.putExtra("sleep",petCategories.getSleep_need());
        intent.putExtra("house",petCategories.getHousing());
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView nameAnimal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.searchItemImage);
            nameAnimal=itemView.findViewById(R.id.searchItemName);
        }
    }
}
