package me.raptor.resellingapp.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import butterknife.BindString;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Product;
import me.raptor.resellingapp.model.Sale;
import me.raptor.resellingapp.store.GroupStore;
import me.raptor.resellingapp.store.SaleStore;
import me.raptor.resellingapp.view.ProductListListener;
import me.raptor.resellingapp.view.SaleList;

public class SalesActivity extends AppCompatActivity implements SaleList.SaleListListener, ProductListListener {

    SaleListFragment slf;
    SaleDetailFragment sdf;
    List<String> groups;

    @BindString(R.string.new_group) String new_group;
    @BindString(R.string.choose_group) String choose_group;
    @BindString(R.string.edit_product_confirm) String ok;
    @BindString(R.string.edit_product_cancel) String cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        groups = GroupStore.getInstance(getApplicationContext()).getGroups();
        groups.add(new_group);
        if (savedInstanceState == null) {
            slf = new SaleListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, slf)
                    .commit()
            ;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_purchases, menu);
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        MenuItem saveItem = menu.findItem(R.id.action_save_button);
        MenuItem newItem = menu.findItem(R.id.action_new_button);
        MenuItem editItem = menu.findItem(R.id.action_edit_button);
        if(fragment instanceof SaleListFragment){
            saveItem.setVisible(false);
            newItem.setVisible(true);
            editItem.setVisible(false);
        } else if(fragment instanceof SaleDetailFragment){
            saveItem.setVisible(false);
            newItem.setVisible(true);
            editItem.setVisible(false);
        } else if (fragment instanceof ProductDetailFragment){
            saveItem.setVisible(false);
            newItem.setVisible(false);
            editItem.setVisible(true);
        } else if (fragment instanceof SaleProductEditFragment){
            saveItem.setVisible(false);
            newItem.setVisible(false);
            editItem.setVisible(false);
        } else if(fragment instanceof SaleEditFragment){
            saveItem.setVisible(false);
            newItem.setVisible(false);
            editItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onSaleSelected(Sale sale) {
        sdf = new SaleDetailFragment().setSale(sale);
        sdf.setSalesListener(this);
        navTo(sdf);
    }

    @Override
    public void onSalesListChanged() {
        slf.onSalesListChanged();
    }

    void navTo(LoadingFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("")
                .commit()
        ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if(fragment instanceof SaleListFragment) {
            if (item.getItemId() == R.id.action_new_button) {
//                if(groups.size()==1) {
//                    newGroupDialog();
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle(choose_group);
//                    builder.setItems(groups.toArray(new String[groups.size()]), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (which == groups.size() - 1) {
//                                dialog.cancel();
//                                newGroupDialog();
//                            }
//                            SaleStore saleStore = SaleStore.getInstance(getApplicationContext());
//                            Sale sale = new Sale(saleStore.getNextID(), new Date(), groups.get(which));
//                            saleStore.insertSale(sale);
//                            newSale(sale);
//                            dialog.cancel();
//                        }
//                    });
//                    AlertDialog alertDialog = builder.create();
//                    alertDialog.show();
//                }
                SaleEditFragment f = new SaleEditFragment();
                f.setNew(true);
                f.setSaleListener((SaleListFragment)fragment);
                navTo(f);
                Toast.makeText(getApplicationContext(), "nueva venta", Toast.LENGTH_SHORT).show();
            }
        } else if(fragment instanceof SaleDetailFragment){
            if (item.getItemId() == R.id.action_new_button) {
                SaleProductEditFragment f  = new SaleProductEditFragment();
                f.setSale(((SaleDetailFragment)fragment).getSale());
                f.setProductListener((SaleDetailFragment)fragment);
                navTo(f);
                Toast.makeText(getApplicationContext(), "nueva venta", Toast.LENGTH_SHORT).show();
            }
        } else if(fragment instanceof ProductDetailFragment){
            if (item.getItemId() == R.id.action_edit_button) {
                SaleProductEditFragment f = new SaleProductEditFragment();
                f.setProduct(((ProductDetailFragment)fragment).product);
                f.setProductListener((ProductDetailFragment)fragment);
                f.setEditing(true);
                navTo(f);
                Toast.makeText(getApplicationContext(), "editando venta", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    private void newGroupDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.new_group_dialog);

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.name);
        Button okButton = (Button) dialog.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupStr = text.getText().toString();
                if(groupStr.isEmpty()){
                    Toast.makeText(getApplicationContext(),"YOU MUST ENTER GROUP", Toast.LENGTH_SHORT).show();
                    return;
                }
                GroupStore.getInstance(getApplicationContext()).addGroup(groupStr);
                SaleStore saleStore = SaleStore.getInstance(getApplicationContext());
                Sale sale = new Sale(saleStore.getNextID(),new Date(),groupStr);
                saleStore.insertSale(sale);
                newSale(sale);
                dialog.dismiss();
            }
        });
        Button cancelButton = (Button) dialog.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void newSale(Sale sale){
        sdf = new SaleDetailFragment().setSale(sale);
        onSalesListChanged();
        sdf.setSalesListener(this);
        navTo(sdf);
        Toast.makeText(getApplicationContext(),   "nueva venta", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onProductSelected(Product product) {
        navTo(new ProductDetailFragment().setProduct(product));
    }

    @Override
    public void onProductListChanged() {
        sdf.onProductListChanged();
        Toast.makeText(getApplicationContext(), "CALLING...", Toast.LENGTH_SHORT).show();
    }
}
