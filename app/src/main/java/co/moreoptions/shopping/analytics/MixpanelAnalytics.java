package co.moreoptions.shopping.analytics;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import co.moreoptions.shopping.BuildConfig;
import co.moreoptions.shopping.MoreOptionsApplication;
import co.moreoptions.shopping.R;

/**
 * Class that contains helper methods for Mixpanel
 * <p/>
 * Created by anshul on 25/09/15.
 */
public class MixpanelAnalytics {

    private static final String TAG = "MixpanelAnalytics";

    private static final boolean ENABLED = BuildConfig.ENABLE_MIXPANEL;

    private static MixpanelAnalytics sInstance;

    private MixpanelAPI mMixpanelAPI;

    private PropsGenerator mPropsGenerator;

    private MixpanelAnalytics() {

        if (ENABLED) {
            final String projectToken = MoreOptionsApplication.getStaticContext().getString(R.string.mixipanel_token);
            mMixpanelAPI = MixpanelAPI.getInstance(MoreOptionsApplication.getStaticContext(), projectToken);
            mPropsGenerator = new PropsGenerator();
        }

    }

    /**
     * Retrieve an instance of the Mixpanel analytics for this application
     */
    public static MixpanelAnalytics getInstance() {

        if (sInstance == null) {
            synchronized (MixpanelAnalytics.class) {
                if (sInstance == null) {
                    sInstance = new MixpanelAnalytics();
                }
            }
        }

        return sInstance;
    }


    /**
     * Attempt to send all stored events. Call this in the onDestroy()
     * of the Main activity
     */
    public void flush() {

        if (ENABLED) {
            mMixpanelAPI.flush();
        }
    }


    /**
     * Send the event that a Session has ended
     *
     * @param sessionDurationSeconds The duration of the session, in seconds
     */
    public void onSessionEnded(final long sessionDurationSeconds) {

        if (ENABLED) {
            track("Session Ended", mPropsGenerator.makeDurationProps(sessionDurationSeconds));
        }
    }

    /**
     * Send the event for which screen is opened
     */
    public void onScreenOpened(String screenName) {

        if (ENABLED) {
            track("Verification SMS Sent", mPropsGenerator.sendScreenProps(screenName));
        }
    }

    /**
     * Send the event for which screen is opened
     */
    public void onAppLaunched(boolean appLaunch) {

        if (ENABLED) {
            track("App Launched", mPropsGenerator.makeAppLaunchProps(appLaunch));
        }
    }


    /**
     * Track an event with a set of properties
     *
     * @param event The event to send
     * @param props The properties to send. If this is {@code null}, the event is not tracked
     */
    private void track(final String event, final JSONObject props) {

        if (props != null) {
            trackOpt(event, props);
        }
    }

    /**
     * Track an event, with an optional set of properties
     *
     * @param event The event to send
     * @param props The properties to send. If this is {@code null}, the event is still tracked
     */
    private void trackOpt(final String event, final JSONObject props) {

        if (ENABLED) {
            mMixpanelAPI.track(event, props);
        }
    }

}
