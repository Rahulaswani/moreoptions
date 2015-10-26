package co.moreoptions.shopping.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import co.moreoptions.shopping.CustomTabs.OpenWebsite;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.moreoptions.shopping.R;
import co.moreoptions.shopping.CustomTabs.ServiceConnectionCallback;
import co.moreoptions.shopping.Utils.AppConstants;
import co.moreoptions.shopping.core.models.response.Product;

public class PopUpFragment extends Fragment implements View.OnClickListener, ServiceConnectionCallback {

    private Product mProduct;

    @InjectView(R.id.productImage)
    SimpleDraweeView productImage;

    @InjectView(R.id.savePrice)
    TextView priceDiffText;

    @InjectView(R.id.buyButton)
    Button buyButton;

    @InjectView(R.id.productName)
    TextView productName;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PopUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PopUpFragment newInstance(Bundle args) {
        PopUpFragment fragment = new PopUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PopUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View contentView = inflater.inflate(R.layout.fragment_pop_up, container, false);

        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);

        Bundle extras = new Bundle();
        extras = getArguments();

        if(extras != null){
            mProduct = (Product) extras.getSerializable("product");
        }

        DraweeController heroController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(mProduct.imageUrlList.get(0).url))
                .build();

        productImage.setAspectRatio(mProduct.imageUrlList.get(0).getAspectRatio());
        productImage.setController(heroController);

        productName.setText(mProduct.productName);
        priceDiffText.setText(mProduct.productSellingPrice);

        buyButton.setOnClickListener(this);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buyButton:
                Intent launchAppIntent = new Intent(getActivity(),OpenWebsite.class);
                launchAppIntent.putExtra(AppConstants.URL_TO_LAUNCH,mProduct.productUrl);
                this.startActivity(launchAppIntent);
                break;
        }
    }

    /**
     * Called when the service is connected.
     *
     * @param client a CustomTabsClient
     */
    @Override
    public void onServiceConnected(CustomTabsClient client) {

    }

    /**
     * Called when the service is disconnected.
     */
    @Override
    public void onServiceDisconnected() {

    }
}
