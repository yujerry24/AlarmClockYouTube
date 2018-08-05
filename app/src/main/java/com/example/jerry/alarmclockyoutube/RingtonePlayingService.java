package com.example.jerry.alarmclockyoutube;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class RingtonePlayingService extends Service {

    MediaPlayer mediaSong;
    boolean isRunning;
    int startId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {

        Log.e("hello?", "hey");

        String state = intent.getExtras().getString("extra");

        Log.e("Ringstone state: extra is ", state);

       /* NotificationManager notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



        Intent mainActivity = new Intent (this.getApplicationContext(), MainActivity.class);

        PendingIntent pendMainActivity = PendingIntent.getActivity(this,0,mainActivity,0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Your alarm is going off!")
                .setContentText("Click here!")
                .setSmallIcon(R.drawable.notif)
                .setContentIntent(pendMainActivity)
                .setAutoCancel(true)
                .build();*/

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this.getApplicationContext(), "notify_001");
        Intent ii = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();

        bigText.setBigContentTitle("Today's Bible Verse");
        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1) {
            Log.e("There is no music, " , " and you want start");
            mediaSong = MediaPlayer.create(this, R.raw.thebeach);
            mediaSong.start();

            this.isRunning=true;
            this.startId=0;
            //notifyManager.notify(0,notification);
        }
        else if (this.isRunning && startId == 0) {
            Log.e("There is music, " , " and you want end");

            mediaSong.stop();
            mediaSong.reset();

            this.isRunning=false;
            this.startId=0;

        }
        else if (!this.isRunning && startId == 0) {
            Log.e("There is no music, " , " and you want end");
                this.isRunning=false;
                this.startId=0;
        }
        else if (this.isRunning && startId == 1) {
            Log.e("There is music, " , " and you want start");
            this.startId=1;
            this.isRunning=true;

        }
        else {
            Log.e("How the hell did you get here ", "???");

        }

        return START_NOT_STICKY;

    };

    @Override
    public void onDestroy() {
        Log.e("on destroy called", "ta da");
        Toast.makeText(this, "on destroy called", Toast.LENGTH_SHORT).show();
    }
}
