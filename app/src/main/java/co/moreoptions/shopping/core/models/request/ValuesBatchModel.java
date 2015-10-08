package co.moreoptions.shopping.core.models.request;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by anshul on 17/09/15.
 */
public class ValuesBatchModel {

    @SerializedName("appName")
    public String appName;

    @SerializedName("productName")
    public String productName;

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValuesBatchModel that = (ValuesBatchModel) o;

        return Objects.equal(this.appName, that.appName) &&
                Objects.equal(this.productName, that.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(appName, productName);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("appName", appName)
                .add("productName", productName)
                .toString();
    }
}
