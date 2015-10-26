package co.moreoptions.shopping.CustomTabs;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import co.moreoptions.shopping.R;
import co.moreoptions.shopping.Utils.AppConstants;

/**
 * Created by aswani on 26/10/15.
 */
public class OpenWebsite extends Activity implements ServiceConnectionCallback {
    private static final String TAG = "CustomTabsClientExample";

    private Uri mProductUrl;
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;

    AppConstants appConstants = new AppConstants();
    String[] chromeAppPackages = {"com.android.chrome", "com.chrome.beta", "com.chrome.dev", "com.google.android.apps.chrome"};
    List<String> supportedAppPackageNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportedAppPackageNames = Arrays.asList(this.getResources().getStringArray(R.array.supported_app_packages_array));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mProductUrl = Uri.parse(extras.get(AppConstants.URL_TO_LAUNCH).toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bindCustomTabsService();

    }

    @Override
    public void onStop() {
        unbindCustomTabsService();
        super.onStop();
    }

    private void unbindCustomTabsService() {
        if (mConnection == null) return;
        unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
    }

    /**
     * we are binding our activity to custom tab service of chrome application in users phone
     * packagetoCheck will cycle through all possible packages of chrome android app
     * ServiceConnection our implimentation of CustomTabServiceConnection to avoid leaking service
     * customTabClient is a client to communicate with the CustomTabService
     * bindCustomTabService returns true when service gets binded to CustomTabService
     */
    private void bindCustomTabsService() {
        String packageToBind = null;
        for (String packageToCheck : chromeAppPackages) {
            if (!TextUtils.isEmpty(packageToCheck) && isAppInstalled(packageToCheck)) {
                packageToBind = packageToCheck;
                break;
            }
        }
        if (mClient != null) return;
        mConnection = new ServiceConnection(this);
        if (packageToBind != null) {
            if (CustomTabsClient.bindCustomTabsService(this, packageToBind, mConnection)) {
                if (mClient != null && mClient.warmup(0)) {
                    launch();
                    //    getSession().mayLaunchUrl(Uri.parse(url), null, null);
                    // TODO : get all the url's of this tab here to warmup the browsers this way no matter what url the user clicks it would be super fast
                }
            } else {
                mConnection = null;
            }
        }else {
            Toast.makeText(this,"packageToBind is null",Toast.LENGTH_LONG).show();
        }
    }


    private CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(new NavigationCallback());
        }
        return mCustomTabsSession;
    }

    public void launch() {
        String appToLaunch = null;
        boolean found = false;
        for (int i = 1; i <= 4; i++) {
            List<String> appList = appConstants.supportedAppList(i);
            for (int j = 0; j < appList.size(); j++) {
                if (mProductUrl.toString().toLowerCase().contains(appList.get(j).toLowerCase())) {
                    appToLaunch = appList.get(j).toLowerCase();
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        for (int i = 0; i < supportedAppPackageNames.size(); i++) {
            if (appToLaunch != null && supportedAppPackageNames.get(i).contains(appToLaunch)) {
                appToLaunch = supportedAppPackageNames.get(i);
            }
        }
        if (!isAppInstalled(appToLaunch)) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
            builder.setToolbarColor(Color.BLUE).setShowTitle(true);
            prepareMenuItems(builder);
            prepareActionButton(builder);
            // builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            // builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
            CustomTabsIntent customTabsIntent = builder.build();
            addKeepAliveExtra(this, customTabsIntent.intent);
            customTabsIntent.launchUrl(this, mProductUrl);
        }
    }

    private void prepareMenuItems(CustomTabsIntent.Builder builder) {
        Intent menuIntent = new Intent();
        menuIntent.setClass(getApplicationContext(), this.getClass());
        // Optional animation configuration when the user clicks menu items.
        //  Bundle menuBundle = ActivityOptions.makeCustomAnimation(this, android.R.anim.slide_in_left,
        //        android.R.anim.slide_out_right).toBundle();
        // PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, menuIntent, 0,
        //       menuBundle);
        //builder.addMenuItem("Menu entry 1", pi);
    }

    private void prepareActionButton(CustomTabsIntent.Builder builder) {
        // An example intent that sends an email.
        // Intent actionIntent = new Intent(Intent.ACTION_SEND);
        // actionIntent.setType("*/*");
        // actionIntent.putExtra(Intent.EXTRA_EMAIL, "example@example.com");
        // actionIntent.putExtra(Intent.EXTRA_SUBJECT, "example");
        // PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, actionIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_favorite);
        //   builder.setActionButton(icon, "send email", pi);
        builder.setActionButton(icon, null, null);
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = this.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public void addKeepAliveExtra(Context context, Intent intent) {
        Intent keepAliveIntent = new Intent().setClassName(
                context.getPackageName(), KeepAliveService.class.getCanonicalName());
        intent.putExtra(AppConstants.EXTRA_CUSTOM_TABS_KEEP_ALIVE, keepAliveIntent);
    }


    /**
     * Called when the service is connected.
     *
     * @param client a CustomTabsClient
     */
    @Override
    public void onServiceConnected(CustomTabsClient client) {
        mClient = client;
    }

    /**
     * Called when the service is disconnected.
     */
    @Override
    public void onServiceDisconnected() {
        mClient = null;
    }

    /**
     * this class gves the status of the custom tab launch with following codes
     * NAVIGATION_STARTED = 1;
     * NAVIGATION_FINISHED = 2;
     * NAVIGATION_FAILED = 3;
     * NAVIGATION_ABORTED = 4;
     */

    private static class NavigationCallback extends CustomTabsCallback {
        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            Log.w(TAG, "onNavigationEvent: Code = " + navigationEvent);
        }
    }
}
