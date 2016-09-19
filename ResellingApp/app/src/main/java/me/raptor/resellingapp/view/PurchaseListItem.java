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
import me.raptor.resellingapp.model.Purchase;
import me.raptor.resellingapp.store.ProductStore;

/**
 * Created by Lucas on 18/09/2016.
 */
public class PurchaseListItem extends FrameLayout{

    @BindView(R.id.purchase_name) TextView name;
    @BindView(R.id.purchase_count) TextView count;
    @BindView(R.id.principal_text) TextView principalTV;
    @BindView(R.id.secondary_text) TextView secondaryTV;

    @BindString(R.string.product) String productString;
    @BindString(R.string.products) String productsString;
    @BindString(R.string.no_products_yet) String noProductsString;

    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");

    public PurchaseListItem(Context context) {
        super(context);
        init();
    }

    public PurchaseListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PurchaseListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        addView(inflate(getContext(), R.layout.purchase_list_item, null));
        ButterKnife.bind(this);
    }

    void setPurchase(Purchase purchase) {
        String msg = getResources().getString(R.string.purchase) + " " + purchase.getPurchaseID();
        name.setText(msg);

        Integer productCount = ProductStore.getInstance(getContext()).getProductCountForPurchase(purchase);
        String productCountString = String.valueOf(productCount);
        String sub_msg;
        if(productCount == 0) {
            sub_msg = noProductsString;
        } else if (productCount == 1){
            sub_msg = productCountString + " " + productString.toLowerCase() + ".";
        } else{
            sub_msg = productCountString + " " + productsString.toLowerCase() + ".";
        }
        count.setText(sub_msg);

        principalTV.setText(sdf1.format(purchase.getDate()));
        secondaryTV.setText(sdf2.format(purchase.getDate()));
    }
}
