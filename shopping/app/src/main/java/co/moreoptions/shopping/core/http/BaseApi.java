package co.moreoptions.shopping.core.http;

import java.util.List;

import co.moreoptions.shopping.core.models.request.ValuesBatchModel;
import co.moreoptions.shopping.core.models.response.Product;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.PUT;

/**
 * Created by anshul on 17/09/15.
 */
public interface BaseApi {

    @PUT("/put")
    void putValues(@Body ValuesBatchModel valuesBatchModel, Callback<List<Product>> callback);
}
