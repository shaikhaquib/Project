package com.abatechnology.kirana2door.Service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.abatechnology.kirana2door.Activity.Category;
import com.abatechnology.kirana2door.Activity.Global;
import com.abatechnology.kirana2door.Connecttodb;
import com.abatechnology.kirana2door.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MyNotificationService extends Service {




	String orderstus;
	String uid;
	String fetch_data="true";
    int shid;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mTimer = new Timer();
		mTimer.schedule(timerTask, 2000, 5 * 1000);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

	/*	sessionId=intent.getStringExtra("sid");
		UserId=intent.getStringExtra("uid");
		msg_type=intent.getStringExtra("mt");
		fetch_data=intent.getStringExtra("fd");*/


        return super.onStartCommand(intent, flags, startId);
	}

	private Timer mTimer;

	TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
            SharedPreferences shared =getSharedPreferences("login", MODE_PRIVATE);
            Global.email = shared.getString( "email", "");
            new AsyncTest().execute(Global.email);
		}
	};

    @Override
    public void onDestroy() {
        super.onDestroy();
		try {
			mTimer.cancel();
			timerTask.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    private class AsyncTest  extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL(Connecttodb.path+"orderhistory");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(Global.READ_TIMEOUT);
                conn.setConnectTimeout(Global.CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            } finally {
                conn.disconnect();
            }


        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            String id;
            String title;
            String subject;
            String status;
            String noticount = null;

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String restoredText = prefs.getString("id", null);
            orderstus=prefs.getString("status",null);
            if (restoredText != null) {
                uid = prefs.getString("id", "0");
                Log.d("last id",uid);//"No name defined" is the default value.
            }

            if (s != null && !s.equals("unsuccessful")){

                try {


                    JSONArray array=new JSONArray(s);

                    final int numberOfItemsInResp = array.length();

                    for ( int i = 0 ; i < numberOfItemsInResp ; i++){
                        JSONObject jsonObject =array.getJSONObject(i);

                        id = jsonObject.getString("id");
                        status = jsonObject.getString("order_status");
                        int iid = Integer.parseInt(id);
                        if (uid != null){
                         shid = Integer.parseInt(uid);}



                        if (iid >shid){

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("id", id);
                        editor.putString("status",status);
                        editor.apply();

                        title=jsonObject.getString("order_id");
                        subject=jsonObject.getString("order_status");



                            NotificationCompat.BigTextStyle style=new NotificationCompat.BigTextStyle();
                            style.bigText(subject);
                            style.setBigContentTitle(title);
                            style.setSummaryText("K2D");

                            Global.notiCount++;

                            Intent intent =new Intent(getApplicationContext(),Category.class);
                            intent.setAction("Notify");

                            NotificationCompat.Builder nbuilder=(NotificationCompat.Builder)new NotificationCompat.Builder(MyNotificationService.this)
                                    .setSmallIcon(R.drawable.kiranaicon)
                                    .setContentTitle("Order "+title)
                                    .setContentText("Your order has been "+subject)
                                    .setAutoCancel(true)
                                    .setStyle(style).setBadgeIconType(R.drawable.kiranaicon)
                                    .setPriority(Notification.PRIORITY_MAX)
                                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.kiranaicon))
                                    .setSmallIcon(R.drawable.kiranaicon)
                                    .setGroup("K2D")
                                    .setTicker("K2D");



                            PendingIntent pendingIntent =PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                            nbuilder.setContentIntent(pendingIntent);

                            long[] vibrate = { 0, 100, 200, 300 };
                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            nbuilder.setSound(alarmSound);
                            nbuilder.setVibrate(vibrate);

                            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(0,nbuilder.build());

                        }else if (iid == shid && !status.equals(orderstus)){

                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                            editor.putString("id", id);
                            editor.putString("status",status);
                            editor.apply();

                            title=jsonObject.getString("order_id");
                            subject=jsonObject.getString("order_status");

                            Global.notiCount++;


                            NotificationCompat.BigTextStyle style=new NotificationCompat.BigTextStyle();
                            style.bigText(subject);
                            style.setBigContentTitle(title);
                            style.setSummaryText("K2D" );


                            Intent intent =new Intent(getApplicationContext(),Category.class);
                            intent.setAction("Notify");

                            NotificationCompat.Builder nbuilder=(NotificationCompat.Builder)new NotificationCompat.Builder(MyNotificationService.this)
                                    .setSmallIcon(R.drawable.kiranaicon)
                                    .setContentTitle("Order "+title)
                                    .setContentText("Your order has been "+subject)
                                    .setAutoCancel(true)
                                    .setStyle(style).setBadgeIconType(R.drawable.kiranaicon)
                                    .setPriority(Notification.PRIORITY_MAX)
                                    .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.kiranaicon))
                                    .setSmallIcon(R.drawable.kiranaicon)
                                    .setGroup("K2D")
                                    .setTicker("K2D");



                            PendingIntent pendingIntent =PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                            nbuilder.setContentIntent(pendingIntent);

                            long[] vibrate = { 0, 100, 200, 300 };
                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            nbuilder.setSound(alarmSound);
                            nbuilder.setVibrate(vibrate);

                            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(0,nbuilder.build());



                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }}
