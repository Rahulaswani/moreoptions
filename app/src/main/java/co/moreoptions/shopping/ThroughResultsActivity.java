package co.moreoptions.shopping;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.appevents.AppEventsLogger;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.leanplum.Leanplum;
import com.leanplum.LeanplumPushService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.moreoptions.shopping.analytics.MixpanelAnalytics;
import co.moreoptions.shopping.core.ReadDataService;
import co.moreoptions.shopping.core.models.response.ProductList;
import co.moreoptions.shopping.views.ProductListPopupSheet;

public class ThroughResultsActivity extends Activity {

    public static final String TAG = ThroughResultsActivity.class.getSimpleName();

    private ReadDataService readDataService;

    @InjectView(R.id.bottomsheet)
    BottomSheetLayout bottomSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


        Intent serviceIntent = new Intent(this, ReadDataService.class);
        startService(serviceIntent);
        readDataService = ReadDataService.getSharedInstance();
        initializeLLeanPlum();
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            setRecyclerView(extras);
        }
    }

    private void initializeLLeanPlum(){
        if (BuildConfig.DEBUG) {
            Leanplum.setAppIdForDevelopmentMode(getResources().getString(R.string.leanplum_application_id),
                    "dev_Iryz153NngA50atKvda6WcxyaadAM1GYGkrGdF1u17k");
        } else {
            Leanplum.setAppIdForProductionMode(getResources().getString(R.string.leanplum_application_id),
                    getResources().getString(R.string.leanplum_prod_access_key));
        }
        LeanplumPushService.setGcmSenderId(LeanplumPushService.LEANPLUM_SENDER_ID);
        Leanplum.enableVerboseLoggingInDevelopmentMode();
        Leanplum.start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        MixpanelAnalytics.getInstance().onScreenOpened(TAG);
    }

    private void setRecyclerView(Bundle extras){
        ProductList productList = (ProductList) extras.getSerializable("data");
        if(productList == null || productList.getProducts() == null){
            return;
        }

        if(productList.getProducts().size() > 0) {
            final ProductListPopupSheet productListPopupSheet = new ProductListPopupSheet(this, productList);

            bottomSheetLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheetLayout.showWithSheetView(productListPopupSheet);
                    bottomSheetLayout.setOnSheetStateChangeListener(new BottomSheetLayout.OnSheetStateChangeListener() {
                        @Override
                        public void onSheetStateChanged(BottomSheetLayout.State state) {
                            if (state == BottomSheetLayout.State.HIDDEN) {
                                finish();
                            }
                        }
                    });
                }
            }, 1000);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    private class ItemSpacingDecoration extends RecyclerView.ItemDecoration {
        private int space;
        private int density;

        public ItemSpacingDecoration(int space) {
            density = (int) ThroughResultsActivity.this.getResources().getDisplayMetrics().density;
            this.space = space * density;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position < 0) {
                return;
            }
            outRect.right = space;
        }
    }
}
