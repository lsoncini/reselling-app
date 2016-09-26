package me.raptor.resellingapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Sale;
import me.raptor.resellingapp.store.ProductStore;

/**
 * Created by Lucas on 18/09/2016.
 */
public class SaleListItem extends FrameLayout{

    @BindView(R.id.name) TextView name;
    @BindView(R.id.sub) TextView sub;
    @BindView(R.id.principal_text) TextView principalTV;
    @BindView(R.id.secondary_text) TextView secondaryTV;

    @BindString(R.string.product) String productString;
    @BindString(R.string.products) String productsString;
    @BindString(R.string.no_products_yet) String noProductsString;
    @BindString(R.string.sale) String sale_str;

    private SimpleDateFormat sdf1 = new SimpleDateFormat(getResources().getString(R.string.date_format));
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");

    public SaleListItem(Context context) {
        super(context);
        init();
    }

    public SaleListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SaleListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        addView(inflate(getContext(), R.layout.sale_list_item, null));
        ButterKnife.bind(this);
    }

    void setSale(Sale sale) {
        String msg = sale_str + " " + sale.getSaleID();
        name.setText(msg);

        Integer productCount = ProductStore.getInstance(getContext()).getProductCountForSale(sale);
        String productCountString = String.valueOf(productCount);

        if(productCount == 0) {
            productCountString = noProductsString;
        } else if (productCount == 1){
            productCountString += " " + productString.toLowerCase() + ".";
        } else{
            productCountString += " " + productsString.toLowerCase() + ".";
        }

        String sub_msg = sale.getGroup() + " - " + productCountString;
        sub.setText(sub_msg);

        principalTV.setText(sdf1.format(sale.getDate()));
        secondaryTV.setText(sdf2.format(sale.getDate()));
    }
}
