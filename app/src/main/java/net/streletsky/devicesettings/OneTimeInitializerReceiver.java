package net.streletsky.devicesettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OneTimeInitializerReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        new DeviceController(context.getApplicationContext()).restoreSettings();
        //context.startService(new Intent(context, OneTimeInitializerService.class));
    }
}