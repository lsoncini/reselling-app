package me.raptor.resellingapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import me.raptor.resellingapp.R;

/**
 * Created by Lucas on 18/09/2016.
 */
public class PurchaseProductListItem extends ProductListItem{

    @BindView(R.id.product_price)
    TextView price;

    public PurchaseProductListItem(Context context) {
        super(context);
    }

    public PurchaseProductListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PurchaseProductListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setPrice() {
        Integer price_value = (int)Math.ceil(product.getPurchasePrice());
        String msg = "$" + price_value;
        price.setText(msg);
    }
}
