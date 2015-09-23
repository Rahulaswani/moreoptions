package co.moreoptions.shopping.core;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.moreoptions.shopping.R;
import co.moreoptions.shopping.core.models.response.Product;

/**
 * Created by anshul on 11/09/15.
 */
public class ProductDetailViewHolder extends RecyclerView.ViewHolder{

    @InjectView(R.id.productName)
    TextView productName;

    @InjectView(R.id.productImage)
    SimpleDraweeView productImage;

    @InjectView(R.id.price)
    TextView productPrice;



    public ProductDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void onBind(Product product){
        bindProductName(product.productName);
        bindProductImage(product.imageUrlList.lowResolution);
        bindProductPrice(product.productSellingPrice.amount);
    }

    private void bindProductName(String text){
        productName.setText(text);
    }

    private void bindProductImage(String uri){
        DraweeController heroController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(uri))
                .build();
        productImage.setController(heroController);
    }

    private void bindProductPrice(String text){
        productPrice.setText(text);
    }
}
