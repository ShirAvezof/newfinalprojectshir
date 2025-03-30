package com.example.finalprojectshir2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

    MediaPlayer mp;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))  {
            boolean noConnectivity=intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {
                Toast.makeText(context, "Network Disconnected", Toast.LENGTH_LONG).show();
                mp=MediaPlayer.create(context, R.raw.ok);
                mp.start();
            }
            else {
                Toast.makeText(context, "Network Connected", Toast.LENGTH_LONG).show();
                mp=MediaPlayer.create(context, R.raw.no);
                mp.start();
            }

        }
    }

}