package com.example.petpolite.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petpolite.Adapters.SearchAdapter;
import com.example.petpolite.Classes.PetCategories;
import com.example.petpolite.R;
import com.example.petpolite.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Search_Fragment extends Fragment implements AdapterView.OnItemSelectedListener {
    FragmentSearchBinding binding;
    boolean isCat, isFish, isRabbit, isReptile, isHorse, isGoat_Sheep, isCow_buffalo, isBird, isDog, isFilterSelected;
    String categoryFilter;
    RecyclerView recyclerView;
    List<PetCategories> resultList;
    SearchAdapter adapter;

    public Search_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        binding.notFoundText.setVisibility(View.INVISIBLE);

        binding.mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech();
            }
        });
        //get Data From SQLite DB in List
        resultList = new ArrayList<>();
        resultList = PetCategories.listAll(PetCategories.class);
        recyclerView = binding.recyclerView;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new SearchAdapter(getContext(), resultList);
        recyclerView.setAdapter(adapter);

        //Search View
        binding.searchEdt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredList(newText);
                return true;
            }
        });
        //spinner Code
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.Category,
                R.layout.category_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.categoryName.setAdapter(arrayAdapter);
        binding.categoryName.setOnItemSelectedListener(Search_Fragment.this);

        // Inflate the layout for this fragment
        return binding.getRoot();
    }



    private void filteredList(String Text) {
        List<PetCategories> filteredList = new ArrayList<>();
        for (PetCategories petCategories : resultList) {
            if (petCategories.getBreedName().toLowerCase().contains(Text.toLowerCase())) {
                filteredList.add(petCategories);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "Not Found", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                binding.notFoundText.setVisibility(View.VISIBLE);
            }, 2000);

        } else {
            binding.notFoundText.setVisibility(View.INVISIBLE);
            adapter.setFilteredList(filteredList);
            adapter.notifyDataSetChanged();
        }

    }

    void speech() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something");
        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "Google Text To Speech Not Found", Toast.LENGTH_SHORT).show();
            Log.e("speech: ", e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> resultSpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                binding.searchEdt.setQuery(resultSpeech.get(0), false);
                binding.searchEdt.clearFocus();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        categoryFilter = parent.getItemAtPosition(position).toString();

        if (categoryFilter.equals("Select")) {
            isFilterSelected = false;
        } else {
            isFilterSelected = true;
        }
        if (categoryFilter.equals("Cats")) {
            isCat = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Cats")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }
        } else {
            isCat = false;
        }
        if (categoryFilter.equals("Dogs")) {
            isDog = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Dogs")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }

        } else {
            isDog = false;
        }
        if (categoryFilter.equals("Fishes")) {
            isFish = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Fishes")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }
        } else {
            isFish = false;
        }
        if (categoryFilter.equals("Rabbits")) {
            isRabbit = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Rabbits")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }
        } else {
            isRabbit = false;
        }
        if (categoryFilter.equals("Reptiles")) {
            isReptile = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Reptiles")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }
        } else {
            isReptile = false;
        }
        if (categoryFilter.equals("Horses")) {
            isHorse = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Horses")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }
        } else {
            isHorse = false;
        }
        if (categoryFilter.equals("Goats_Sheep")) {
            isGoat_Sheep = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Goats_Sheep")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }
        } else {
            isGoat_Sheep = false;
        }
        if (categoryFilter.equals("Cows_Buffaloes")) {
            isCow_buffalo = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Cows_Buffaloes")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }
        } else {
            isCow_buffalo = false;
        }
        if (categoryFilter.equals("Birds")) {
            isBird = true;
            List<PetCategories> list = new ArrayList<>();
            for (PetCategories petCategories : resultList) {
                if (petCategories.getCategory().contains("Birds")) {
                    list.add(petCategories);
                }
            }
            if (list.isEmpty()) {
                Toast.makeText(getContext(), "List Not Found", Toast.LENGTH_SHORT).show();
            } else {
                adapter.setFilteredList(list);
            }
        } else {
            isBird = false;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getContext(), "Select Search Filter !", Toast.LENGTH_SHORT).show();
    }

}
