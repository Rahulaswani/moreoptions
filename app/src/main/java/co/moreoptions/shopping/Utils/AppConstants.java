package co.moreoptions.shopping.Utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anshul on 25/09/15.
 */
public class AppConstants {

    public static final int ELECTRONICS_CATEGORY = 1;
    public static final int FASHION_CATEGORY = 2;
    public static final int CABS_CATEGORY = 3;
    public static final int COUPONS_CATEGORY = 4;

    public static final String PREF_INVITE_CODE = "invite_code";
    public static final String PREF_IS_INVITE_ON = "is_invite_on";
    public static final String URL_TO_LAUNCH = "url_to_launch";


    public static final String EXTRA_CUSTOM_TABS_KEEP_ALIVE =
            "android.support.customtabs.extra.KEEP_ALIVE";

    private String[] electronicsApps = {"Zopper","BuyingIQ"};
    private String[] fashionApps = {"Myntra","Jabong","Limeroad","Yempe","wooplr","roposo","Voonik","Yebhi"};
    private String[] cabApps = {"cabsguru","ola","uber","meru","tfs","mega",};
    private String[] couponApps = {"91Mobiles","Scandid","CouponRani","BuyHatke"};
    private String[] commonApps = {"Amazon","flipkart","Snapdeal","Paytm","Ebay","Shopclues","Infibeam","HomeShop18"};

    public HashMap<String, String> mIdProductMap = new HashMap<>();

    public static interface Keys{
        String UP_NAVIGATION_TAG = "up_navigation_tag";
    }

    public List<String> supportedAppList(int category){
        List<String> appList = new ArrayList<>();
        switch (category){
            case ELECTRONICS_CATEGORY:
                appList = Arrays.asList(electronicsApps);
                appList.addAll(Arrays.asList(commonApps));
                break;
            case FASHION_CATEGORY:
                appList = Arrays.asList(fashionApps);
                appList.addAll(Arrays.asList(commonApps));
                break;
            case CABS_CATEGORY:
                appList = Arrays.asList(cabApps);
                break;
            case COUPONS_CATEGORY:
                appList = Arrays.asList(couponApps);
                appList.addAll(Arrays.asList(commonApps));
                break;
        }
        return appList;
    }

    public HashMap<String, String> getmIdProductMap() {
        mIdProductMap.put("com.flipkart.android","product_page_title_main_title");
        mIdProductMap.put("com.myntra.android","tv_pdp_brand_and_description");
        return mIdProductMap;
    }


}
