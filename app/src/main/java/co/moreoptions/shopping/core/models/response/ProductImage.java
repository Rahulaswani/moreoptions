package co.moreoptions.shopping.core.models.response;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anshul on 10/10/15.
 */
public class ProductImage implements Serializable{

    @SerializedName("url")
    public String url;

    @SerializedName("width")
    public String width;

    @SerializedName("height")
    public String height;


    public float getAspectRatio(){
        return Integer.parseInt(width) / Integer.parseInt(height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductImage that = (ProductImage) o;

        return Objects.equal(this.url, that.url) &&
                Objects.equal(this.width, that.width) &&
                Objects.equal(this.height, that.height);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url, width, height);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("url", url)
                .add("width", width)
                .add("height", height)
                .toString();
    }
}
