package com.example.petpolite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.petpolite.Classes.NotificationReceiver;
import com.example.petpolite.Classes.PetCategories;
import com.example.petpolite.Classes.Pet_Profile;
import com.example.petpolite.Fragments.Alert_Notifications_Fragment;
import com.example.petpolite.Fragments.Categories_Fragment;
import com.example.petpolite.Fragments.Home_Posts_Fragment;
import com.example.petpolite.Fragments.Pet_Profile_Fragment;
import com.example.petpolite.Fragments.Search_Fragment;
import com.example.petpolite.Fragments.Upload_File_Fragment;
import com.example.petpolite.Utilities.ClickedCallback;
import com.example.petpolite.Utilities.DialogShow;
import com.example.petpolite.Utilities.MyCallback;
import com.example.petpolite.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    List<Pet_Profile> pet_profileList;
    final int maxPet = 2;
    boolean doubleBackToExitPressedOnce = false;
    String userID;
    Pet_Profile pet1;
    Pet_Profile pet2;
    int petListSize;
    public static String selectedPetName;
    String p1, p2;
    Bundle bundlePet1;
    Bundle bundlePet2;
    Pet_Profile_Fragment pet_profile_fragment1;
    Pet_Profile_Fragment pet_profile_fragment2;
    Alert_Notifications_Fragment alert_notifications_fragment;
    public static int currentProfileLoaded = 0;
    public static String currentPetPrfURL;
    public static String isShowNoti;
    Upload_File_Fragment upload_file_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeStatusBar();
        SharedPreferences prefs = getSharedPreferences("AppState", MODE_PRIVATE);
        boolean isGuest = prefs.getBoolean("isGuest", false);
        if (!isGuest) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userID = user.getUid();
            Toast.makeText(this, "UserId: " + userID, Toast.LENGTH_SHORT).show();
        }
        //Get Data From Firebase and save in SQLite DB

        if (!isGuest) {
            pet1 = new Pet_Profile();
            pet2 = new Pet_Profile();
            pet_profileList = new ArrayList<>();
        }

        bundlePet1 = new Bundle();
        bundlePet2 = new Bundle();
        Bundle bundleUploadFragment = new Bundle();

        // set FragmentClass Arguments
        pet_profile_fragment1 = new Pet_Profile_Fragment();
        pet_profile_fragment2 = new Pet_Profile_Fragment();

        Home_Posts_Fragment home_posts_fragment = new Home_Posts_Fragment();
        alert_notifications_fragment = new Alert_Notifications_Fragment();
        Categories_Fragment categories_fragment = new Categories_Fragment();
        Search_Fragment search_fragment = new Search_Fragment();
        upload_file_fragment = new Upload_File_Fragment();


        database = FirebaseDatabase.getInstance();
        DatabaseReference node = database.getReference("/data/specificUserPets/" + userID + "/Pet_Profiles");
        if (!isGuest) {
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
                            p1 = pet1.getName_Pet();
                            bundlePet1.putString("coverPic", pet1.getCoverPhoto_pet());
                            bundlePet1.putString("profilePic", pet1.getProfilePhoto_pet());
                            bundlePet1.putString("petName", pet1.getName_Pet());
                            bundlePet1.putString("gender", pet1.getGender_Pet());
                            bundlePet1.putString("dateOfBirth", pet1.getDate_of_birth_Pet());
                            bundlePet1.putString("age", pet1.getAge());
                            bundlePet1.putString("breed", pet1.getBreed_Pet());
                            bundlePet1.putString("careNote", pet1.getCareNote_Pet());
                            bundlePet1.putString("userId", userID);
                            bundlePet1.putString("category", pet1.getCategory());
                            getUserName(new MyCallback() {
                                @Override
                                public void onCallback(String name) {
                                    bundlePet1.putString("userName", name);
                                }
                            });

                            pet2 = pet_profileList.get(1);
                            p2 = pet2.getName_Pet();
                            bundlePet2.putString("coverPic", pet2.getCoverPhoto_pet());
                            bundlePet2.putString("profilePic", pet2.getProfilePhoto_pet());
                            bundlePet2.putString("petName", pet2.getName_Pet());
                            bundlePet2.putString("gender", pet2.getGender_Pet());
                            bundlePet2.putString("dateOfBirth", pet2.getDate_of_birth_Pet());
                            bundlePet2.putString("age", pet2.getAge());
                            bundlePet2.putString("breed", pet2.getBreed_Pet());
                            bundlePet2.putString("careNote", pet2.getCareNote_Pet());
                            bundlePet2.putString("category", pet2.getCategory());

                            bundlePet2.putString("userId", userID);
                            getUserName(new MyCallback() {
                                @Override
                                public void onCallback(String name) {
                                    bundlePet2.putString("userName", name);
                                }
                            });

                        } else {
                            pet1 = pet_profileList.get(0);
                            p1 = pet1.getName_Pet();
                            bundlePet1.putString("coverPic", pet1.getCoverPhoto_pet());
                            bundlePet1.putString("profilePic", pet1.getProfilePhoto_pet());
                            bundlePet1.putString("petName", pet1.getName_Pet());
                            bundlePet1.putString("gender", pet1.getGender_Pet());
                            bundlePet1.putString("dateOfBirth", pet1.getDate_of_birth_Pet());
                            bundlePet1.putString("age", pet1.getAge());
                            bundlePet1.putString("breed", pet1.getBreed_Pet());
                            bundlePet1.putString("careNote", pet1.getCareNote_Pet());

                            bundlePet1.putString("userId", userID);
                            getUserName(new MyCallback() {
                                @Override
                                public void onCallback(String name) {
                                    bundlePet1.putString("userName", name);
                                }
                            });

                        }
                    } else {
                        Toast.makeText(MainActivity.this, "No Data Found On Database ", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        isShowNoti = NotificationReceiver.getIsShow();
        if (isShowNoti != null) {
            openNotificationFragment();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, home_posts_fragment).commit();
        }
        //Refreshing Profile Fragment
        try {
            Intent intent=getIntent();
            String refresh=intent.getStringExtra("refresh");
            if (refresh!=null || !refresh.contains("")){
                if (currentProfileLoaded==0 || currentProfileLoaded==1){
                    loadProfile_1();
                }else {
                    loadProfile_2();
                }
            }
        }catch (Exception e){
        }

        getData();

        binding.notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isGuest) {
                    if (currentProfileLoaded == 0) {
                        alert_notifications_fragment.setArguments(bundlePet1);
                    } else if (currentProfileLoaded == 1) {
                        alert_notifications_fragment.setArguments(bundlePet1);
                    } else if (currentProfileLoaded == 2) {
                        alert_notifications_fragment.setArguments(bundlePet2);
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, alert_notifications_fragment).commit();
                } else {
                    showDialogForSignup();
                }
            }
        });
        if (!isGuest) {
            binding.optionMenu.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(MainActivity.this, binding.optionMenu);
                popup.getMenuInflater().inflate(R.menu.option_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.create_profile_option:
                            if (maxPet == petListSize) {
                                DialogShow.showCustomDialogNoInternet("You Can Create Maximum 2 Pet Profiles !",
                                        "No More Pet Profile", MainActivity.this, new ClickedCallback() {
                                            @Override
                                            public void YesClicked() {
                                            }

                                            @Override
                                            public void NoClicked() {
                                            }
                                        });
                            } else {
                                Intent openMainActivity = new Intent(MainActivity.this, Create_Pet_Profile.class);
                                startActivity(openMainActivity);
                            }
                            return true;
                        case R.id.switchAccountID:
                            Toast.makeText(this, "Select Profile", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.pet_profile_1:
                            //open profile 1
                            loadProfile_1();
                            return true;
                        case R.id.pet_profile_2:
                            // open profile 2
                            if (petListSize < 2) {
                                DialogShow.showCustomDialogNoInternet("You do not have another pet Profile create it",
                                        "", MainActivity.this, new ClickedCallback() {
                                            @Override
                                            public void YesClicked() {
                                            }

                                            @Override
                                            public void NoClicked() {
                                            }
                                        });
                            } else {
                                loadProfile_2();
                            }
                            return true;
                        case R.id.logout_option:
                            logoutUser();
                            return true;
                    }
                    return false;
                });
                popup.show();

            });
        } else {
            showDialogForSignup();
        }

        binding.bnv.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.HomeItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, home_posts_fragment).commit();

                    return true;
                case R.id.searchItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, search_fragment).commit();
                    return true;
                case R.id.cameraItem:
                    if (!isGuest) {
                        if (currentProfileLoaded == 1) {
                            selectedPetName = bundlePet1.getString("petName");
                            currentPetPrfURL = bundlePet1.getString("profilePic");
                        } else if (currentProfileLoaded == 2) {
                            selectedPetName = bundlePet2.getString("petName");
                            currentPetPrfURL = bundlePet1.getString("profilePic");
                        }
                        getUserName(new MyCallback() {
                            @Override
                            public void onCallback(String name) {
                                bundleUploadFragment.putString("userName", name);
                            }
                        });

                        bundleUploadFragment.putString("userId", userID);
                        upload_file_fragment.setArguments(bundleUploadFragment);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, upload_file_fragment).commit();
                        return true;
                    } else {
                        showDialogForSignup();
                    }
                case R.id.categoriesItem:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, categories_fragment).commit();
                    return true;
                case R.id.profileItem:
                    if (!isGuest) {
                        if (currentProfileLoaded == 0) {
                            loadProfile_1();
                        } else if (currentProfileLoaded == 1) {
                            loadProfile_1();
                        } else if (currentProfileLoaded == 2) {
                            loadProfile_2();
                        }

                        return true;
                    } else {
                        showDialogForSignup();
                    }
            }
            return false;
        });

    }

    private void showDialogForSignup() {
        new AlertDialog.Builder(this)
                .setTitle("Sign Up")
                .setMessage("To Get All Features make sure to Signup First \nAre You Want to Sign Up ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, Sign_up.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("NO", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void getUserName(MyCallback myCallback) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference node = database.getReference("/data/Users/" + userID).child("name_user");
        node.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(String.class);
                myCallback.onCallback(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Double Back Press To Exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void logoutUser() {

        DialogShow.showCustomDialogLogout("Are You Sure To Logout",
                "Logout"
                , MainActivity.this, new ClickedCallback() {
                    @Override
                    public void YesClicked() {
                        FirebaseAuth mAuth;
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this, Login_Activity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void NoClicked() {
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatusBar() {
        Window window = MainActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.myBlue));
    }


    void loadProfile_1() {
        selectedPetName = bundlePet1.getString("petName");
        pet_profile_fragment1.setArguments(bundlePet1);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, pet_profile_fragment1).commit();
        currentProfileLoaded = 1;
    }

    void loadProfile_2() {
        selectedPetName = bundlePet2.getString("petName");
        pet_profile_fragment2.setArguments(bundlePet2);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, pet_profile_fragment2).commit();
        currentProfileLoaded = 2;
        Toast.makeText(this, "Switched To 2nd Pet Profile", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    void openNotificationFragment() {
        alert_notifications_fragment = new Alert_Notifications_Fragment();
        if (currentProfileLoaded == 0) {
            alert_notifications_fragment.setArguments(bundlePet1);
        } else if (currentProfileLoaded == 1) {
            alert_notifications_fragment.setArguments(bundlePet1);
        } else if (currentProfileLoaded == 2) {
            alert_notifications_fragment.setArguments(bundlePet2);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentId, alert_notifications_fragment).commit();

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void getData() {
        try {
            if (isNetworkConnected()) {
                PetCategories.deleteAll(PetCategories.class);
            }
            String Cats = "Cats";
            String Dogs = "Dogs";
            String Fishes = "Fishes";
            String Rabbits = "Rabbits";
            String Reptiles = "Reptiles";
            String Horses = "Horses";
            String Goats_Sheep = "Goats_Sheep";
            String Cows_Buffaloes = "Cows_Buffaloes";
            String Birds = "Birds";
            List<String> categories = new ArrayList<>();

            categories.add(Cats);
            categories.add(Dogs);
            categories.add(Fishes);
            categories.add(Rabbits);
            categories.add(Reptiles);
            categories.add(Horses);
            categories.add(Goats_Sheep);
            categories.add(Cows_Buffaloes);
            categories.add(Birds);
            for (int i = 0; i < categories.size(); i++) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/data/CategoriesPet/" + categories.get(i));
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            final List<PetCategories> categoriesDBList = new ArrayList<>();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                PetCategories pet_category = snapshot1.getValue(PetCategories.class);
                                categoriesDBList.add(pet_category);
                                Log.e("getData: ", categoriesDBList.toString());

                            }
                            for (PetCategories petCategories : categoriesDBList) {
                                petCategories.save();
                            }
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Error :" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }catch (Exception e){}
    }
}