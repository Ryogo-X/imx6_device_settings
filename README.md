# Description
Simple application to manage imx6-based eInk readers settings.

Exactly:
 - WIFI;
 - Frontlight;
 - Comfortlight (changing screen color temperature);

# Installation
 **readers without *comfortlight***: install as usual.
 
**readers with *comfortlight***: since *comfortlight* control requires `android.permission.DEVICE_POWER` permission you would need to put `Imx6DeviceSettings.apk` into `/system/priv-app/`

# Known working devices
 - Nook Glowlight Plus;
 - Nook Glowlight 3;
 - Tolino Shine 2 HD;
 
 # Potentially working devices
 any imx6-based eInk reader which includes most *Tolino* ereaders, some of the *Onyx Boox* ereaders, some of the *InkBook* readers, etc...
