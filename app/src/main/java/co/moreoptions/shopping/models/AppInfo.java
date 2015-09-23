package co.moreoptions.shopping.models;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import co.moreoptions.shopping.Utils.ApplicationMode;
import co.moreoptions.shopping.app.MOLog;

/**
 * All information related to the application is handeled by this class
 */
public class AppInfo {
    private static final String LOGTAG = AppInfo.class.getSimpleName();

    public final static String APP_VERSION_KEY = "version";
    public final static String APP_VERSION_KEY_HEADER = "app_version";
    public static final String USER_AGENT_KEY = "User-Agent";
//    private static final String USER_AGENT_DELIMITER = "/";

    public static int sVersionCode;
    public static String sVersionName;
    public static String sUserAgent;
    public static String sAppSignature;
  //  public static ApplicationMode sRunningMode = BuildConfiguration.sAppMode; // TODO : Rahul create BuildConfiguration to get Running more

    private static AppInfo sInstance;

    private Context mContext;

    public static AppInfo getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppInfo(context);
        }
        return sInstance;
    }

    private AppInfo(Context context) {
        mContext = context;
    }

    public void init() {

      //  Utils.assertNotInMainThread();
        MOLog.v("Loading up app info");

        try {
            PackageInfo packageInfo
                    = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            sVersionCode = packageInfo.versionCode;
            sVersionName = packageInfo.versionName;

            packageInfo
                    = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] sigs = packageInfo.signatures;
            if (sigs != null && sigs.length > 0) {
                sAppSignature = sigs[0].toCharsString();
            } else {
                MOLog.e("Failed to detect app signature!");
            }

        } catch (PackageManager.NameNotFoundException e) {
            MOLog.wtf("Package Manager can't find our own package!");
        }
    }

    // TODO : Anshul need Aplication class to fix this code
   /* public String getUserAgent() {
        if (sUserAgent == null) {
            DeviceInfo di = (() mContext).getDataManager().getDeviceInfo();
            StringBuffer sb = new StringBuffer("MoreOptionsApp").append(USER_AGENT_DELIMITER)
                    .append(sVersionName).append(" (")
                    .append(di.getOsType()).append(USER_AGENT_DELIMITER)
                    .append(di.getOsVersion()).append(")");
            sUserAgent = sb.toString();
        }
        return sUserAgent;
    }*/
}
