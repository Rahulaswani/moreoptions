package co.moreoptions.shopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import co.moreoptions.shopping.app.MOLog;
import co.moreoptions.shopping.core.ReadDataService;
import com.crashlytics.android.Crashlytics;
import com.facebook.appevents.AppEventsLogger;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    private MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(this, ReadDataService.class);
        startService(serviceIntent);
        String projectToken = getResources().getString(R.string.mixipanel_token);
        mixpanel = MixpanelAPI.getInstance(this, projectToken);

        trackApplaunch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    void trackApplaunch(){
        try {
            JSONObject props = new JSONObject();
            props.put("App Launched", true);
            mixpanel.track("MainActivity - onCreate called", props);
        } catch (JSONException e) {
            MOLog.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
