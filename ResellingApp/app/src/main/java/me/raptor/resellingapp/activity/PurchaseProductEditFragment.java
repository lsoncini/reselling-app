package me.raptor.resellingapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Product;
import me.raptor.resellingapp.model.Purchase;
import me.raptor.resellingapp.store.CategoryStore;
import me.raptor.resellingapp.store.ProductStore;

/**
 * Created by Lucas on 23/09/2016.
 */
public class PurchaseProductEditFragment extends LoadingFragment {

    @BindString(R.string.product_edition_title) String editing_title;
    @BindString(R.string.new_product_title) String new_title;
    @BindString(R.string.new_category) String new_category;
    @BindString(R.string.edit_product_name_msg) String nameDefault;
    @BindString(R.string.edit_product_category_msg) String categoryHint;
    @BindString(R.string.edit_product_purchase_price_msg) String purchasePriceDefault;
    @BindString(R.string.edit_product_sale_price_msg) String salePriceDefault;
    @BindString(R.string.bad_arguments_msg) String bad_arguments_msg;
    @BindString(R.string.purchase_edition_no_name_msg) String no_name_msg;
    @BindString(R.string.no_category_msg) String no_category_msg;
    @BindString(R.string.no_pp_msg) String no_pp_msg;


    @BindView(R.id.name) EditText nameTV;
    @BindView(R.id.category_alt_menu) EditText categoryTV;
    @BindView(R.id.category_spinner) Spinner categorySpinner;
    @BindView(R.id.purchase_price) EditText purchasePriceTV;
    @BindView(R.id.sale_price) EditText salePriceTV;
    @BindView(R.id.ok_button) Button okButton;
    @BindView(R.id.cancel_button) Button cancelButton;

    private boolean isNew = false;
    private Purchase purchase;
    private Integer productID = null;
    private OnProductChangeListener listener;
    private List<String> categories;
    private ProductStore productStore;
    private CategoryStore categoryStore;

    private Product product;

    @Override
    public String getTitle() {
        return isNew ? new_title : editing_title;
    }

    public void setNew(boolean isNew){
        this.isNew = isNew;

    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_product_edition, container, false);
        ButterKnife.bind(this, view);
        this.categoryStore = CategoryStore.getInstance(getContext());
        this.categories = categoryStore.getCategories();
        categories.add(new_category);
        this.productStore = ProductStore.getInstance(getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
        getActivity().invalidateOptionsMenu();
    }

    public PurchaseProductEditFragment setProduct(Product product) {
        this.product = product;

        updateView();
        return this;
    }

    private void updateView() {
        if (getView() == null) return;
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoriesAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = categories.get(position);
                if (item.equals(new_category)){
                    categorySpinner.setVisibility(View.GONE);
                    categoryTV.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(product!=null) {
            productID = product.getProductID();
            nameTV.setText(product.getName(), TextView.BufferType.EDITABLE);
            categoryTV.setText(product.getCategory(), TextView.BufferType.EDITABLE);
            categorySpinner.setSelection(categories.indexOf(product.getCategory()));
            purchasePriceTV.setText(String.valueOf(product.getPurchasePrice()), TextView.BufferType.EDITABLE);
            purchase = product.getPurchase();
            Double salePrice = product.getSalePrice();
            if(salePrice != null){
                salePriceTV.setText(String.valueOf(salePrice), TextView.BufferType.EDITABLE);
            }
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productID = productID != null ? productID : productStore.getNextID();
                String nameTV_value = nameTV.getText().toString();
                String name = (nameTV_value.isEmpty()) ? null : nameTV_value;
                String categoryTV_value = categoryTV.getText().toString();
                String categorySpinnerValue = (String) categorySpinner.getSelectedItem();
                String category = (categoryTV.getVisibility() == View.VISIBLE) ? categoryTV_value : categorySpinnerValue;
                if (category.isEmpty())
                    category = null;
                String ppTV_value = purchasePriceTV.getText().toString();
                Double pp = (ppTV_value.isEmpty()) ? null : Double.valueOf(ppTV_value);
                String spTV_value = salePriceTV.getText().toString();
                Double sp = (spTV_value.isEmpty()) ? null : Double.valueOf(spTV_value);

                if(name == null || category == null || pp == null){
                    Toast.makeText(getContext(),bad_arguments_msg,Toast.LENGTH_LONG).show();
                    if (name == null)
                        Toast.makeText(getContext(),no_name_msg,Toast.LENGTH_LONG).show();
                    if (category == null)
                        Toast.makeText(getContext(),no_category_msg,Toast.LENGTH_LONG).show();
                    if (pp == null)
                        Toast.makeText(getContext(),no_pp_msg,Toast.LENGTH_LONG).show();
                    return;
                }


                Product product = new Product(productID, name, category, null, null, purchase, pp, sp);

                if(!categoryStore.hasCategory(category))
                    categoryStore.addCategory(category);

                if(isNew){
                    productStore.insertProduct(product);
                }else{
                    productStore.updateProduct(product);
                }
                listener.onProductChanged(product);
                getActivity().onBackPressed();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(product != null)
            setProduct(product);
        getActivity().invalidateOptionsMenu();
    }

    public void setProductListener(OnProductChangeListener l){
        listener = l;
    }
}
