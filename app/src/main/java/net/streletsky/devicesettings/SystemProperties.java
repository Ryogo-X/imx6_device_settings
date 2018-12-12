package net.streletsky.devicesettings;

import java.lang.reflect.InvocationTargetException;

class SystemProperties {
    static String getProperty(String property) {
        try {
            return (String) Class.forName("android.os.SystemProperties").getMethod("get", new Class[] { String.class, String.class }).invoke(null, property, null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
