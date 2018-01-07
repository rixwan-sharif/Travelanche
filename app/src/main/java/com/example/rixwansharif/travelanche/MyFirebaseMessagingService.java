package com.example.rixwansharif.travelanche;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rixwan Sharif on 12/22/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public int N_id=0;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Firebaseidoio", "Service started " );
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        Log.i("Firebaseidoio", "Messge " + remoteMessage.getNotification().getBody());


       Notify(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody()
               , remoteMessage.getData().get("trip_id"), remoteMessage.getNotification().getClickAction());
    }


    public void Notify(String title,String body,String trip_id,String click_action) {

        Intent intent=new Intent(click_action);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // intent.putExtra("trip_id", trip_id);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //builder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_green_car_icon));
        builder.setContentTitle(title);
        builder.setContentText(body+":"+trip_id+":"+click_action);
        builder.setColor(101195);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(createID(),builder.build());

    }

    public int createID(){
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(now));
        return id;
    }

}
