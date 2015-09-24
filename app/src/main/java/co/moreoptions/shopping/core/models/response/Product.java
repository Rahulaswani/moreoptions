package co.moreoptions.shopping.core.models.response;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anshul on 17/09/15.
 */
public class Product implements Serializable{
    @SerializedName("productName")
    public String productName;

    @SerializedName("productSellingPrice")
    public ProductPrice productSellingPrice;

    @SerializedName("productImageUrls")
    public ImageList imageUrlList;

    @SerializedName("appName")
    public String appName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product that = (Product) o;

        return Objects.equal(this.productName, that.productName) &&
                Objects.equal(this.productSellingPrice, that.productSellingPrice) &&
               // Objects.equal(this.imageUrlList, that.imageUrlList) &&
                Objects.equal(this.appName, that.appName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productName, productSellingPrice, appName);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("productName", productName)
                .add("productPrice", productSellingPrice)
              //  .add("imageUrlList", imageUrlList)
                .add("appName", appName)
                .toString();
    }
}
