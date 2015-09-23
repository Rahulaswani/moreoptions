package co.moreoptions.shopping;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.moreoptions.shopping.core.ReadDataService;
import co.moreoptions.shopping.core.models.response.ProductList;

public class MainActivity extends Activity {

    private ReadDataService readDataService;

    private ProductAdapter productAdapter;

    @InjectView(R.id.productRack)
    RecyclerView productRack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Intent serviceIntent = new Intent(this, ReadDataService.class);
        startService(serviceIntent);
        readDataService = ReadDataService.getSharedInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            setRecyclerView(extras);
        }
    }

    private void setRecyclerView(Bundle extras){
        ProductList productList = (ProductList) extras.getSerializable("data");
        if(productList.getProducts() == null){
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.
                HORIZONTAL, false);
        productAdapter = new ProductAdapter(productList.getProducts());
        productRack.setLayoutManager(linearLayoutManager);
        productRack.addItemDecoration(new ItemSpacingDecoration(11));
        productRack.setAdapter(productAdapter);
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

    private class ItemSpacingDecoration extends RecyclerView.ItemDecoration {
        private int space;
        private int density;

        public ItemSpacingDecoration(int space) {
            density = (int) MainActivity.this.getResources().getDisplayMetrics().density;
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
