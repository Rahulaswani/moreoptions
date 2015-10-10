package co.moreoptions.shopping.Utils;

import android.util.ArrayMap;

import java.util.HashMap;

/**
 * Created by anshul on 25/09/15.
 */
public class AppConstants {

    public HashMap<String, String> mIdProductMap = new HashMap<>();

    public static interface Keys{
        String UP_NAVIGATION_TAG = "up_navigation_tag";
    }

    public HashMap<String, String> getmIdProductMap() {
        mIdProductMap.put("com.flipkart.android","product_page_title_main_title");
        mIdProductMap.put("com.myntra.android","tv_pdp_brand_and_description");
        return mIdProductMap;
    }


}
