package com.example.petpolite.Classes;

import com.orm.SugarRecord;

public class PetCategories extends SugarRecord {
    String breedName;
    String description;
    String feeding;
    String grooming;
    String handling;
    String sleep_need;
    String housing;
    String photo;
    String category_Name;

    public String getCategory() {
        return category_Name;
    }

    public void setCategory(String category) {
        this.category_Name = category;
    }

    public PetCategories() {
    }

    public PetCategories(String breedName, String description, String feeding, String grooming, String handling, String sleep_need, String housing) {
        this.breedName = breedName;
        this.description = description;
        this.feeding = feeding;
        this.grooming = grooming;
        this.handling = handling;
        this.sleep_need = sleep_need;
        this.housing = housing;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeeding() {
        return feeding;
    }

    public void setFeeding(String feeding) {
        this.feeding = feeding;
    }

    public String getGrooming() {
        return grooming;
    }

    public void setGrooming(String grooming) {
        this.grooming = grooming;
    }

    public String getHandling() {
        return handling;
    }

    public void setHandling(String handling) {
        this.handling = handling;
    }

    public String getSleep_need() {
        return sleep_need;
    }

    public void setSleep_need(String sleep_need) {
        this.sleep_need = sleep_need;
    }

    public String getHousing() {
        return housing;
    }

    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
