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
import me.raptor.resellingapp.model.Sale;
import me.raptor.resellingapp.store.ProductStore;
import me.raptor.resellingapp.store.SaleStore;
import me.raptor.resellingapp.view.ProductListListener;
import me.raptor.resellingapp.view.SaleList;
import me.raptor.resellingapp.view.SaleProductList;

/**
 * Created by Lucas on 25/09/2016.
 */
public class SaleDetailFragment extends LoadingFragment implements ProductListListener, OnProductChangeListener{
    public String getTitle() {
        return sale ==null?"Sale Details": getResources().getString(R.string.sale) + " "+ sale.getSaleID();
    }

    public Sale sale;
    public List<Product> products;
    public SaleList.SaleListListener listener;
    private SimpleDateFormat sdf;

    @BindView(R.id.description_title) TextView data_title;
    @BindView(R.id.description) TextView data;
    @BindView(R.id.product_list_empty_view) TextView emptyView;
    @BindView(R.id.productsList)
    SaleProductList productList;

    @BindString(R.string.information_title) String data_title_string;
    @BindString(R.string.sale_date_title) String date_title;
    @BindString(R.string.product_count_title) String count_title;
    @BindString(R.string.total_sales_title) String total_sales_title;
    @BindString(R.string.group_title) String group_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_detail, container, false);
        ButterKnife.bind(this, view);
        sdf = new SimpleDateFormat(getResources().getString(R.string.date_format_2));
        productList.setListener((ProductListListener) getActivity());
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

    public SaleDetailFragment newSale(){
        sale = new Sale(SaleStore.getInstance(getContext()).getNextID(), new Date(),null);
        products = new ArrayList<>();
        saveSale();
        updateView();
        return this;
    }

    public SaleDetailFragment setSale(Sale sale) {
        this.sale = sale;
        this.products = ProductStore.getInstance(getContext()).getProductsForSale(sale);
        updateView();
        return this;
    }

    public void saveSale() {
        SaleStore.getInstance(getContext()).insertSale(sale);
        if(listener != null)
            listener.onSalesListChanged();
    }

    public void setSalesListener(SaleList.SaleListListener l){
        listener = l;
    }

    private void updateView() {
        if (getView() == null) return;
        showSpinner();
        loadFullSale();
        hideSpinner();
    }

    private void loadFullSale() {
        if (products.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            productList.setProducts(products);
            emptyView.setVisibility(View.GONE);
        }
        this.data_title.setText(data_title_string);
        Integer productCount = ProductStore.getInstance(getContext()).getProductCountForSale(sale);
        Integer total_sale = ProductStore.getInstance(getContext()).getTotalForSale(sale);
        String date_msg = date_title + ' ' + sdf.format(sale.getDate()) + '\n';
        String group_msg = group_title + ' ' + sale.getGroup() + '\n';
        String count_msg = count_title + ' ' + productCount + '\n';
        String total_msg = total_sales_title + " $" + total_sale;
        String msg = date_msg + group_msg + count_msg + total_msg;
        this.data.setText(msg);
    }

    @Override
    public void onProductSelected(Product product) {

    }

    @Override
    public void onProductListChanged() {
        products.clear();
        products.addAll(ProductStore.getInstance(getContext()).getProductsForSale(sale));
        productList.clear();
        updateView();
        listener.onSalesListChanged();
    }

    public Sale getSale(){
        return sale;
    }

    @Override
    public void onProductChanged(Product product) {
        onProductListChanged();
    }
}
