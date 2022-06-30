package com.example.petpolite.Classes;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.petpolite.MainActivity;
import com.example.petpolite.R;

public class NotificationReceiver extends BroadcastReceiver {
    static String title,massage,isShow;

    public static String getIsShow() {
        return isShow;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i=new Intent(context, MainActivity.class);
        isShow= intent.getStringExtra("isShow");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,i,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"MyChannel")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(massage)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(123,builder.build());

    }

    public static void setMassageAndTitle(String title1,String massage1){
        massage=massage1;
        title=title1+" Reminder";
    }

}
