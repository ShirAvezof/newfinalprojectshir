package com.example.finalprojectshir2;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class BackgroundMusicService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        // הפעלת קובץ מוזיקה  -
        mediaPlayer = MediaPlayer.create(this, R.raw.parents_music);
        mediaPlayer.setLooping(true); // השמעה בלולאה
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        return START_STICKY; //  ימשיך גם אם ייהרס ויופעל מחדש
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // לא מחובר לשירות חיצוני שיכול להרוס את המוזיקה
    }
}
