package com.example.petpolite.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.petpolite.Adapters.PostAdapter;
import com.example.petpolite.Classes.Post_pets;
import com.example.petpolite.R;
import com.example.petpolite.databinding.FragmentHomePostsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home_Posts_Fragment extends Fragment {

    FragmentHomePostsBinding binding;
    String UserID;
    boolean isGuest;
    PostAdapter adapter;
    List<Post_pets> post_petsList;
    public Home_Posts_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomePostsBinding.inflate(inflater, container, false);
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppState", Context.MODE_PRIVATE);
        isGuest = prefs.getBoolean("isGuest", false);
        if (!isGuest) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            UserID = user.getUid();
        }
        //updated
       List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.community__,ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.category__,ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.notification__,ScaleTypes.CENTER_INSIDE));
        slideModels.add(new SlideModel(R.drawable.categories_slide,ScaleTypes.CENTER_INSIDE));

        binding.imageSlider.setImageList(slideModels);
       binding.imageSlider.setImageList(slideModels);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        post_petsList=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("/data/Posts/");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 :snapshot.getChildren()){
                        Post_pets post=snapshot1.getValue(Post_pets.class);
                        if (post_petsList.isEmpty()){
                            binding.progressBar.setVisibility(View.VISIBLE);
                        }else {
                            binding.progressBar.setVisibility(View.INVISIBLE);
                        }
                        post_petsList.add(post);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        adapter=new PostAdapter(post_petsList,getContext(),isGuest,UserID);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

}