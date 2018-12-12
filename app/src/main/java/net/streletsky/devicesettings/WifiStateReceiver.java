package net.streletsky.devicesettings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WifiStateReceiver extends BroadcastReceiver {
    WifiStateListener listener;

    public WifiStateReceiver(WifiStateListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        listener.onWifiStateChange();
    }

    public interface WifiStateListener {
        void onWifiStateChange();
    }
}
