package net.streletsky.devicesettings;

import android.content.Context;
import android.content.SharedPreferences;

class Settings {
    private static final String CONFIG_ID = "settings";
    private static final String WIFI_STATE = "WIFI_STATE";
    private static final String FRONTLIGHT_STATE = "FRONTLIGHT_STATE";
    private static final String FRONTLIGHT_VALUE = "FRONTLIGHT_VALUE";
    private static final String COMFORTLIGHT_TEMP_VALUE = "COMFORTLIGHT_TEMP_VALUE";

    SharedPreferences settings;

    public Settings(Context context) {
        settings = context.getSharedPreferences(CONFIG_ID, Context.MODE_PRIVATE);
    }

    public boolean getWifiState() {
        return settings.getBoolean(WIFI_STATE, false);
    }

    public void setWifiState(boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(WIFI_STATE, value);
        editor.apply();
    }

    public boolean getFrontlightState() {
        return settings.getBoolean(FRONTLIGHT_STATE, true);
    }

    public void setFrontlightState(boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(FRONTLIGHT_STATE, value);
        editor.apply();
    }

    public int getFrontlightValue() {
        return settings.getInt(FRONTLIGHT_VALUE, 30);
    }

    public void setFrontlightValue(int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(FRONTLIGHT_VALUE, value);
        editor.apply();
    }

    public int getComfortlightTempValue() {
        return settings.getInt(COMFORTLIGHT_TEMP_VALUE, 0);
    }

    public void setComfortlightTempValue(int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(COMFORTLIGHT_TEMP_VALUE, value);
        editor.apply();
    }
}
