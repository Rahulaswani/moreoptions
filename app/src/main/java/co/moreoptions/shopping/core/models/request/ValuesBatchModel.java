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

    @SerializedName("values")
    public List<Values> values;

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setValues(List<Values> values) {
        this.values = values;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ValuesBatchModel that = (ValuesBatchModel) o;

        return Objects.equal(this.appName, that.appName) &&
                Objects.equal(this.values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(appName, values);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("appName", appName)
                .add("values", values)
                .toString();
    }
}
