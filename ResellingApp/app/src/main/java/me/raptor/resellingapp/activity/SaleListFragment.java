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
import me.raptor.resellingapp.model.Sale;
import me.raptor.resellingapp.store.SaleStore;
import me.raptor.resellingapp.view.SaleList;

/**
 * Created by Lucas on 19/09/2016.
 */
public class SaleListFragment extends LoadingFragment implements SaleList.SaleListListener{
    @BindView(R.id.saleList) SaleList saleList;
    @BindView(R.id.empty_view) RelativeLayout emptyView;

    List<Sale> sales = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_list, container, false);
        ButterKnife.bind(this, view);

        sales = new ArrayList<>();
        sales.addAll(SaleStore.getInstance(getContext()).getAllSalesOrdered());

        saleList.setListener((SaleList.SaleListListener) getActivity());
        updateView();

        return view;
    }

    public void updateView() {
        if (getView() == null) return;

        showSpinner();
        saleList.setSales(sales);
        if(sales!=null && sales.size()==0)
            emptyView.setVisibility(View.VISIBLE);
        else emptyView.setVisibility(View.GONE);
        hideSpinner();
    }

    @Override
    public String getTitle() {
        return getResources().getString(R.string.sales_main_title);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onSaleSelected(Sale sale) {}

    @Override
    public void onSalesListChanged() {
        sales.clear();
        sales.addAll(SaleStore.getInstance(getContext()).getAllSalesOrdered());
        saleList.clear();
        updateView();
    }
}
