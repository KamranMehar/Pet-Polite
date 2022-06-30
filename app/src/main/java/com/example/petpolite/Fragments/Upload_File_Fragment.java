package com.example.petpolite.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.petpolite.Classes.Pet_Profile;
import com.example.petpolite.Classes.Post_pets;
import com.example.petpolite.MainActivity;
import com.example.petpolite.R;
import com.example.petpolite.databinding.FragmentUploadFileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Upload_File_Fragment extends Fragment {
    Uri filePAth;
    String petName, userName;
    Post_pets post_pets;
    boolean isImageSelected = false;
    Random randomNumber;
    String postID;
    String description;
    String profilePicURL;
    String userID;
    List<Pet_Profile> pet_profileList;
    FragmentUploadFileBinding binding;
    int petListSize;
    Pet_Profile pet1;
    Pet_Profile pet2;
    public Upload_File_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUploadFileBinding.inflate(inflater, container, false);


//get User Name And Pet Name from database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        pet_profileList=new ArrayList<>();
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference node = database.getReference("/data/specificUserPets/" + userID + "/Pet_Profiles");
            node.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Pet_Profile p = dataSnapshot.getValue(Pet_Profile.class);
                            pet_profileList.add(p);
                        }

                        petListSize = pet_profileList.size();
                        if (petListSize == 2) {
                            pet1 = pet_profileList.get(0);
                            pet2=pet_profileList.get(1);
                            if (MainActivity.currentProfileLoaded==0 || MainActivity.currentProfileLoaded==1 ){
                                profilePicURL=pet1.getProfilePhoto_pet();
                                petName=pet1.getName_Pet();
                            }else if (MainActivity.currentProfileLoaded==2){
                                profilePicURL=pet2.getProfilePhoto_pet();
                                petName=pet2.getName_Pet();
                            }

                        } else {
                            pet1 = pet_profileList.get(0);
                            if (MainActivity.currentProfileLoaded==0 || MainActivity.currentProfileLoaded==1 ){
                                profilePicURL=pet1.getProfilePhoto_pet();
                                petName=pet1.getName_Pet();
                            }else if (MainActivity.currentProfileLoaded==2){
                                profilePicURL=pet2.getProfilePhoto_pet();
                                petName=pet2.getName_Pet();
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "No Data Found On Database ", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Log.e( "onCreateView: ",e.getMessage() );
        }

        post_pets = new Post_pets();
        randomNumber = new Random();
        int rand = randomNumber.nextInt();
        postID = rand + "";

        binding.browsBtn.setOnClickListener(view1 -> {

            Dexter.withContext(getContext())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Please Select The Image"), 1);
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                            Toast.makeText(getContext(), "Permission Denied !", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        });

        binding.uploadBtn.setOnClickListener(view -> {
            description = binding.descriptionEdt.getText().toString();
            if (description.isEmpty()) {
                binding.descriptionEdt.setError("Write Something In Description ");
                return;
            } else {
                description = binding.descriptionEdt.getText().toString();
            }
            if (userName==null && userID==null && profilePicURL==null){
                Toast.makeText(getContext(), "User Name or user Id is null refresh and open upload page Again", Toast.LENGTH_LONG).show();
                return;
            }
            if (isImageSelected) {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Image Uploading");
                progressDialog.show();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference reference = storage.getReference().child("app_files/"
                        + userName + "/"
                        + petName + "/Post/Images/").child(postID);
                reference.putFile(filePAth).addOnProgressListener(snapshot -> {
                    double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + (int) progress + " %");
                }).addOnSuccessListener(taskSnapshot -> {
                    progressDialog.dismiss();
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        post_pets.setPostImageURL(uri.toString());
                        post_pets.setPostID(postID);
                        post_pets.setPostDescription(description);
                        post_pets.setName_Pet(petName);
                        post_pets.setProfilePhoto_pet(profilePicURL);


                        //Upload data to all post root node
                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                        DatabaseReference node1 = database1.getReference("/data/Posts/");
                        node1.child(post_pets.getPostID()).setValue(post_pets);

                        //Upload data to specific user post
                        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                        DatabaseReference node2 = database2.getReference("/data/specificUserPets/"+userID+"/Album_Photos");
                        node2.child(post_pets.getPostID()).setValue(post_pets);

                        binding.descriptionEdt.getText().clear();
                        Glide.with(Upload_File_Fragment.this).load(R.drawable.cat_cover_pic).into(binding.imageUpload);
                        Toast.makeText(getContext(), "Post is Added Successfully ", Toast.LENGTH_SHORT).show();

                    });
                });
            } else {
                Toast.makeText(getContext(), "Image Not Selected !", Toast.LENGTH_LONG).show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            filePAth = data.getData();
            if (filePAth != null) {
                Glide.with(Upload_File_Fragment.this).load(filePAth).into(binding.imageUpload);
                isImageSelected = true;
            }
        }

    }

    void getPetProfilePhoto() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference node = database.getReference("/data/specificUserPets/" + userID +
                "/Pet_Profiles/"+petName+"/profilePhoto_pet");
        node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    profilePicURL = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                    Toast.makeText(getContext(), profilePicURL, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}