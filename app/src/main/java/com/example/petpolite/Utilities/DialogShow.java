package com.example.petpolite.Utilities;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.petpolite.R;

public class DialogShow extends Dialog {

    public DialogShow(@NonNull Context context) {
        super(context);
    }

    public static void showConfirmationDialog(Context context, String title, String massage,ClickedCallback callback) {
        new AlertDialog.Builder(context)

                .setIcon(R.drawable.alert_dialog)
                .setTitle(title)
                .setMessage(massage)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        callback.YesClicked();
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.NoClicked();
                    }
                })
                .show();
    }
    public static void showCustomDialogNoInternet(String massage,String title,Context context1,ClickedCallback callback){
        Dialog dialog=new Dialog(context1,R.style.CustomAlert);

        View view = LayoutInflater.from(context1).inflate( R.layout.custom_nointernet_dialog,null, false);
        Button button=(Button) view.findViewById(R.id.No_Logout);
        TextView massage1=view.findViewById(R.id.massage);
        TextView title1=view.findViewById(R.id.title_NoInternet);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(580, WindowManager.LayoutParams.WRAP_CONTENT);
        try {
            massage1.setText(massage);
            title1.setText(title);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.YesClicked();
                    dialog.dismiss();

                }
            });
        }
        catch (Exception e){
            Toast.makeText(context1, ""+e, Toast.LENGTH_LONG).show();
        }
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void showCustomDialogLogout(String massage,String title,Context context1,ClickedCallback callback){
        Dialog dialog=new Dialog(context1,R.style.CustomAlert);

        View view = LayoutInflater.from(context1).inflate( R.layout.logout_dialog_laout,null, false);
        Button yesBtn=view.findViewById(R.id.yes_Logout);
        Button NoBtn= view.findViewById(R.id.No_Logout);
        TextView massage1=view.findViewById(R.id.massageLogout);
        TextView title1=view.findViewById(R.id.title_Logout);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(580, WindowManager.LayoutParams.WRAP_CONTENT);
        try {
            massage1.setText(massage);
            title1.setText(title);
            NoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.NoClicked();
                    dialog.dismiss();
                }

            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 /*  FirebaseAuth mAuth;
                   mAuth= FirebaseAuth.getInstance();
                   mAuth.signOut();
                   Intent intent=new Intent(context1, Login_Activity.class);
                   context1.startActivity(intent);*/
                    callback.YesClicked();
                }
            });

        }
        catch (Exception e){
            Toast.makeText(context1, ""+e, Toast.LENGTH_LONG).show();
        }
        dialog.setCancelable(false);
        dialog.show();
    }
}
