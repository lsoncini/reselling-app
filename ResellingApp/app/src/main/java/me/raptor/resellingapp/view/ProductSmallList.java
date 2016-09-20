package me.raptor.resellingapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Product;

/**
 * Created by Lucas on 19/09/2015.
 */
public class ProductSmallList extends FrameLayout {

    public interface ProductSmallListListener {
        void onProductSelected(Product product);
        void onProductListChanged();
    }

    @BindView(R.id.list) LinearLayout list;

    Boolean hasProducts = false;
    ProductSmallListListener listener;


    public ProductSmallList(Context context) {
        super(context);
        init();
    }

    public ProductSmallList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProductSmallList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addView(inflate(getContext(), R.layout.product_small_list_layout, null));
        ButterKnife.bind(this);

        list.setFocusable(false);
    }

    public void setProducts(List<Product> products) {
        //adapter.clear();
        //adapter.addAll(contacts);
        if(hasProducts)
            return;

        for (int i = 0; i < products.size(); i++) {
            ProductSmallListItem item = new ProductSmallListItem(getContext());
            item.setProduct(products.get(i));
            list.addView(item);
        }
        hasProducts = true;
    }

    public void setListener(ProductSmallListListener listener) {
        this.listener = listener;
    }

}