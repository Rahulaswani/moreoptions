package co.moreoptions.shopping.analytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.moreoptions.shopping.app.MOLog;

/**
 * Props generator for Mixpanel
 * <p/>
 * Created by anshul on 25/09/15.
 */
public class PropsGenerator {

    private static final String TAG = "PropsGenerator";


    /**
     * Create a JsonObject props for the session duration
     *
     * @param sessionDurationSeconds The duration of the session, in seconds
     */
    public JSONObject makeDurationProps(final long sessionDurationSeconds) {

        JSONObject props;

        try {
            props = new JSONObject();
            props.put("Duration", sessionDurationSeconds);

        } catch (JSONException e) {
            MOLog.e(TAG, "Error building Mixpanel Props", e);
            props = null;
        }

        return props;
    }

    /**
     * Create a JsonObject props for the screen opened
     *
     * @param screenName The name of the activity or the screen opened
     */
    public JSONObject sendScreenProps(String screenName) {

        JSONObject props;

        try {
            props = new JSONObject();
            props.put("Screen", screenName);

        } catch (JSONException e) {
            MOLog.e(TAG, "Error building Mixpanel Props", e);
            props = null;
        }

        return props;
    }

    /**
     * Create a JsonObject props for the app launch event
     *
     * @param appLaunch boolean for app launch
     */
    public JSONObject makeAppLaunchProps(boolean appLaunch) {

        JSONObject props;

        try {
            props = new JSONObject();
            props.put("App Launched", appLaunch);

        } catch (JSONException e) {
            MOLog.e(TAG, "Error building Mixpanel Props", e);
            props = null;
        }

        return props;
    }


    /**
     * Converts an array of strings to a JSONArray
     *
     * @param strings The list of strings to convert
     * @return A JSONArray with the strings
     */
    private JSONArray arrayFromStrings(List<String> strings) {

        final JSONArray array = new JSONArray();

        if (strings != null) {
            for (final String eachString : strings) {
                array.put(eachString);
            }
        }

        return array;
    }
}
