package co.moreoptions.shopping.models;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import co.moreoptions.shopping.app.MOLog;

/**
 * Contains all data related to the device.
 */
public class DeviceInfo {

    private static final String LOGTAG = DeviceInfo.class.getSimpleName();

    public static final String CLIENT_ID_KEY = "client";
    public static final String DEVICE_ID_KEY = "device_id";
    public static final String ANDROID_ID_KEY = "android_id";
    public static final String DEVICE_IDENTIFIER_KEY = "device_identifier";
    public static final String OS_TYPE_KEY = "os_type";
    public static final String OS_TYPE_KEY_HEADER = "os";
    public static final String OS_VERSION_KEY = "os_version";
    public static final String PHONE_MODEL_KEY = "phone_model";
    public static final String DEVICE_RESOLUTION_KEY = "resolution";
    public static final String NETWORK_TYPE_KEY = "network_status";

    private static DeviceInfo sInstance;
    private Context mContext;

    private static String sDeviceId;
    private static String sDeviceIdentifier;
    private static String sAndroidVersion;
    private static String sPhoneModel;
    private long mDeviceMemClass;
    private String mScreenSize;
    private int mScreenWidth;

    public static String DEVICE_MODEL = android.os.Build.MODEL;

    public static DeviceInfo getInstance(Context c) {
        if (sInstance == null) {
            sInstance = new DeviceInfo(c);
        }
        return sInstance;
    }

    private DeviceInfo(Context c) {
        mContext = c;
    }

    public void init() {
     //   Utils.assertNotInMainThread();
        MOLog.v("Loading up device info");
        sDeviceId = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if(TextUtils.isEmpty(sDeviceId)) {
            sDeviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        sDeviceIdentifier = computeDeviceIdentifier();
        sAndroidVersion = Build.VERSION.RELEASE;
        sPhoneModel = Build.MODEL;
        mDeviceMemClass = 1024 * 1024 * ((ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

        mScreenSize = getScreenSizeFromDensity(mContext.getResources().getDisplayMetrics().densityDpi);

        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
    }

    public String getDeviceId() {
        return sDeviceId;
    }

    public String getDeviceIdentifier() {
        return sDeviceIdentifier;
    }

    public String getOsType() {
        return "android";
    }

    public String getOsVersion() {
        return sAndroidVersion;
    }

    public String getPhoneModel() {
        return sPhoneModel;
    }

    /**
     * Return memory size of the device
     * @return
     */
    public long getDeviceMemSize() {
        return mDeviceMemClass;
    }

    /**
     * Returns screen size of the device - one of xxhdpi, xhdpi, hdpi, mdpi or ldpi.
     */
    public String getScreenSize() {
        return mScreenSize;
    }

    private String getScreenSizeFromDensity(int density) {

        String screenSize;
        if (density== DisplayMetrics.DENSITY_LOW) {
            screenSize = "ldpi";
        } else if (density == DisplayMetrics.DENSITY_MEDIUM) {
            screenSize = "mdpi";
        } else if (density ==DisplayMetrics.DENSITY_HIGH) {
            screenSize = "hdpi";
        } else if (density == DisplayMetrics.DENSITY_XHIGH) {
            screenSize = "xhdpi";
        } else if (density == DisplayMetrics.DENSITY_XXHIGH) {
            screenSize = "xxhdpi";
        } else {
            screenSize = "xhdpi"; //Default
        }
        return screenSize;
    }

    private String computeDeviceIdentifier() {
        String serial = getFullDeviceId(getInternalDeviceId());
        try {
            serial = Base64.encodeToString(serial.getBytes(), Base64.NO_PADDING | Base64.URL_SAFE).toUpperCase();
        } catch(Exception ex) {
            MOLog.e(ex, "Error generating ID");
        }
        return serial;
    }

    private String getInternalDeviceId() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        }
        catch (Exception ignored) {
            MOLog.e(ignored, "Error getting id");
        }
        return serial;
    }

    private String getDeviceIdFromWifiManager() {
        WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if(wm != null) {
            return wm.getConnectionInfo().getMacAddress();
        } else {
            return null;
        }
    }


//    private String getDeviceIdFromBluetoothManager() {
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//        if(adapter != null) {
//            return adapter.getAddress();
//        } else {
//            return null;
//        }
//    }

    private String getFullDeviceId(String roId) {
        StringBuilder buff = new StringBuilder();
        if(roId != null) {
            buff.append(roId);
            buff.append("$$");
        }
        String wifiMac = getDeviceIdFromWifiManager();
        if(wifiMac != null) {
            buff.append(wifiMac);
            //buff.append("$$");
        }
//        String bluetoothAddress = getDeviceIdFromBluetoothManager();
//        if(bluetoothAddress != null) {
//            buff.append(bluetoothAddress);
//        }
        return buff.toString();
    }

    public static String getAndroidVersion() {
        String sdkName = "";

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            try {
                int fieldValue = field.getInt(new Object());

                if (fieldValue == Build.VERSION.SDK_INT) {
                    sdkName = fieldName;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(sdkName)) {
            builder.append(" ");
            builder.append(sdkName);
            builder.append(" ");
        }
        builder.append(Build.VERSION.RELEASE);
        builder.append(" - ");
        builder.append(" ");
        builder.append(Build.VERSION.SDK_INT);

        return builder.toString();
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }
}
