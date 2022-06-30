package com.example.petpolite;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.petpolite.Classes.Pet_Profile;
import com.example.petpolite.Classes.User;
import com.example.petpolite.Utilities.ClickedCallback;
import com.example.petpolite.Utilities.DialogShow;
import com.example.petpolite.Utilities.MyCallback;
import com.example.petpolite.databinding.ActivityCreatePetProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Calendar;

public class Create_Pet_Profile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Uri filePAthProfilePic, filepathCoverPic;
    String petProfilePic_URL, coverPic_URL;
    boolean isProfPicSelected, isCoverPicSelected;
    Pet_Profile pet_profile = new Pet_Profile();
    ProgressDialog progressDialog;
    boolean imageGet = false;
    boolean infoGet = false;
    FirebaseDatabase database;
    ActivityCreatePetProfileBinding binding;
    String userName;
    boolean isPetUploaded;
    String userId;
    boolean isCat, isFish, isRabbit, isReptile, isHorse, isGoat_Sheep, isCow_buffalo, isBird, isDog, isCategorySelected;
    String categoryFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePetProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeStatusBar();


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();

        database = FirebaseDatabase.getInstance();
        getUserNameDataBase(new MyCallback() {
            @Override
            public void onCallback(String name) {
                userName = name;
            }
        });

        binding.uploadProfileBtn.setOnClickListener(view -> Dexter.withContext(Create_Pet_Profile.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Please Select The Image"), 5);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText((Create_Pet_Profile.this), "Permission Denied !", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.Category,
                R.layout.category_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categoryName.setAdapter(arrayAdapter);
        binding.categoryName.setOnItemSelectedListener(this);

        binding.coverPicBtn.setOnClickListener(view -> Dexter.withContext(Create_Pet_Profile.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Please Select The Image"), 10);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText((Create_Pet_Profile.this), "Permission Denied !", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check());
        //Date Picker
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        binding.calenderBtn.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Create_Pet_Profile.this, (view1, year1, month1, day1) -> {
                month1 = month1 + 1;
                String date = day1 + "/" + month1 + "/" + year1;
                binding.dateOfBirthEdt.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });


        binding.createProfileBtn.setOnClickListener(view -> {
            //Checking Internet
            if (!(isNetworkConnected())) {
                DialogShow.showCustomDialogNoInternet("Check Your Internet Connection And Try Again ",
                        "No Internet", Create_Pet_Profile.this, new ClickedCallback() {
                            @Override
                            public void YesClicked() {
                            }

                            @Override
                            public void NoClicked() {
                            }
                        });
            }
            String name, age, bread_name, dateOfBirth, gender, careNote;
            name = binding.nameEdt.getText().toString();
            age = binding.ageEdt.getText().toString();
            bread_name = binding.breedEdt.getText().toString();
            dateOfBirth = binding.dateOfBirthEdt.getText().toString();
            gender = binding.genderEdt.getText().toString();
            careNote = binding.careNotEdt.getText().toString();

            if (name.isEmpty()) {
                binding.nameEdt.setError("Name Should not empty");
            } else {
                name = binding.nameEdt.getText().toString();
            }

            if (age.isEmpty()) {
                binding.ageEdt.setError("Age Should not empty");
            } else {
                age = binding.ageEdt.getText().toString();
            }
            if (bread_name.isEmpty()) {
                binding.breedEdt.setError("Breed Name Should not empty");
            } else {
                bread_name = binding.breedEdt.getText().toString();
            }
            if (dateOfBirth.isEmpty()) {
                binding.dateOfBirthEdt.setError("Date of Birth Should not empty");
            } else {
                dateOfBirth = binding.dateOfBirthEdt.getText().toString();
            }
            if (gender.isEmpty()) {
                binding.genderEdt.setError("Gender Should not empty");
            } else {
                gender = binding.genderEdt.getText().toString();
            }
            if (careNote.isEmpty()) {
                binding.careNotEdt.setError("Care Note Should not empty");
            } else {
                careNote = binding.careNotEdt.getText().toString();
            }
            if (!isCategorySelected) {
                Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
            }
            infoGet = true;

            pet_profile.setBreed_Pet(bread_name);
            pet_profile.setCareNote_Pet(careNote);
            pet_profile.setName_Pet(name);
            pet_profile.setGender_Pet(gender);
            pet_profile.setDate_of_birth_Pet(dateOfBirth);
            pet_profile.setAge(age);


            if (!isProfPicSelected && !isCoverPicSelected) {
                new AlertDialog.Builder(Create_Pet_Profile.this)
                        .setTitle("Image Upload")
                        .setMessage("Upload Pet Image")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
            }

            if ((filePAthProfilePic != null) && (filepathCoverPic != null)) {
                progressDialog = new ProgressDialog(Create_Pet_Profile.this);
                progressDialog.setTitle("Image Uploading");
                progressDialog.show();

                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference reference = storage.getReference().child("app_files/" + userName + "/"
                        + name + "/Images").child(name + "_profilePhoto_pet");


                StorageReference reference1 = storage.getReference().child("app_files/" + userName + "/"
                        + name + "/Images").child(name + "_coverPhoto_pet");


                reference.putFile(filePAthProfilePic).addOnProgressListener(snapshot -> {
                    double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + (int) progress + " %");
                }).addOnSuccessListener(taskSnapshot -> {
                    reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        petProfilePic_URL = uri.toString();
                        pet_profile.setProfilePhoto_pet(petProfilePic_URL);
                        isPetUploaded = addPetToFireBase(pet_profile);
                    }).addOnFailureListener(e -> {
                        new AlertDialog.Builder(getApplicationContext())
                                .setTitle("Image Uploading Error")
                                .setMessage("Image is Not Uploaded try Again")
                                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    });
                }).addOnFailureListener(e -> {
                    Toast.makeText(Create_Pet_Profile.this, "Failed To Upload Image \n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });


                reference1.putFile(filepathCoverPic).addOnProgressListener(snapshot -> {
                }).addOnSuccessListener(taskSnapshot -> {

                    reference1.getDownloadUrl().addOnSuccessListener(uri -> {
                        coverPic_URL = uri.toString();
                        pet_profile.setCoverPhoto_pet(coverPic_URL);
                        isPetUploaded = addPetToFireBase(pet_profile);

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new AlertDialog.Builder(getApplicationContext())
                                    .setTitle("Image Uploading Error")
                                    .setMessage("Image is Not Uploaded try Again")
                                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    });
                    progressDialog.dismiss();
                }).addOnFailureListener(e -> Toast.makeText(Create_Pet_Profile.this, "Failed To Upload Image \n" + e.getMessage(), Toast.LENGTH_SHORT).show());

                imageGet = true;
            }
            if (imageGet && infoGet) {

                binding.nameEdt.getText().clear();
                binding.ageEdt.getText().clear();
                binding.breedEdt.getText().clear();
                binding.dateOfBirthEdt.getText().clear();
                binding.genderEdt.getText().clear();
                binding.careNotEdt.getText().clear();


            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {

            filePAthProfilePic = data.getData();
            Glide.with(Create_Pet_Profile.this).load(filePAthProfilePic).placeholder(R.drawable.loader).into(binding.profilePic);
            isProfPicSelected = true;
        }
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            filepathCoverPic = data.getData();
            Glide.with(Create_Pet_Profile.this).load(filepathCoverPic).placeholder(R.drawable.loader).into(binding.coverPic);
            isProfPicSelected = true;
        }
    }

    private void saveAppState(boolean isSignup, boolean isProfileCreated) {
        SharedPreferences.Editor editor = getSharedPreferences("AppState", MODE_PRIVATE).edit();
        editor.putBoolean("isSignUp", isSignup);
        editor.putBoolean("isProfileCreated", isProfileCreated);
        editor.apply();
    }

    //Utility Methods
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null)
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatusBar() {
        Window window = Create_Pet_Profile.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.myBlue));
    }

    boolean addPetToFireBase(Pet_Profile pet_profile1) {

        DatabaseReference node = database.getReference("/data/specificUserPets/" + userId + "/Pet_Profiles/");
        node.child(pet_profile.getName_Pet()).setValue(pet_profile1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Create_Pet_Profile.this, "Pet Uploaded Successfully ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Create_Pet_Profile.this, MainActivity.class);
                saveAppState(true, true);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Create_Pet_Profile.this, "Pet Uploaded Failed \n" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        return true;


    }

    public void getUserNameDataBase(MyCallback callback) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = firebaseUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference node = database.getReference("/data/Users/" + userUid);
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                callback.onCallback(u.getName_user());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryFilter = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, "" + categoryFilter, Toast.LENGTH_SHORT).show();
        if (categoryFilter.equals("Select")) {
            isCategorySelected = false;
        } else {
            isCategorySelected = true;
            pet_profile.setCategory(categoryFilter);
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
        if (categoryFilter.equals("Goats/ Sheep")) {
            isGoat_Sheep = true;
        } else {
            isGoat_Sheep = false;
        }
        if (categoryFilter.equals("Cows/ Buffaloes")) {
            isCow_buffalo = true;
        } else {
            isCow_buffalo = false;
        }
        if (categoryFilter.equals("Birds")) {
            isBird = true;
        } else {
            isBird = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Select Search Filter !", Toast.LENGTH_SHORT).show();

    }
}
