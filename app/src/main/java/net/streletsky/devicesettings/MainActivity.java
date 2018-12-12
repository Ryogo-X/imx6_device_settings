package net.streletsky.devicesettings;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.PopupWindow;

public class MainActivity extends Activity implements PopupWindow.OnDismissListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(android.R.id.content).post(new Runnable() {
            @Override
            public void run() {
                SettingsPopup popup = new SettingsPopup(getApplicationContext());
                popup.showAtLocation(findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
                popup.setOnDismissListener(MainActivity.this);
            }
        });
    }

    @Override
    public void onDismiss() {
        finish();
    }
}
