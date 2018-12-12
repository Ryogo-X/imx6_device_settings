package net.streletsky.devicesettings;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.provider.Settings;

import java.lang.reflect.InvocationTargetException;

class DeviceController {
    private Context ctx;
    private net.streletsky.devicesettings.Settings settings;

    DeviceController(Context ctx) {
        this.ctx = ctx;
        settings = new net.streletsky.devicesettings.Settings(ctx);
    }

    boolean hasComfortlight() {
        String value = SystemProperties.getProperty("ro.hw.ctm");
        if (value.equals("yes")) { return true; }

        value = SystemProperties.getProperty("hw.CTM.dev");

        return value != null && !value.equals("");
    }

    boolean getWiFiState() {
        WifiManager manager = (WifiManager)ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return manager.isWifiEnabled();
    }

    void setWiFiState(boolean newState) {
        WifiManager manager = (WifiManager)ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        manager.setWifiEnabled(newState);
        settings.setWifiState(newState);
    }

    String getWifiSSID() {
        WifiManager manager = (WifiManager) ctx.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();

        String ssid = info.getSSID();

        return ssid != null && !ssid.equals("0x") ? ssid : "not connected";
    }

    boolean getFrontlightState() {
        boolean on = false;

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        try {
            on = (Boolean) pm.getClass().getMethod("isFrontLightOn", new Class[0]).invoke(pm);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            return getFrontlightValue() > 0;
        }

        return on;
    }

    void setFrontlightState(boolean on) {
        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        try {
            pm.getClass().getMethod("setFrontLightState", new Class[]{Boolean.TYPE}).invoke(pm, on);
        } catch (IllegalArgumentException | SecurityException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            try {
                pm.getClass().getMethod("setFrontLightState", new Class[]{Boolean.TYPE, Boolean.TYPE}).invoke(pm, on, Boolean.FALSE);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        try {
            pm.getClass().getMethod("updateFrontLightStatusBarIcon", new Class[0]).invoke(pm);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        settings.setFrontlightState(on);
    }

    int getFrontlightValue() {
        return android.provider.Settings.System.getInt(ctx.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, -1);
    }

    void setFrontlightValue(int value) {
        android.provider.Settings.System.putInt(ctx.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, value);
        settings.setFrontlightValue(value);
    }

    int getFrontlightColorTemp() {
        return 100 - Settings.System.getInt(ctx.getContentResolver(), "creen_brightness_color", 0);
    }

    void setFrontlightColorTemp(int temp) {
        int normalizedTemp = 100 - temp;
        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        try {
            pm.getClass().getMethod("setFrontlightBrightnessColor", new Class[]{Integer.TYPE}).invoke(pm, normalizedTemp);
            Settings.System.putInt(ctx.getContentResolver(), "creen_brightness_color", normalizedTemp);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        settings.setComfortlightTempValue(temp);
    }

    void restoreSettings() {
        setWiFiState(settings.getWifiState());
        setFrontlightState(settings.getFrontlightState());
        setFrontlightValue(settings.getFrontlightValue());
        setFrontlightColorTemp(settings.getComfortlightTempValue());
    }
}