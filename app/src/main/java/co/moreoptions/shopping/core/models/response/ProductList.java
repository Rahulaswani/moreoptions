package co.moreoptions.shopping.core.models.response;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anshul on 17/09/15.
 */
public class ProductList implements Serializable{
    List<Product> products;

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductList that = (ProductList) o;

        return Objects.equal(this.products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(products);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("products", products)
                .toString();
    }
}
