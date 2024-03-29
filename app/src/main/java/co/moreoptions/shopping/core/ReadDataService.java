package co.moreoptions.shopping.core;

/**
 * Created by anshul on 08/09/15.
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.List;

import co.moreoptions.shopping.R;
import co.moreoptions.shopping.ThroughResultsActivity;
import co.moreoptions.shopping.Utils.AppConstants;
import co.moreoptions.shopping.core.http.RequestListener;
import co.moreoptions.shopping.core.models.response.Product;
import co.moreoptions.shopping.core.models.response.ProductList;
import co.moreoptions.shopping.core.services.SendBatchService;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;


/**
 * This class demonstrates how an accessibility service can query
 * window content.
 */
public class ReadDataService extends AccessibilityService implements FloatingViewListener, ServiceConnection{

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "ReadDataService";

    /**
     * Comma separator.
     */
    private static ReadDataService sSharedInstance;

    private ValueBatchHelper mValueBatchHelper;

    private WindowManager windowManager;
    private FloatingActionButton mFloatingActionButton;

    private HashMap<String, String> mIdProductMap;

    private FloatingViewManager mFloatingViewManager;

    private FloatingViewManager.Options options = new FloatingViewManager.Options();

    private FloatingHeadService mFloatingHeadService;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onServiceConnected() {
        sSharedInstance = this;
        mValueBatchHelper = new ValueBatchHelper();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT |
                AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS |
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sSharedInstance = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static ReadDataService getSharedInstance() {
        return sSharedInstance;
    }


    /**
     * Processes an AccessibilityEvent, by traversing the View's tree and
     * putting together a message to speak to the user.
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        final DisplayMetrics metrics = new DisplayMetrics();

        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }

//        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendValues();
//            }
//        });

        if (source.getText() == null) {
            return;
        }
        CharSequence taskLabel = source.getText() + " " + source.getViewIdResourceName();

        if (mValueBatchHelper.checkForBatchStart(source.getViewIdResourceName(),
                source.getPackageName().toString())) {

            mValueBatchHelper.clearBatch();
            startService(new Intent(getApplicationContext(), FloatingHeadService.class));

            mValueBatchHelper.setIsBatching(true);
            checkContent(source.getPackageName().toString(),
                    source.getText().toString());
        }

    }

    @Override
    public void onInterrupt() {

    }

    private void checkContent(String packageName, String value) {
        mValueBatchHelper.addValue(packageName, value);
    }

    public void sendValues() {
        mValueBatchHelper.setShouldBatch(true);
        SendBatchService sendBatchService = new SendBatchService();
        sendBatchService.postValues(mValueBatchHelper.getFinalRequestBody(), new RequestListener() {
            @Override
            public void onRequestFailure(Exception e) {
            }

            @Override
            public void onRequestSuccess(Object jsonObject) {
                List<Product> products = (List<Product>) jsonObject;
                ProductList productList = new ProductList();
                productList.setProducts(products);
                if (products.size() > 0) {
                    Intent dialogIntent = new Intent(ReadDataService.this, ThroughResultsActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("data", productList);
                    dialogIntent.putExtras(arguments);
                    startActivity(dialogIntent);
                }
            }
        });
        mValueBatchHelper.setIsBatching(false);
    }

    @Override
    public void onFinishFloatingView() {
        mFloatingViewManager.removeAllViewToWindow();
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
