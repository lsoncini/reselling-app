package me.raptor.resellingapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import me.raptor.resellingapp.R;

/**
 * Created by Lucas on 18/09/2016.
 */
public class SaleProductListItem extends ProductListItem{

    @BindView(R.id.product_price)
    TextView price;

    public SaleProductListItem(Context context) {
        super(context);
    }

    public SaleProductListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SaleProductListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPrice() {
        Integer price_value = (int)Math.ceil(product.getSalePrice());
        String msg = "$" + price_value;
        price.setText(msg);
    }
}
