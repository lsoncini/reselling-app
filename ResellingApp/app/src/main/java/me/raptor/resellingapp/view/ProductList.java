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
import me.raptor.resellingapp.model.Product;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ProductList extends FrameLayout {
    public interface ProductListListener {
        void onProductSelected(Product product);
        void onProductListChanged();
    }

    @BindView(R.id.list) ListView list;

    ProductAdapter adapter;
    ProductListListener listener;


    public ProductList(Context context) {
        super(context);
        init();
    }

    public ProductList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProductList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.product_list, null));
        ButterKnife.bind(this);

        list.setFocusable(false);

        list.setAdapter(adapter = new ProductAdapter(getContext()));
        list.setOnItemClickListener(onListItemClick);
    }

    public void clear() {
        adapter.clear();
    }

    public void addProducts(List<Product> products) {
        adapter.addAll(products);
    }

    public void setProducts(List<Product> products) {
        adapter.clear();
        adapter.addAll(products);
    }

    public void setListener(ProductListListener listener) {
        this.listener = listener;
    }


    private final AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (listener != null)
                listener.onProductSelected(adapter.getItem(position));
        }
    };


    public static class ProductAdapter extends ArrayAdapter<Product> {

        public ProductAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProductListItem view = (convertView != null) ?
                    (ProductListItem) convertView :
                    new ProductListItem(getContext());

            view.setProduct(getItem(position));

            return view;
        }
    }
}
