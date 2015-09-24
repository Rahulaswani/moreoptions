package co.moreoptions.shopping.core.models.response;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 11101 on 24/09/15.
 */
public class ImageList implements Serializable{

    @SerializedName("100x100")
    public String lowResolution;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageList that = (ImageList) o;

        return Objects.equal(this.lowResolution, that.lowResolution);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(lowResolution);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("lowResolution", lowResolution)
                .toString();
    }
}
