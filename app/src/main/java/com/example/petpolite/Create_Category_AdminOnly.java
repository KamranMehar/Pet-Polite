package com.example.petpolite;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.example.petpolite.Classes.PetCategories;
import com.example.petpolite.databinding.ActivityCreateCategoryAdminOnlyBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class Create_Category_AdminOnly extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ActivityCreateCategoryAdminOnlyBinding binding;
    Uri imagePath;
    String categoryName;
    PetCategories petCategories;
    String breedName;
    boolean isImageSelected = false;
    ProgressDialog progressDialog;
    boolean isCat, isFish, isRabbit, isReptile, isHorse, isGoat_Sheep, isCow_buffalo, isBird, isDog, isCategorySelected;
    String categoryPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateCategoryAdminOnlyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Category,
                R.layout.category_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categoryName.setAdapter(arrayAdapter);
        binding.categoryName.setOnItemSelectedListener(this);

        progressDialog = new ProgressDialog(this);
        petCategories = new PetCategories();
        binding.browsImage.setOnClickListener(view -> Dexter.withContext(this)
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
                        Toast.makeText(Create_Category_AdminOnly.this, "Permission Denied !", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());


        binding.add.setOnClickListener(view -> {
            String description, feeding, grooming, handling, housing, sleep_need, category;
            breedName = binding.breedNameEdt.getText().toString();
            description = binding.descriptionEdt.getText().toString();
            feeding = binding.feedingEdt.getText().toString();
            grooming = binding.groomingEdt.getText().toString();
            handling = binding.handlingEdt.getText().toString();
            housing = binding.housingEdt.getText().toString();
            sleep_need = binding.sleepEDt.getText().toString();

            if (breedName.isEmpty()) {
                binding.breedNameEdt.setError("Empty Not allowed");
            } else {
                breedName = binding.breedNameEdt.getText().toString();
            }
            if (description.isEmpty()) {
                binding.descriptionEdt.setError("Empty Not allowed");
            } else {
                description = binding.descriptionEdt.getText().toString();
            }
            if (feeding.isEmpty()) {
                binding.feedingEdt.setError("Empty Not allowed");
            } else {
                feeding = binding.feedingEdt.getText().toString();
            }
            if (grooming.isEmpty()) {
                binding.groomingEdt.setError("Empty Not allowed");
            } else {
                grooming = binding.groomingEdt.getText().toString();
            }
            if (handling.isEmpty()) {
                binding.handlingEdt.setError("Empty Not allowed");
            } else {
                handling = binding.handlingEdt.getText().toString();
            }
            if (housing.isEmpty()) {
                binding.housingEdt.setError("Empty Not allowed");
            } else {
                housing = binding.housingEdt.getText().toString();
            }
            if (sleep_need.isEmpty()) {
                binding.sleepEDt.setError("Empty Not allowed");
            } else {
                sleep_need = binding.sleepEDt.getText().toString();
            }
            petCategories.setBreedName(breedName);
            petCategories.setDescription(description);
            petCategories.setFeeding(feeding);
            petCategories.setGrooming(grooming);
            petCategories.setHandling(handling);
            petCategories.setHousing(housing);
            petCategories.setSleep_need(sleep_need);

            if (isImageSelected && isCategorySelected) {
                addPetCategoryToDatabase(petCategories);
            } else {
                Toast.makeText(this, "Add Pet Image ! or select Category", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            imagePath = data.getData();
            Glide.with(this).load(imagePath).into(binding.petImage);
            isImageSelected = true;
        }
    }

    void addPetCategoryToDatabase(PetCategories petCategories) {
        progressDialog.setTitle("Image Uploading");
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference reference = storage.getReference().child("app_files/Categories/" + categoryPet + petCategories.getBreedName());
        reference.putFile(imagePath).addOnProgressListener(snapshot -> {
            double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
            progressDialog.setMessage("Uploaded " + (int) progress + " %");
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        petCategories.setPhoto(downloadUrl.toString());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference node = database.getReference("/data/CategoriesPet/" + categoryPet);
                        node.child(petCategories.getBreedName().trim()).setValue(petCategories);
                        progressDialog.dismiss();
                        petCategories.save();
                        Toast.makeText(Create_Category_AdminOnly.this, "Pet Category added Successfully ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Create_Category_AdminOnly.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        progressDialog.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryName = parent.getItemAtPosition(position).toString();

        if (categoryName.equals("Select")) {
            isCategorySelected = false;
        } else {
            isCategorySelected = true;
            categoryPet = categoryName;
            petCategories.setCategory(categoryName);
        }
        if (categoryName.equals("Cats")) {
            isCat = true;
        } else {
            isCat = false;
        }
        if (categoryName.equals("Dogs")) {
            isDog = true;

        } else {
            isDog = false;
        }
        if (categoryName.equals("Fishes")) {
            isFish = true;
        } else {
            isFish = false;
        }
        if (categoryName.equals("Rabbits")) {
            isRabbit = true;
        } else {
            isRabbit = false;
        }
        if (categoryName.equals("Reptiles")) {
            isReptile = true;
        } else {
            isReptile = false;
        }
        if (categoryName.equals("Horses")) {
            isHorse = true;
        } else {
            isHorse = false;
        }
        if (categoryName.equals("Goats/ Sheep")) {
            isGoat_Sheep = true;
        } else {
            isGoat_Sheep = false;
        }
        if (categoryName.equals("Cows/ Buffaloes")) {
            isCow_buffalo = true;
        } else {
            isCow_buffalo = false;
        }
        if (categoryName.equals("Birds")) {
            isBird = true;
        } else {
            isBird = false;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}