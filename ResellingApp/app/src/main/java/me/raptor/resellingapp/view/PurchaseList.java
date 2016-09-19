package me.raptor.resellingapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Purchase;

/**
 * Created by Lucas on 18/09/2016.
 */
public class PurchaseList extends FrameLayout {
    public interface PurchaseListListener {
        void onPurchaseSelected(Purchase purchase);
        void onPurchasesListChanged();
    }

    @BindView(R.id.list) ListView list;

    PurchaseAdapter adapter;
    PurchaseListListener listener;


    public PurchaseList(Context context) {
        super(context);
        init();
    }

    public PurchaseList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PurchaseList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.purchase_list, null));
        ButterKnife.bind(this);

        list.setFocusable(false);

        list.setAdapter(adapter = new PurchaseAdapter(getContext()));
        list.setOnItemClickListener(onListItemClick);
    }

    public void clear() {
        adapter.clear();
    }

    public void addPurchases(List<Purchase> purchases) {
        adapter.addAll(purchases);
    }

    public void setPurchases(List<Purchase> purchases) {
        adapter.clear();
        adapter.addAll(purchases);
    }

    public void setListener(PurchaseListListener listener) {
        this.listener = listener;
    }


    private final AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (listener != null)
                listener.onPurchaseSelected(adapter.getItem(position));
        }
    };


    public static class PurchaseAdapter extends ArrayAdapter<Purchase> {

        public PurchaseAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PurchaseListItem view = (convertView != null) ?
                    (PurchaseListItem) convertView :
                    new PurchaseListItem(getContext());

            view.setPurchase(getItem(position));

            return view;
        }
    }
}
