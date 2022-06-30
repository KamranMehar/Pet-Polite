package com.example.petpolite.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petpolite.Adapters.CategoryAdapter;
import com.example.petpolite.Classes.PetCategories;
import com.example.petpolite.Create_Category_AdminOnly;
import com.example.petpolite.R;
import com.example.petpolite.databinding.FragmentCategoriesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Categories_Fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    ArrayList<PetCategories> petCategoriesList;
    FragmentCategoriesBinding binding;
    boolean isCat, isFish, isRabbit, isReptile, isHorse, isGoat_Sheep, isCow_buffalo, isBird, isDog, isFilterSelected;
    String categoryFilter,category;

    public Categories_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);

        if (isAdmin()){
            binding.createCategory.setVisibility(View.VISIBLE);
        }else {
            binding.createCategory.setVisibility(View.INVISIBLE);
        }

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Category,
                R.layout.category_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categoryName.setAdapter(arrayAdapter);
        binding.categoryName.setOnItemSelectedListener(Categories_Fragment.this);


        binding.createCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Create_Category_AdminOnly.class);
                startActivity(intent);
            }
        });

        return binding.getRoot();
    }




    public static boolean isAdmin() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        String currentUser = mFirebaseAuth.getUid();
        return currentUser.contains("sFosgwVmY8eB09oBz7Rf87uOCj42");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryFilter = parent.getItemAtPosition(position).toString();
        if (categoryFilter.equals("Select")) {
            isFilterSelected = false;
        } else {
            isFilterSelected = true;
            Toast.makeText(getContext(), "" + categoryFilter, Toast.LENGTH_SHORT).show();
            category = categoryFilter;
        }
        if (categoryFilter.equals("Cats")) {
            isCat = true;
        } else {
            isCat = false;
        }
        if (categoryFilter.equals("Dogs")) {
            isDog = true;

        } else {
            isDog = false;
        }
        if (categoryFilter.equals("Fishes")) {
            isFish = true;
        } else {
            isFish = false;
        }
        if (categoryFilter.equals("Rabbits")) {
            isRabbit = true;
        } else {
            isRabbit = false;
        }
        if (categoryFilter.equals("Reptiles")) {
            isReptile = true;
        } else {
            isReptile = false;
        }
        if (categoryFilter.equals("Horses")) {
            isHorse = true;
        } else {
            isHorse = false;
        }
        if (categoryFilter.equals("Goats_Sheep")) {
            isGoat_Sheep = true;
        } else {
            isGoat_Sheep = false;
        }
        if (categoryFilter.equals("Cows_Buffaloes")) {
            isCow_buffalo = true;
        } else {
            isCow_buffalo = false;
        }
        if (categoryFilter.equals("Birds")) {
            isBird = true;
        } else {
            isBird = false;
        }
        petCategoriesList = new ArrayList<>();
        String path;
        if (isFilterSelected){
            path="/data/CategoriesPet/" + categoryFilter;
        }else {
            path="/data/CategoriesPet/Cats";
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        PetCategories pet_category = snapshot1.getValue(PetCategories.class);
                        petCategoriesList.add(pet_category);
                        RecyclerView recyclerView = binding.categoriesRecyclerview;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        CategoryAdapter adapter = new CategoryAdapter(getContext(), petCategoriesList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}