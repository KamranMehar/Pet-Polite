package com.example.petpolite.Fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.petpolite.Adapters.ReminderHistoryAdapter;
import com.example.petpolite.Classes.NotificationReceiver;
import com.example.petpolite.Classes.ReminderClass;
import com.example.petpolite.MainActivity;
import com.example.petpolite.databinding.FragmentAlertNotificationsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Alert_Notifications_Fragment extends Fragment {
    FragmentAlertNotificationsBinding binding;
    String petName;
    int DAY, MONTH, YEAR;
    int HOUR, MINUTE, AM_PM;
    Calendar calendar;
    String am_pm;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    String massage;
    boolean isDateSet = false;
    boolean isTimeSet = false;
    ReminderClass reminderClass;
    List<ReminderClass> reminderHistoryList = new ArrayList<>();

    public Alert_Notifications_Fragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAlertNotificationsBinding.inflate(inflater, container, false);


        calendar = Calendar.getInstance();
        AM_PM = Calendar.AM_PM;
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            am_pm = "AM";
        } else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
            am_pm = "PM";
        }
        HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        MINUTE = calendar.get(Calendar.MINUTE);
        binding.dayText.setText("" + calendar.get(Calendar.DAY_OF_MONTH));
        binding.monthText.setText(calendar.get(Calendar.MONTH) + "");
        binding.yearText.setText(calendar.get(Calendar.YEAR) + "");
        binding.hourText.setText(calendar.get(Calendar.HOUR) + "");
        binding.mintText.setText(calendar.get(Calendar.MINUTE) + "");
        binding.amPmText.setText(am_pm);
      //  Toast.makeText(getContext(), "" + MainActivity.isShowNoti, Toast.LENGTH_SHORT).show();
        ToDoData();

        //setting Chanel
        CharSequence channelName = "MyChannel";
        String description = "Channel Description";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("MyChannel", channelName, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        petName = getArguments().getString("petName");
        binding.pickDate.setOnClickListener(view -> {
            openDatePickerDialog();
        });

        binding.pickTime.setOnClickListener(view -> openTimePickerDialog());

        binding.setReminderBtn.setOnClickListener(view -> {
            setReminder();
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void ToDoData() {
        String check=MainActivity.isShowNoti;
        if (check != null) {
            binding.petNameTodo.setVisibility(View.INVISIBLE);
            binding.reminderMassage.setVisibility(View.INVISIBLE);
            binding.reminderTime.setVisibility(View.INVISIBLE);
            deletePrefData();
            Toast.makeText(getContext(), "Notification Received", Toast.LENGTH_SHORT).show();
        } else {
            binding.ToDoReminderLayer.setVisibility(View.VISIBLE);
            SharedPreferences pref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            binding.petNameTodo.setText(pref.getString("petName", ""));
            binding.reminderMassage.setText(pref.getString("massage", ""));
            binding.reminderTime.setText(pref.getString("time", ""));
        }
        try {
            reminderHistoryList = ReminderClass.listAll(ReminderClass.class);
        } catch (Exception e) {
            Log.e("showHistory: ", e.getMessage());
        }
        if (reminderHistoryList != null) {
            ReminderHistoryAdapter adapter = new ReminderHistoryAdapter(reminderHistoryList);
            RecyclerView recyclerView = binding.historyRecyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(adapter);
            Log.e("ToDoData: ", "Reminder List is Empty" + reminderHistoryList);
        }

    }

    private void deletePrefData() {
        SharedPreferences pref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("petName");
        editor.remove("massage");
        editor.remove("time");
        editor.apply();
    }

    @SuppressLint({"SetTextI18n", "UnspecifiedImmutableFlag"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setReminder() {

        massage = binding.massage.getText().toString();
        if (massage.isEmpty()) {
            binding.massage.setError("Remind Massage should Not Empty");
            Toast.makeText(getContext(), "Remind Massage should Not Empty", Toast.LENGTH_SHORT).show();
        } else {
            massage = binding.massage.getText().toString();
            NotificationReceiver.setMassageAndTitle(petName, massage);

        }
        if (!isDateSet) {
            Toast.makeText(getContext(), "Select Date ", Toast.LENGTH_SHORT).show();
        }
        if (!isTimeSet) {
            Toast.makeText(getContext(), "Select Time ", Toast.LENGTH_SHORT).show();
        }
        if (isTimeSet && isTimeSet && !massage.isEmpty()) {

            alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), NotificationReceiver.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("isShow", "Notification");

            pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

            Toast.makeText(getContext(), "Reminder Set Successfully", Toast.LENGTH_SHORT).show();
            binding.massage.getText().clear();

            savePref(petName, massage, HOUR + ":" + MINUTE + ":" + am_pm + "  " + DAY + "/" + MONTH + "/" + YEAR);
        } else {
            Toast.makeText(getContext(), "Reminder Not Set !", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePref(String petName, String massage, String time) {
        SharedPreferences pref = requireActivity().getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("petName", petName);
        editor.putString("massage", massage);
        editor.putString("time", time);
        editor.apply();
        binding.reminderMassage.setText(massage);
        binding.petNameTodo.setText(petName);
        binding.reminderTime.setText(time);
        try {
            reminderClass = new ReminderClass(petName, massage, time);
            reminderClass.save();

        }catch (Exception e){
            Log.e("onCreateView: ",e.getMessage() );
        }
    }

    private void openTimePickerDialog() {

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                int h = 0;
                MINUTE = minute;
                if (hour < 12) {
                    h = hour;
                    HOUR = h;
                    binding.hourText.setText(hour + "");
                    binding.amPmText.setText("AM");
                } else if (hour > 12) {
                    h = hour - 12;
                    HOUR = h;
                    binding.amPmText.setText("PM");
                }
                Toast.makeText(getContext(), h+":"+minute, Toast.LENGTH_SHORT).show();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                binding.hourText.setText(h + "");
                binding.mintText.setText(minute + "");
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
        isTimeSet = true;
    }

    @SuppressLint("SetTextI18n")
    private void openDatePickerDialog() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
            binding.dayText.setText(day + "");
            convertMonthNoToName(month);

            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);

            binding.yearText.setText(year + "");
            DAY = day;
            MONTH = month + 1;
            YEAR = year;
            Toast.makeText(getContext(), DAY + "/" + MONTH + "/" + YEAR, Toast.LENGTH_SHORT).show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        isDateSet = true;
    }

    @SuppressLint("SetTextI18n")
    void convertMonthNoToName(int m) {
        if (m == 0) {
            binding.monthText.setText("Jan");
        }
        if (m == 1) {
            binding.monthText.setText("Feb");
        }
        if (m == 2) {
            binding.monthText.setText("Mar");
        }
        if (m == 3) {
            binding.monthText.setText("Apr");
        }
        if (m == 4) {
            binding.monthText.setText("May");
        }
        if (m == 5) {
            binding.monthText.setText("Jun");
        }
        if (m == 6) {
            binding.monthText.setText("Jul");
        }
        if (m == 7) {
            binding.monthText.setText("Aug");
        }
        if (m == 8) {
            binding.monthText.setText("Sep");
        }
        if (m == 9) {
            binding.monthText.setText("Oct");
        }
        if (m == 10) {
            binding.monthText.setText("Nov");
        }
        if (m == 11) {
            binding.monthText.setText("Dec");
        }
    }
}