package com.example.finalprojectshir2;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InternetConnectionReceiver extends BroadcastReceiver {

    String status="";
    public InternetConnectionReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
       //הפעולה מופעלת כשיש שינוי בחיבור לרשת, ואם אין אינטרנט - היא מציגה חלונית (Dialog) המתריעה למשתמש על כך.
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null)  {
            if (networkInfo.getType()==ConnectivityManager.TYPE_WIFI) {
                status="WiFi enabled";
            }
            if (networkInfo.getType()==ConnectivityManager.TYPE_MOBILE)  {
                status="Mobile enabled";
            }
        }
        else {
            status = "No internet is available";
            final Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.alert_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations= android.R.style.Animation_Dialog;
            TextView tvStatus=dialog.findViewById(R.id.tvStatus);
            tvStatus.setText(status);
            Button bOk=dialog.findViewById(R.id.bOk);//כפתור לסגירת דיאלוג
            bOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }



    }
}
