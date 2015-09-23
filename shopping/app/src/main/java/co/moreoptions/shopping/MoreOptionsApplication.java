package co.moreoptions.shopping;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.ViewConfiguration;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.lang.reflect.Field;

import co.moreoptions.shopping.core.http.BaseApi;
import co.moreoptions.shopping.core.http.HttpConstants;
import retrofit.RestAdapter;

/**
 * Custom Application class which holds some common functionality for the
 * Application
 *
 * @author Anshul Kamboj
 */
public class MoreOptionsApplication extends Application {

    private static final String TAG = "MoreOptionsApplication";
    /**
     * Maintains a reference to the application context so that it can be
     * referred anywhere wihout fear of leaking. It's a hack, but it works.
     */
    private static Context sStaticContext;
    private BaseApi mBaseApi;
    public static volatile Handler applicationHandler = null;
    /**
     * Gets a reference to the application context
     */
    public static Context getStaticContext() {
        if (sStaticContext != null) {
            return sStaticContext;
        }
        //Should NEVER hapen
        throw new RuntimeException("No static context instance");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sStaticContext = getApplicationContext();
        overrideHardwareMenuButton();
        applicationHandler = new Handler(sStaticContext.getMainLooper());
        Fresco.initialize(this);
    }

    /**
     * Initialize the Api Services
     */
    private void initApiServices() {
        final RestAdapter.Builder builder = new RestAdapter.Builder();
        mBaseApi = builder.setEndpoint(HttpConstants.getBaseUrl()).build().create(BaseApi.class);
    }

    public BaseApi getmBaseApi() {
        return mBaseApi;
    }

    /**
     * Some device manufacturers are stuck in the past and stubbornly use H/W
     * menu buttons, which is deprecated since Android 3.0. This breaks the UX
     * on newer devices since the Action Bar overflow just doesn't show. This
     * little hack tricks the Android OS into thinking that the device doesn't
     * have a permanant menu button, and hence the Overflow button gets shown.
     * This doesn't disable the Menu button, however. It will continue to
     * function as normal, so the users who are already used to it will be able
     * to use it as before
     */
    private void overrideHardwareMenuButton() {
        try {
            final ViewConfiguration config = ViewConfiguration.get(this);
            final Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (final Exception ex) {
            // Ignore since we can't do anything
        }
    }
}