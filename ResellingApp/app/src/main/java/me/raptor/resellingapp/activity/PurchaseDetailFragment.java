package me.raptor.resellingapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Product;
import me.raptor.resellingapp.model.Purchase;
import me.raptor.resellingapp.store.ProductStore;
import me.raptor.resellingapp.store.PurchaseStore;
import me.raptor.resellingapp.view.ProductList;
import me.raptor.resellingapp.view.PurchaseList;

/**
 * Created by Lucas on 19/09/2016.
 */
public class PurchaseDetailFragment extends LoadingFragment implements ProductList.ProductListListener{
    public String getTitle() {
        return purchase ==null?"Purchase Details": getResources().getString(R.string.purchase) + " "+ purchase.getPurchaseID();
    }

    public Purchase purchase;
    public List<Product> products;
    public PurchaseList.PurchaseListListener listener;
    private SimpleDateFormat sdf;

    @BindView(R.id.description_title) TextView data_title;
    @BindView(R.id.description) TextView data;
    @BindView(R.id.product_list_empty_view) TextView emptyView;
    @BindView(R.id.productsList) ProductList productList;

    @BindString(R.string.purchase_data_title) String data_title_string;
    @BindString(R.string.date_title) String date_title;
    @BindString(R.string.product_count_title) String count_title;
    @BindString(R.string.investment_title) String investment_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_detail, container, false);
        ButterKnife.bind(this, view);
        sdf = new SimpleDateFormat("dd/MM/yy");
        productList.setListener((ProductList.ProductListListener) getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateView();
        getActivity().invalidateOptionsMenu();
    }

    public PurchaseDetailFragment newPurchase(){
        purchase = new Purchase(PurchaseStore.getInstance(getContext()).getNextID(), new Date());
        products = new ArrayList<>();
        savePurchase();
        updateView();
        return this;
    }

    public PurchaseDetailFragment setPurchase(Purchase purchase) {
        this.purchase = purchase;
        this.products = ProductStore.getInstance(getContext()).getProductsForPurchase(purchase);
        updateView();
        return this;
    }

    public void savePurchase() {
        PurchaseStore.getInstance(getContext()).insertPurchase(purchase);
        if(listener != null)
            listener.onPurchasesListChanged();
    }

    public void setPurchasesListener(PurchaseList.PurchaseListListener l){
        listener = l;
    }

    private void updateView() {
        if (getView() == null) return;
        showSpinner();
        loadFullPurchase();
        hideSpinner();
    }

    private void loadFullPurchase() {
        if (products.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            productList.setProducts(products);
            emptyView.setVisibility(View.GONE);
        }
        this.data_title.setText(data_title_string);
        Integer productCount = ProductStore.getInstance(getContext()).getProductCountForPurchase(purchase);
        Integer investment = ProductStore.getInstance(getContext()).getInvestmentForPurchase(purchase);
        String date_msg = date_title + ' ' + sdf.format(purchase.getDate()) + '\n';
        String count_msg = count_title + ' ' + productCount + '\n';
        String investment_msg = investment_title + " $" + investment;
        String msg = date_msg + count_msg + investment_msg;
        this.data.setText(msg);
    }

    @Override
    public void onProductSelected(Product product) {

    }

    @Override
    public void onProductListChanged() {
        products.clear();
        products.addAll(ProductStore.getInstance(getContext()).getProductsForPurchase(purchase));
        productList.clear();
        updateView();
    }

    public Purchase getPurchase(){
        return purchase;
    }
}
