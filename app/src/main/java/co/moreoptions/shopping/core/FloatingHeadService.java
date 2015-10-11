package co.moreoptions.shopping.core;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.lang.ref.WeakReference;

import co.moreoptions.shopping.R;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewListener;
import jp.co.recruit_lifestyle.android.floatingview.FloatingViewManager;



/**
 * Created by anshul on 10/10/15.
 */
public class FloatingHeadService extends Service implements FloatingViewListener {

    private IBinder mFloatingHeadBinder;

    /**
     * FloatingViewManager
     */
    private FloatingViewManager mFloatingViewManager;

    private ReadDataService mReadDataService;

    /**
     * {@inheritDoc}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mFloatingViewManager != null) {
            return START_STICKY;
        }

        Fresco.initialize(this);
        final DisplayMetrics metrics = new DisplayMetrics();
        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mFloatingHeadBinder = new FloatingHeadBinder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);
        final View view =  inflater.inflate(R.layout.floating_action_button, null, false);

        SimpleDraweeView iconView = (SimpleDraweeView) view.findViewById(R.id.floating_icon);

        mFloatingViewManager = new FloatingViewManager(this, this);
        mFloatingViewManager.setFixedTrashIconImage(R.drawable.ic_trash_fixed);
        mFloatingViewManager.setActionTrashIconImage(R.drawable.ic_trash_action);
        final FloatingViewManager.Options options = new FloatingViewManager.Options();
        options.shape = FloatingViewManager.SHAPE_CIRCLE;
        options.overMargin = (int) (1 * metrics.density);
        mFloatingViewManager.addViewToWindow(view, options);
        mReadDataService = ReadDataService.getSharedInstance();


        try {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.round_fab_button))
                    .build();
            DraweeController heroController = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setAutoPlayAnimations(true)
                    .build();

            iconView.setController(heroController);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReadDataService.sendValues();
            }
        });

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mFloatingHeadBinder;
    }

    @Override
    public void onFinishFloatingView() {
        stopSelf();
    }

    private void destroy() {
        if (mFloatingViewManager != null) {
            mFloatingViewManager.removeAllViewToWindow();
            mFloatingViewManager = null;
        }
    }

    public static class FloatingHeadBinder extends Binder {

        private final WeakReference<FloatingHeadService> mService;

        FloatingHeadBinder(FloatingHeadService service) {
            mService = new WeakReference<>(service);
        }

        public FloatingHeadService getService() {
            return mService.get();
        }
    }

}

