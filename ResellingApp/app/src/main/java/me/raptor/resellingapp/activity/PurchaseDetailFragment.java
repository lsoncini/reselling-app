package me.raptor.resellingapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
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
import me.raptor.resellingapp.view.PurchaseList;

/**
 * Created by Lucas on 19/09/2016.
 */
public class PurchaseDetailFragment extends LoadingFragment {
    public String getTitle() {
        return purchase ==null?"Purchase Details": getResources().getString(R.string.purchase) + " "+ purchase.getPurchaseID();
    }

    public Purchase purchase;
    public List<Product> productList;
    public PurchaseList.PurchaseListListener listener;
    private SimpleDateFormat sdf;

    @BindView(R.id.description_title) TextView data_title;
    @BindView(R.id.description) TextView data;
    @BindView(R.id.product_list_empty_view) TextView emptyView;

    @BindString(R.string.purchase_data_title) String data_title_string;
    @BindString(R.string.date_title) String date_title;
    @BindString(R.string.product_count_title) String count_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_detail, container, false);
        ButterKnife.bind(this, view);
        sdf = new SimpleDateFormat("dd/MM/yy");
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PurchaseStore.getInstance(getContext()).insertPurchase(purchase);
                PurchaseDetailFragment.this.listener.onPurchasesListChanged();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
    }

    @Override
    public void onResume(){
        super.onResume();
        updateView();
    }

    public PurchaseDetailFragment setPurchase(Purchase purchase) {
        this.purchase = purchase;
        this.productList = ProductStore.getInstance(getContext()).getProductsForPurchase(purchase);
        updateView();
        return this;
    }

    public void setPurchasesListener(PurchaseList.PurchaseListListener l){
        listener = l;
    }

    private void updateView() {
        if (getView() == null) return;
        showSpinner();
        if(purchase == null) {
            purchase = new Purchase(PurchaseStore.getInstance(getContext()).getNextID(), new Date());
            productList = ProductStore.getInstance(getContext()).getProductsForPurchase(purchase);
        }
        loadFullPurchase();
        hideSpinner();
    }

    private void loadFullPurchase() {
        if (productList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        this.data_title.setText(data_title_string);
        Integer productCount = ProductStore.getInstance(getContext()).getProductCountForPurchase(purchase);
        StringBuilder msg = new StringBuilder(date_title).append(sdf.format(purchase.getDate())).append('\n').append(count_title).append(productCount);
        this.data.setText(msg.toString());
    }
}
