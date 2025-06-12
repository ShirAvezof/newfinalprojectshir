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

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction()))  {//האם מדובר בשינוי במצב החיבור לרשת.
            boolean noConnectivity=intent.getBooleanExtra(//בודק ואומר כן אם אין חיבור ולא אם יש חיבור
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (noConnectivity) {//אם אין חיבור מציג הודעה ואמור גם צליל
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