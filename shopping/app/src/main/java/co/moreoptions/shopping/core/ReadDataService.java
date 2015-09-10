package co.moreoptions.shopping.core;

/**
 * Created by anshul on 08/09/15.
 */

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityRecord;
import android.widget.Toast;

import java.util.Locale;

import co.moreoptions.shopping.MainActivity;


/**
 * This class demonstrates how an accessibility service can query
 * window content .
 */
public class ReadDataService extends AccessibilityService implements TextToSpeech.OnInitListener {

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "ReadDataService/onAccessibilityEvent";

    /**
     * Comma separator.
     */
    private static final String SEPARATOR = ", ";

    /**
     * The class name of TaskListView - for simplicity we speak only its items.
     */
    private static final String TASK_LIST_VIEW_CLASS_NAME =
            "com.example.android.apis.accessibility.TaskListView";

    /**
     * Flag whether Text-To-Speech is initialized.
     */
    private boolean mTextToSpeechInitialized;


    private static ReadDataService sSharedInstance;

    private StringBuffer readDataString = new StringBuffer("");

    private int index;
    /**
     * {@inheritDoc}
     */
    @Override
    public void onServiceConnected() {
        sSharedInstance = this;
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
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        // This AccessibilityNodeInfo represents the view that fired the
        // AccessibilityEvent. The following code will use it to traverse the
        // view hierarchy, using this node as a starting point.
        //
        // NOTE: Every method that returns an AccessibilityNodeInfo may return null,
        // because the explored window is in another process and the
        // corresponding View might be gone by the time your request reaches the
        // view hierarchy.
        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }

        int count = source.getChildCount();
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo source1 = source.getChild(i);
            if (source1.getText() == null) {
                int count2 = source1.getChildCount();
                for (int j = 0; j < count2; j++) {
                    AccessibilityNodeInfo source2 = source1.getChild(j);
                    if (source2 != null && source2.getText() != null) {
                        Log.d(LOG_TAG, source2.getText().toString());
                        readDataString.append(source2.getText().toString());
                        checkContent();
                    }
                }
            }
            else {
                Log.d(LOG_TAG, source1.getText().toString());
                readDataString.append(source1.getText().toString());
                checkContent();
            }

        }
        if (source.getText() == null) {
            return;
        }
        CharSequence taskLabel = source.getText();


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
        checkContent();
        readDataString.append(taskStr);


    }

    private void checkContent(){
        index++;
        if(index >= 200) {
            index = 0;

            Intent dialogIntent = new Intent(this, MainActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra("data",readDataString.toString());
            startActivity(dialogIntent);
            //Toast.makeText(getApplicationContext(), readDataString, Toast.LENGTH_LONG).show();
        }
    }

    private AccessibilityNodeInfo getListItemNodeInfo(AccessibilityNodeInfo source) {
        AccessibilityNodeInfo current = source;
        while (true) {
            AccessibilityNodeInfo parent = current.getParent();
            if (parent == null) {
                return null;
            }
            if (TASK_LIST_VIEW_CLASS_NAME.equals(parent.getClassName())) {
                return current;
            }
            // NOTE: Recycle the infos.
            AccessibilityNodeInfo oldCurrent = current;
            current = parent;
            oldCurrent.recycle();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInterrupt() {
        /* do nothing */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onInit(int status) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
