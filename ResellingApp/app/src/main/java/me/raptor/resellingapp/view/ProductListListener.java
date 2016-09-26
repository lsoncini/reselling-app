package me.raptor.resellingapp.view;

import me.raptor.resellingapp.model.Product;

/**
 * Created by Lucas on 26/09/2016.
 */
public interface ProductListListener {
    void onProductSelected(Product product);
    void onProductListChanged();
}
