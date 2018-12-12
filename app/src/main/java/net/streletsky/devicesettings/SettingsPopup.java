package net.streletsky.devicesettings;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsPopup extends PopupWindow
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, WifiStateReceiver.WifiStateListener {
    private Context ctx;
    private DeviceController controller;
    private WifiStateReceiver wifiStateReceiver;

    private ImageView wifiSettings;
    private CheckBox wifiState;
    private CheckBox glowlightState;
    private SeekBar glowlightValue;
    private SeekBar comfortlightValue;
    private TextView wifiSSIDValue;

    private Handler handler;
    private Runnable dismissRunnable;

    SettingsPopup(Context context) {
        super(context);

        ctx = context;
        controller = new DeviceController(context);
        handler = new Handler();
        dismissRunnable = new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        };

        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main, null);

        setContentView(view);

        Initialize();
        Refresh();
        Subscribe();
    }

    private void Initialize() {
        wifiSettings = getContentView().findViewById(R.id.wifiSettings);
        wifiState = getContentView().findViewById(R.id.wifiState);
        wifiSSIDValue = getContentView().findViewById(R.id.wifiSSID);
        glowlightState = getContentView().findViewById(R.id.glowlightState);
        glowlightValue = getContentView().findViewById(R.id.glowlightValue);
        glowlightValue.setMax(255);
        comfortlightValue = getContentView().findViewById(R.id.comfortlightValue);
        comfortlightValue.setMax(100);
        if (controller.hasComfortlight()) {
            getContentView().findViewById(R.id.comfortlightExpand).setVisibility(View.VISIBLE);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION );
        wifiStateReceiver = new WifiStateReceiver(this);
        ctx.getApplicationContext().registerReceiver(wifiStateReceiver, intentFilter);

        getContentView().setOnClickListener(this);
        scheduleDismiss();
    }

    private void Finalize() {
        try {
            ctx.getApplicationContext().unregisterReceiver(wifiStateReceiver);
        } catch (IllegalArgumentException ex) { }
    }

    void Refresh() {
        wifiState.setChecked(controller.getWiFiState());
        wifiSettings.setVisibility(wifiState.isChecked() ? View.VISIBLE : View.INVISIBLE);
        wifiSSIDValue.setVisibility(wifiState.isChecked() ? View.VISIBLE : View.INVISIBLE);
        wifiSSIDValue.setText(controller.getWifiSSID());
        glowlightState.setChecked(controller.getFrontlightState());
        glowlightValue.setProgress(controller.getFrontlightValue());
        comfortlightValue.setProgress(controller.getFrontlightColorTemp());
        glowlightValue.setEnabled(glowlightState.isChecked());
        comfortlightValue.setEnabled(glowlightState.isChecked());
    }

    void Subscribe() {
        wifiSettings.setOnClickListener(this);
        wifiState.setOnCheckedChangeListener(this);
        glowlightState.setOnCheckedChangeListener(this);
        glowlightValue.setOnSeekBarChangeListener(this);
        comfortlightValue.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        scheduleDismiss();

        switch (view.getId()) {
            case R.id.wifiSettings:
                openWiFiSettings();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean newState) {
        scheduleDismiss();

        switch (compoundButton.getId()) {
            case R.id.wifiState:
                updateWiFiState(newState);
                break;
            case R.id.glowlightState:
                updateGlowlightState(newState);
                break;
        }
    }

    private void updateWiFiState(boolean newState) {
        controller.setWiFiState(newState);
        wifiSettings.setVisibility(wifiState.isChecked() ? View.VISIBLE : View.INVISIBLE);
        wifiSSIDValue.setVisibility(wifiState.isChecked() ? View.VISIBLE : View.INVISIBLE);
        wifiSSIDValue.setText(controller.getWifiSSID());
    }

    private void updateGlowlightState(boolean newState) {
        controller.setFrontlightState(newState);
        glowlightValue.setEnabled(newState);
        comfortlightValue.setEnabled(newState);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int newValue, boolean b) {
        scheduleDismiss();

        switch (seekBar.getId()) {
            case R.id.glowlightValue:
                updateGlowlightValue(newValue);
                break;
            case R.id.comfortlightValue:
                updateComfortlightValue(newValue);
                break;
        }
    }

    private void updateGlowlightValue(int newValue) {
        controller.setFrontlightValue(newValue);
    }

    private void updateComfortlightValue(int newValue) {
        controller.setFrontlightColorTemp(newValue);
    }

    private void openWiFiSettings() {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
        dismiss();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    @Override
    public void dismiss() {
        super.dismiss();
        Finalize();
    }

    @Override
    public void onWifiStateChange() {
        wifiSSIDValue.setText(controller.getWifiSSID());
    }

    private void scheduleDismiss() {
        handler.removeCallbacks(dismissRunnable);
        handler.postDelayed(dismissRunnable, 5000);
    }
}