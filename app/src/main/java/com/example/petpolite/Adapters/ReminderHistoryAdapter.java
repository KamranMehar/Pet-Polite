package com.example.petpolite.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
;import com.example.petpolite.Classes.ReminderClass;
import com.example.petpolite.R;

import java.util.List;

public class ReminderHistoryAdapter extends RecyclerView.Adapter<ReminderHistoryAdapter.ViewHolder> {
    List<ReminderClass> reminderList;

    public ReminderHistoryAdapter(List<ReminderClass> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_history_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReminderClass reminderClass=reminderList.get(position);
        holder.time.setText(reminderClass.getDate_Time());
        holder.massage.setText(reminderClass.getMassageReminder());
        holder.petName.setText(reminderClass.getPetNameReminder());
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView petName,massage,time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            petName=itemView.findViewById(R.id.petNameHistoryItem);
            massage=itemView.findViewById(R.id.massageHistoryItem);
            time=itemView.findViewById(R.id.timeHistoryItem);
        }
    }
}
