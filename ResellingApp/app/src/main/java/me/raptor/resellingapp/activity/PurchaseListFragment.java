package me.raptor.resellingapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Purchase;
import me.raptor.resellingapp.store.PurchaseStore;
import me.raptor.resellingapp.view.PurchaseList;

/**
 * Created by Lucas on 19/09/2016.
 */
public class PurchaseListFragment extends LoadingFragment implements PurchaseList.PurchaseListListener{
    @BindView(R.id.purchaseList) PurchaseList purchaseList;
    @BindView(R.id.empty_view) RelativeLayout emptyView;

    List<Purchase> purchases = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_list, container, false);
        ButterKnife.bind(this, view);

        purchases = new ArrayList<>();
        purchases.addAll(PurchaseStore.getInstance(getContext()).getAllPurchasesOrdered());

        purchaseList.setListener((PurchaseList.PurchaseListListener) getActivity());
        updateView();

        return view;
    }

    public void updateView() {
        if (getView() == null) return;

        showSpinner();
        purchaseList.setPurchases(purchases);
        if(purchases!=null && purchases.size()==0)
            emptyView.setVisibility(View.VISIBLE);
        else emptyView.setVisibility(View.GONE);
        hideSpinner();
    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.purchases_main_title);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    public void onPurchaseSelected(Purchase purchase) {}

    @Override
    public void onPurchasesListChanged() {
        purchases.clear();
        purchases.addAll(PurchaseStore.getInstance(getContext()).getAllPurchasesOrdered());
        purchaseList.clear();
        updateView();
    }
}
