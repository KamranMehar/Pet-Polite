package com.example.petpolite.Classes;


import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class ReminderClass extends SugarRecord{
    @Unique
    String petNameReminder;
    String massageReminder;
    String date_Time;
    public ReminderClass(String petNameReminder, String massageReminder, String date_Time) {
        this.petNameReminder = petNameReminder;
        this.massageReminder = massageReminder;
        this.date_Time = date_Time;
    }

    public ReminderClass() {
    }

    public String getPetNameReminder() {
        return petNameReminder;
    }

    public void setPetNameReminder(String petNameReminder) {
        this.petNameReminder = petNameReminder;
    }

    public String getMassageReminder() {
        return massageReminder;
    }

    public void setMassageReminder(String massageReminder) {
        this.massageReminder = massageReminder;
    }

    public String getDate_Time() {
        return date_Time;
    }

    public void setDate_Time(String date_Time) {
        this.date_Time = date_Time;
    }
}
