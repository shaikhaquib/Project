package com.completewallet.grocery.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by Shaikh Aquib on 21-Apr-18.
 */

public class NotiBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            context.startService(new Intent(context, MyNotificationService.class));
        } else{
            context.startForegroundService(new Intent(context, MyNotificationService.class));
        }
    }
}
