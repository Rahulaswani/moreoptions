package co.moreoptions.shopping;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import co.moreoptions.shopping.core.ProductDetailViewHolder;
import co.moreoptions.shopping.core.models.response.Product;

/**
 * Created by anshul on 11/09/15.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductDetailViewHolder>{
    private static final String TAG = ProductAdapter.class.getSimpleName();

    private List<Product> mDataList = new ArrayList<>();

    public ProductAdapter(List<Product> dataList){
        mDataList = dataList;
    }

    @Override
    public ProductDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_product, viewGroup, false);
        return new ProductDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductDetailViewHolder holder, int position) {
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