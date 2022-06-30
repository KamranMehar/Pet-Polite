package com.example.petpolite.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import com.example.petpolite.Adapters.AlbumAdapter;
import com.example.petpolite.Classes.Pet_Profile;
import com.example.petpolite.Classes.Post_pets;
import com.example.petpolite.MainActivity;
import com.example.petpolite.R;
import com.example.petpolite.databinding.FragmentPetProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class Pet_Profile_Fragment extends Fragment {
    Uri filePAthCoverPic, filePAthProfilePic;
    String profile_URL;
    String coverPic_URL;
    String petName, userName;
    String userUid;
    List<Post_pets> albumList;
    FragmentPetProfileBinding binding;
    AlbumAdapter adapter;
    int petListSize;
    Pet_Profile pet1;
    Pet_Profile pet2;
    List<Pet_Profile> pet_profileList;
    public Pet_Profile_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentPetProfileBinding.inflate(inflater, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userUid = user.getUid();
        pet_profileList=new ArrayList<>();
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference node = database.getReference("/data/specificUserPets/" + userUid + "/Pet_Profiles");
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
                                Glide.with(Pet_Profile_Fragment.this).load(pet1.getProfilePhoto_pet()).placeholder(R.drawable.loader).into(binding.petProfileImage1);
                                Glide.with(Pet_Profile_Fragment.this).load(pet1.getCoverPhoto_pet()).placeholder(R.drawable.loader).into(binding.coverPic);
                                binding.petProfileName.setText(pet1.getName_Pet());
                                binding.careNote.setText(pet1.getCareNote_Pet());
                                binding.gender.setText(pet1.getGender_Pet());
                                binding.dateOfBirth.setText(pet1.getDate_of_birth_Pet());
                                binding.breed.setText(pet1.getBreed_Pet());
                                binding.age.setText(pet1.getAge());
                                binding.category.setText(pet1.getCategory());
                            }else if (MainActivity.currentProfileLoaded==2){
                                Glide.with(Pet_Profile_Fragment.this).load(pet2.getProfilePhoto_pet()).placeholder(R.drawable.loader).into(binding.petProfileImage1);
                                Glide.with(Pet_Profile_Fragment.this).load(pet2.getCoverPhoto_pet()).placeholder(R.drawable.loader).into(binding.coverPic);
                                binding.petProfileName.setText(pet2.getName_Pet());
                                binding.careNote.setText(pet2.getCareNote_Pet());
                                binding.gender.setText(pet2.getGender_Pet());
                                binding.dateOfBirth.setText(pet2.getDate_of_birth_Pet());
                                binding.breed.setText(pet2.getBreed_Pet());
                                binding.age.setText(pet2.getAge());
                                binding.category.setText(pet2.getCategory());

                            }

                        } else {
                            pet1 = pet_profileList.get(0);
                            if (MainActivity.currentProfileLoaded==0 || MainActivity.currentProfileLoaded==1 ){
                                Glide.with(Pet_Profile_Fragment.this).load(pet1.getProfilePhoto_pet()).placeholder(R.drawable.loader).into(binding.petProfileImage1);
                                Glide.with(Pet_Profile_Fragment.this).load(pet1.getCoverPhoto_pet()).placeholder(R.drawable.loader).into(binding.coverPic);
                                binding.petProfileName.setText(pet1.getName_Pet());
                                binding.careNote.setText(pet1.getCareNote_Pet());
                                binding.gender.setText(pet1.getGender_Pet());
                                binding.dateOfBirth.setText(pet1.getDate_of_birth_Pet());
                                binding.breed.setText(pet1.getBreed_Pet());
                                binding.age.setText(pet1.getAge());
                                binding.category.setText(pet1.getCategory());
                            }else if (MainActivity.currentProfileLoaded==2){
                                Glide.with(Pet_Profile_Fragment.this).load(pet2.getProfilePhoto_pet()).placeholder(R.drawable.loader).into(binding.petProfileImage1);
                                Glide.with(Pet_Profile_Fragment.this).load(pet2.getCoverPhoto_pet()).placeholder(R.drawable.loader).into(binding.coverPic);
                                binding.petProfileName.setText(pet2.getName_Pet());
                                binding.careNote.setText(pet2.getCareNote_Pet());
                                binding.gender.setText(pet2.getGender_Pet());
                                binding.dateOfBirth.setText(pet2.getDate_of_birth_Pet());
                                binding.breed.setText(pet2.getBreed_Pet());
                                binding.age.setText(pet2.getAge());
                                binding.category.setText(pet2.getCategory());
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

        binding.uploadPicBTN.setOnClickListener(view1 -> {
            Dexter.withContext(getContext())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Please Select The Image"), 2);
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

        binding.coverPicBtn.setOnClickListener(view1 -> {
            Dexter.withContext(getContext())
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Please Select The Image"), 3);
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

        //Getting Album pet photos from Firebase DB
        albumList=new ArrayList<>();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("/data/specificUserPets/"+userUid+"/Album_Photos");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot snapshot1 :snapshot.getChildren()){
                        Post_pets post=snapshot1.getValue(Post_pets.class);
                        albumList.add(post);
                        RecyclerView recyclerView=binding.recyclerView;
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        adapter=new AlbumAdapter(albumList,getContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            filePAthProfilePic = data.getData();
            if (filePAthProfilePic != null) {
                Glide.with(Pet_Profile_Fragment.this).load(filePAthProfilePic).into(binding.petProfileImage1);

                if (userName != null && petName != null) {
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Image Uploading");
                    progressDialog.show();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference reference = storage.getReference().child("app_files/" + userName + "/"
                            + petName + "/Images").child(petName + "_profilePhoto_pet");
                    reference.putFile(filePAthProfilePic).addOnProgressListener(snapshot -> {
                        double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) progress + " %");
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profile_URL = uri.toString();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference node = database.getReference("/data/PetsProfiles");
                                    node.child(petName).child("profilePhoto_pet").setValue(profile_URL);

                                    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                    DatabaseReference node1 = database1.getReference("/data/specificUserPets/" + userUid + "/Pet_Profiles");
                                    node1.child(petName).child("profilePhoto_pet").setValue(profile_URL);
                                    progressDialog.dismiss();
                                }
                            });

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "User Name Or Pet Name Is Null for Uploading Profile Photo", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            filePAthCoverPic = data.getData();
            if (filePAthCoverPic != null) {
                Glide.with(Pet_Profile_Fragment.this).load(filePAthCoverPic).into(binding.coverPic);
                if (userName != null && petName != null) {
                    ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Image Uploading");
                    progressDialog.show();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference reference = storage.getReference().child("app_files/" + userName + "/"
                            + petName + "/Images").child(petName + "_coverPhoto_pet");
                    reference.putFile(filePAthCoverPic).addOnProgressListener(snapshot -> {
                        double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) progress + " %");
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    coverPic_URL = uri.toString();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference node = database.getReference("/data/PetsProfiles");
                                    node.child(petName).child("coverPhoto_pet").setValue(coverPic_URL);

                                    FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                    DatabaseReference node1 = database1.getReference("/data/specificUserPets/" +
                                            userUid + "/Pet_Profiles/"+petName).child("coverPhoto_pet");
                                    node1.setValue(coverPic_URL);
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "User Name or Pet Name is Null for Uploading Cover Photo", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
