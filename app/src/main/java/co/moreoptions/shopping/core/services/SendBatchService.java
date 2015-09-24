package co.moreoptions.shopping.core.services;

import java.util.List;

import co.moreoptions.shopping.core.http.BaseApi;
import co.moreoptions.shopping.core.http.HttpConstants;
import co.moreoptions.shopping.core.http.RequestListener;
import co.moreoptions.shopping.core.models.request.ValuesBatchModel;
import co.moreoptions.shopping.core.models.response.Product;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by anshul on 17/09/15.
 */
public class SendBatchService {
    public SendBatchService(){
    }

    public void postValues(ValuesBatchModel valuesBatchModel, final RequestListener requestListener){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(HttpConstants.TEMP_BASE_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        BaseApi baseApi = restAdapter.create(BaseApi.class);
        baseApi.putValues(valuesBatchModel, new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products, Response response) {
                requestListener.onRequestSuccess(products);
            }

            @Override
            public void failure(RetrofitError error) {
                requestListener.onRequestFailure(error);

            }
        });
    }

}
