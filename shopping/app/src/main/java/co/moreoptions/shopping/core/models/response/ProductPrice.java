package co.moreoptions.shopping.core.models.response;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anshul on 24/09/15.
 */
public class ProductPrice implements Serializable{

    @SerializedName("currency")
    public String currency;

    @SerializedName("amount")
    public String amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductPrice that = (ProductPrice) o;

        return Objects.equal(this.currency, that.currency) &&
                Objects.equal(this.amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(currency, amount);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("currency", currency)
                .add("amount", amount)
                .toString();
    }
}
