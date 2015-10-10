package co.moreoptions.shopping.views;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import co.moreoptions.shopping.ProductAdapter;
import co.moreoptions.shopping.R;
import co.moreoptions.shopping.ThroughResultsActivity;
import co.moreoptions.shopping.core.models.response.Product;
import co.moreoptions.shopping.core.models.response.ProductList;
import co.moreoptions.shopping.fragments.PopUpFragment;

/**
 * Created by anshul on 09/10/15.
 */
public class ProductListPopupSheet extends FrameLayout {

    private ProductAdapter productAdapter;

    private RecyclerView mProductRack;

    private ProductList mProductList;

    private SlidingTabLayout mSlidingTabLayout;

    private PagerAdapter mPagerdapter;

    private ViewPager mPager;

    private SparseArray<Fragment> mPages;

    private ThroughResultsActivity context;




    public ProductListPopupSheet(ThroughResultsActivity context, final ProductList productList) {
        super(context);
        this.mProductList = productList;

        inflate(context, R.layout.product_list_layout, this);
        mProductRack = (RecyclerView) findViewById(R.id.productRack);
        this.context = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                productAdapter = new ProductAdapter(mProductList.getProducts());

                List<String> tabs = new ArrayList<String>();
                for(Product product : mProductList.getProducts()){
                    tabs.add(product.productSellingPrice);
                }

                mPagerdapter = new PagerAdapter(context.getFragmentManager(), tabs);



                return true;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.
                        HORIZONTAL, false);
                mProductRack.setLayoutManager(linearLayoutManager);
                mProductRack.addItemDecoration(new ItemSpacingDecoration(11));
                mProductRack.setAdapter(productAdapter);

                mPager = (ViewPager) findViewById(R.id.pager);
                mPager.setAdapter(mPagerdapter);

                mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
                mSlidingTabLayout.setCustomTabView(R.layout.tab_home, R.id.label_tab, R.id.app_image);
                mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.blue));
                mSlidingTabLayout.setDistributeEvenly(true);
                mSlidingTabLayout.setViewPager(mPager);

                mPager.setCurrentItem(0);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    private class ItemSpacingDecoration extends RecyclerView.ItemDecoration {
        private int space;
        private int density;

        public ItemSpacingDecoration(int space) {
            density = (int) getResources().getDisplayMetrics().density;
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

    public class PagerAdapter extends FragmentStatePagerAdapter {

        private final List<String> mTabs;
        private int mScrollY;


        public PagerAdapter(final FragmentManager manager, final List<String> tabs) {
            super(manager);
            mTabs = tabs;
            mPages = new SparseArray<Fragment>();
        }

        public void setScrollY(int scrollY) {
            mScrollY = scrollY;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (0 <= mPages.indexOfKey(position)) {
                mPages.remove(position);
            }
            super.destroyItem(container, position, object);
        }



        @Override
        public Fragment getItem(int position) {

            Fragment f;
            Bundle args = new Bundle();
            args.putSerializable("product",mProductList.getProducts().get(position));
            f = PopUpFragment.newInstance(args);
            mPages.put(position, f);
            return f;
        }


        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position);
        }

        public Fragment getItemAt(int position) {
            return mPages.get(position);
        }


    }
}
