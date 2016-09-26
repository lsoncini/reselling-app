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
import me.raptor.resellingapp.model.Sale;

/**
 * Created by Lucas on 25/09/2016.
 */
public class SaleList extends FrameLayout {
    public interface SaleListListener {
        void onSaleSelected(Sale sale);
        void onSalesListChanged();
    }

    @BindView(R.id.list) ListView list;

    SaleAdapter adapter;
    SaleListListener listener;


    public SaleList(Context context) {
        super(context);
        init();
    }

    public SaleList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SaleList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.sale_list, null));
        ButterKnife.bind(this);

        list.setFocusable(false);

        list.setAdapter(adapter = new SaleAdapter(getContext()));
        list.setOnItemClickListener(onListItemClick);
    }

    public void clear() {
        adapter.clear();
    }

    public void addSales(List<Sale> sales) {
        adapter.addAll(sales);
    }

    public void setSales(List<Sale> sales) {
        adapter.clear();
        adapter.addAll(sales);
    }

    public void setListener(SaleListListener listener) {
        this.listener = listener;
    }


    private final AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (listener != null)
                listener.onSaleSelected(adapter.getItem(position));
        }
    };


    public static class SaleAdapter extends ArrayAdapter<Sale> {

        public SaleAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            SaleListItem view = (convertView != null) ?
                    (SaleListItem) convertView :
                    new SaleListItem(getContext());

            view.setSale(getItem(position));

            return view;
        }
    }
}
