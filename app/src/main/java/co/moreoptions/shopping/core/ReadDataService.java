package co.moreoptions.shopping.core;

/**
 * Created by anshul on 08/09/15.
 */

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityRecord;

import java.util.List;

import co.moreoptions.shopping.MainActivity;
import co.moreoptions.shopping.core.http.RequestListener;
import co.moreoptions.shopping.core.models.request.ValuesBatchModel;
import co.moreoptions.shopping.core.models.response.Product;
import co.moreoptions.shopping.core.models.response.ProductList;
import co.moreoptions.shopping.core.services.SendBatchService;


/**
 * This class demonstrates how an accessibility service can query
 * window content .
 */
public class ReadDataService extends AccessibilityService {

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "ReadDataService";

    /**
     * Comma separator.
     */
    private static final String SEPARATOR = ", ";

    private static ReadDataService sSharedInstance;

    private StringBuffer readDataString = new StringBuffer("");

    private int index;

    private ValuesBatchModel mValueBatchModel = new ValuesBatchModel();

    private ValueBatchHelper mValueBatchHelper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onServiceConnected() {
        sSharedInstance = this;
        mValueBatchHelper = new ValueBatchHelper();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT |
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS |
                AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |
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
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }
        String packageName = "";

        int count = source.getChildCount();
        packageName = source.getPackageName().toString();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo source1 = source.getChild(i);

            if (source1.getText() == null) {
                int count2 = source1.getChildCount();
                for (int j = 0; j < count2; j++) {
                    AccessibilityNodeInfo source2 = source1.getChild(j);
                    if (source2 != null && source2.getText() != null) {
                        Log.d(LOG_TAG, source2.getText().toString() + " " +
                                source2.getViewIdResourceName());
                        if (mValueBatchHelper.checkForBatchStart(source2.getViewIdResourceName())) {
                            mValueBatchHelper.setIsBatching(true);
                        }
                        if (mValueBatchHelper.isBatching() && source1.getText() != null) {
                            checkContent(packageName, source1.getViewIdResourceName(),
                                    source1.getText().toString());
                        }
                        readDataString.append(source2.getText().toString());
                    }
                }
            } else {
                Log.d(LOG_TAG, source1.getText().toString() + " " + source1.getViewIdResourceName());
                mValueBatchHelper.addValues(packageName, source1.getViewIdResourceName(),
                        source1.getText().toString(), index + "");
                readDataString.append(source1.getText().toString());
                if (mValueBatchHelper.checkForBatchStart(source1.getViewIdResourceName())) {
                    mValueBatchHelper.setIsBatching(true);
                }
                if (mValueBatchHelper.isBatching() && source1.getText() != null) {
                    checkContent(packageName, source1.getViewIdResourceName(),
                            source1.getText().toString());
                }
            }

        }
        if (source.getText() == null) {
            return;
        }
        CharSequence taskLabel = source.getText() + " " + source.getViewIdResourceName();


        String taskStr = taskLabel.toString();
        StringBuilder utterance = new StringBuilder(taskStr);

        // The custom ListView added extra context to the event by adding an
        // AccessibilityRecord to it. Extract that from the event and read it.
        final int records = event.getRecordCount();
        for (int i = 0; i < records; i++) {
            AccessibilityRecord record = event.getRecord(i);
            CharSequence contentDescription = record.getContentDescription();
            if (!TextUtils.isEmpty(contentDescription)) {
                utterance.append(SEPARATOR);
                utterance.append(contentDescription);
            }
        }

        Log.d(LOG_TAG, taskStr);
        if (mValueBatchHelper.checkForBatchStart(source.getViewIdResourceName())) {
            mValueBatchHelper.setIsBatching(true);
        }
        if (mValueBatchHelper.isBatching() && source.getText() != null) {
            checkContent(source.getPackageName().toString(), source.getViewIdResourceName(),
                    source.getText().toString());
        }
        readDataString.append(taskStr);


    }

    public ValueBatchHelper getValueBatchHelper() {
        return mValueBatchHelper;
    }

    @Override
    public void onInterrupt() {

    }

    private void checkContent(String packageName, String id, String value) {
        index++;
        mValueBatchHelper.addValues(packageName, id, value, index + "");
        if (index >= 50) {
            mValueBatchHelper.setShouldBatch(true);

            index = 0;

            SendBatchService sendBatchService = new SendBatchService();
            sendBatchService.postValues(mValueBatchHelper.getFinalRequestBody(), new RequestListener() {
                @Override
                public void onRequestFailure(Exception e) {
                    mValueBatchHelper.clearBatch();
                    Intent dialogIntent = new Intent(ReadDataService.this, MainActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialogIntent.putExtra("data", readDataString.toString());
                    startActivity(dialogIntent);
                }

                @Override
                public void onRequestSuccess(Object jsonObject) {
                    List<Product> products = (List<Product>) jsonObject;
                    ProductList productList = new ProductList();
                    productList.setProducts(products);
                    if(products.size()>0) {
                        mValueBatchHelper.clearBatch();
                        Intent dialogIntent = new Intent(ReadDataService.this, MainActivity.class);
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
    }

}
