package co.moreoptions.shopping.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.moreoptions.shopping.R;

/**
 * Created by anshul on 11/09/15.
 */
public class ProductDetailViewHolder extends RecyclerView.ViewHolder{

    @InjectView(R.id.textTest)
    TextView testText;
    public ProductDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }

    public void onBind(String text){
        bindText(text);
    }

    private void bindText(String text){
        testText.setText(text);
    }

}
