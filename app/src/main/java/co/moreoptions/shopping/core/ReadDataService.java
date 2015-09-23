package co.moreoptions.shopping.core;

/**
 * Created by 11101 on 08/09/15.
 */

import android.accessibilityservice.AccessibilityService;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityRecord;

import java.util.Locale;


/**
 * This class demonstrates how an accessibility service can query
 * window content to improve the feedback given to the user.
 */
public class ReadDataService extends AccessibilityService implements TextToSpeech.OnInitListener {

    /** Tag for logging. */
    private static final String LOG_TAG = "ReadDataService/onAccessibilityEvent";

    /** Comma separator. */
    private static final String SEPARATOR = ", ";

    /** The class name of TaskListView - for simplicity we speak only its items. */
    private static final String TASK_LIST_VIEW_CLASS_NAME =
            "com.example.android.apis.accessibility.TaskListView";

    /** Flag whether Text-To-Speech is initialized. */
    private boolean mTextToSpeechInitialized;

    /** Handle to the Text-To-Speech engine. */
    private TextToSpeech mTts;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onServiceConnected() {
        // Initializes the Text-To-Speech engine as soon as the service is connected.
        mTts = new TextToSpeech(getApplicationContext(), this);
    }

    /**
     * Processes an AccessibilityEvent, by traversing the View's tree and
     * putting together a message to speak to the user.
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!mTextToSpeechInitialized) {
            Log.e(LOG_TAG, "Text-To-Speech engine not ready.  Bailing out.");
            return;
        }

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




        if(source.getText() == null){
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

        // Announce the utterance.
        mTts.speak(utterance.toString(), TextToSpeech.QUEUE_FLUSH, null);
        Log.d(LOG_TAG, utterance.toString());
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
        // Set a flag so that the TaskBackService knows that the Text-To-Speech
        // engine has been initialized, and can now handle speaking requests.
        if (status == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.US);
            mTextToSpeechInitialized = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTextToSpeechInitialized) {
            mTts.shutdown();
        }
    }
}
