package co.moreoptions.shopping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import co.moreoptions.shopping.core.ProductDetailViewHolder;

/**
 * Created by anshul on 11/09/15.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductDetailViewHolder>{
    private static final String TAG = ProductAdapter.class.getSimpleName();

    //private ArrayList<Product> products;
    private ArrayList<String> mDataList = new ArrayList<>();

    public ProductAdapter(ArrayList<String> dataList){
        mDataList = dataList;
    }
//    public void updateProducts( ArrayList<Product> products){
//        this.products = products;
//        notifyDataSetChanged();
//    }

    @Override
    public ProductDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_product, viewGroup, false);
        return new ProductDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductDetailViewHolder holder, int position) {
//        Product product = products.get(position);
//        holder.onBind(product);

        holder.onBind(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        if(mDataList != null){
            return mDataList.size();
        }
        return 0;
    }
}