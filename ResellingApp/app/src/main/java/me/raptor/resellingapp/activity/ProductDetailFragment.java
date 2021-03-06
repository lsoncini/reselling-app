package me.raptor.resellingapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Product;
import me.raptor.resellingapp.model.Purchase;
import me.raptor.resellingapp.store.CategoryStore;
import me.raptor.resellingapp.store.ProductStore;
import me.raptor.resellingapp.store.PurchaseStore;
import me.raptor.resellingapp.view.ProductListListener;

public class ProductDetailFragment extends LoadingFragment implements OnProductChangeListener{

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.category)
    TextView category;

    public Product product;
    boolean productChanged;
    ProductListListener listener;

    @Override
    public String getTitle() {
        return product.getName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, view);
        this.setProductsListener((ProductListListener) getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
        getActivity().invalidateOptionsMenu();
    }

    public ProductDetailFragment setProduct(Product product) {
        this.product = product;

        productChanged = true;
        updateView();
        return this;
    }

    private void updateView() {
        if (getView() == null || !productChanged) return;

        if(product == null && getContext() != null){
            Context ctx = getContext();
            Integer id = ProductStore.getInstance(ctx).numberOfRows()+1;
            CategoryStore.getInstance(ctx).addCategory("Remera");
            Purchase purchase = PurchaseStore.getInstance(ctx).getPurchase(1);
            this.product = new Product(id,"Producto "+id, "Remera", null, null, purchase, 100.0, 200.0);
        }

        name.setText(product.getName());
        String s = "$" + (int)Math.ceil(product.getSalePrice());
        price.setText(s);
        category.setText(product.getCategory());
        productChanged = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        setProduct(product);
        getActivity().invalidateOptionsMenu();
    }

    public void saveProduct() {
        ProductStore.getInstance(getContext()).insertProduct(product);
        listener.onProductListChanged();
    }


    public void setProductsListener(ProductListListener l){
        listener = l;
    }

    @Override
    public void onProductChanged(Product product) {
        setProduct(product);
        listener.onProductListChanged();
    }
}
