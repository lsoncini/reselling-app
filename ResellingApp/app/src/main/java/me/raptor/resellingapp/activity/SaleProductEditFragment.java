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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Client;
import me.raptor.resellingapp.model.Product;
import me.raptor.resellingapp.model.Sale;
import me.raptor.resellingapp.store.ClientStore;
import me.raptor.resellingapp.store.ProductStore;

/**
 * Created by Lucas on 23/09/2016.
 */
public class SaleProductEditFragment extends LoadingFragment {

    @BindString(R.string.sale_edition_title) String editing_title;
    @BindString(R.string.new_sale_title) String new_sale_title;
    @BindString(R.string.bad_arguments_msg) String bad_arguments_msg;
    @BindString(R.string.sale_edition_no_name_msg) String no_name_msg;
    @BindString(R.string.no_buyer_msg) String no_buyer_msg;
    @BindString(R.string.no_sp_msg) String no_sp_msg;
    @BindString(R.string.new_client) String new_client;
    @BindString(R.string.no_products_on_sale) String no_products_on_sale;


    @BindView(R.id.name_spinner) Spinner nameSpinner;
    @BindView(R.id.client_alt_menu) EditText buyerTV;
    @BindView(R.id.client_spinner) Spinner buyerSpinner;
    @BindView(R.id.sale_price) EditText salePriceTV;
    @BindView(R.id.ok_button) Button okButton;
    @BindView(R.id.cancel_button) Button cancelButton;
    @BindView(R.id.no_products_view) TextView noProducts;

    private boolean editing = false;
    private Sale sale;
    private OnProductChangeListener listener;
    private List<Client> clients;
    private List<String> clientNames;
    private List<Product> products;
    private List<String> productNames;
    private ProductStore productStore;
    private ClientStore clientStore;
    private boolean productChanged = true;

    private Product product;

    @Override
    public String getTitle() {
        return editing ? editing_title : new_sale_title;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_product_edition, container, false);
        ButterKnife.bind(this, view);
        this.clientStore = ClientStore.getInstance(getContext());
        if(sale == null || sale.getGroup() == null){
            this.clients = clientStore.getAllClients();
        } else{
            this.clients = clientStore.getClientsInGroup(sale.getGroup());
        }
        clientNames = new ArrayList<>();
        for(Client client : clients){
            clientNames.add(client.getName());
        }
        clientNames.add(new_client);
        this.productStore = ProductStore.getInstance(getContext());
        this.products = productStore.getAllProductsOnSale();
        this.productNames = new ArrayList<>();
        for(Product product : products){
            productNames.add(product.getName());
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                if(editing){
                    name = product.getName();
                } else{
                    String nameSpinnerValue = (String) nameSpinner.getSelectedItem();
                    name = (nameSpinnerValue.isEmpty()) ? null : nameSpinnerValue;
                }
                String buyerTV_value = buyerTV.getText().toString();
                String buyerSpinnerValue = (String) buyerSpinner.getSelectedItem();
                String buyer = (buyerTV.getVisibility() == View.VISIBLE) ? buyerTV_value : buyerSpinnerValue;
                if (buyer.isEmpty())
                    buyer = null;
                String spTV_value = salePriceTV.getText().toString();
                Double sp = (spTV_value.isEmpty()) ? null : Double.valueOf(spTV_value);


                if(name == null || buyer == null || sp == null){
                    Toast.makeText(getContext(),bad_arguments_msg,Toast.LENGTH_LONG).show();
                    if (name == null)
                        Toast.makeText(getContext(),no_name_msg,Toast.LENGTH_LONG).show();
                    if (buyer == null)
                        Toast.makeText(getContext(),no_buyer_msg,Toast.LENGTH_LONG).show();
                    if (sp == null)
                        Toast.makeText(getContext(),no_sp_msg,Toast.LENGTH_LONG).show();
                    return;
                }
                Client thisClient;
                if(buyerTV.getVisibility() == View.VISIBLE) {
                    thisClient = new Client(clientStore.getNextID(), buyer, null, null, sale.getGroup());
                    clientStore.insertClient(thisClient);
                } else{
                    thisClient = clients.get(buyerSpinner.getSelectedItemPosition());
                }

                product.setBuyer(thisClient);
                product.setSale(sale);
                product.setSalePrice(sp);
                productStore.updateProduct(product);
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
        getActivity().invalidateOptionsMenu();
    }

    public SaleProductEditFragment setProduct(Product product) {
        this.product = product;
        productChanged = true;
        if (product.getSale() != null)
            sale = product.getSale();
        updateView();
        return this;
    }

    private void updateView() {
        if (getView() == null || !productChanged) return;
        if(!editing){
            if (productNames.size() == 0){
                nameSpinner.setVisibility(View.GONE);
                noProducts.setVisibility(View.VISIBLE);
            }else{
                nameSpinner.setVisibility(View.VISIBLE);
                noProducts.setVisibility(View.GONE);
                ArrayAdapter<String> productsAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, productNames);
                productsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nameSpinner.setAdapter(productsAdapter);
                nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Product product = products.get(position);
                        if(!product.equals(SaleProductEditFragment.this.product) && !editing)
                            setProduct(product);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            if(product!=null)
                nameSpinner.setSelection(products.indexOf(product));
        }else{
            if (product != null) {
                nameSpinner.setVisibility(View.GONE);
                noProducts.setVisibility(View.VISIBLE);
                noProducts.setText(product.getName());
            }
        }
        ArrayAdapter<String> clientsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, clientNames);
        clientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buyerSpinner.setAdapter(clientsAdapter);
        buyerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = clientNames.get(position);
                if (item.equals(new_client)){
                    buyerSpinner.setVisibility(View.GONE);
                    buyerTV.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if(product!=null) {
            Client buyer = product.getBuyer();
            if (buyer != null) {
                buyerTV.setText(buyer.getName(), TextView.BufferType.EDITABLE);
                buyerSpinner.setSelection(clients.indexOf(product.getBuyer()));
            }
            Double sp = product.getSalePrice();
            if(sp != null) {
                salePriceTV.setText(String.valueOf(sp), TextView.BufferType.EDITABLE);
            }
        }

        productChanged = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(product != null)
            setProduct(product);
        getActivity().invalidateOptionsMenu();
    }

    public void setEditing(boolean isEditing){
        editing = isEditing;
    }
    public void setProductListener(OnProductChangeListener l){
        listener = l;
    }
}
