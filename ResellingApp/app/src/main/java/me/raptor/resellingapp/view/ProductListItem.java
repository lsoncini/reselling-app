package me.raptor.resellingapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Product;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ProductListItem extends FrameLayout{

    @BindView(R.id.product_name)
    TextView name;
    @BindView(R.id.product_price)
    TextView price;
    @BindView(R.id.image)
    CircleImageView imageView;

    private Product product;

    public ProductListItem(Context context) {
        super(context);
        init();
    }

    public ProductListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProductListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        addView(inflate(getContext(), R.layout.product_small_list_item, null));
        ButterKnife.bind(this);
    }

    void setProduct(Product product) {
        this.product = product;
        name.setText(product.getName());
        String msg = "$" + Math.ceil(product.getSalePrice());
        price.setText(msg);
    }
}
