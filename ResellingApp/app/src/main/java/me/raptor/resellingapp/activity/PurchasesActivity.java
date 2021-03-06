package me.raptor.resellingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Product;
import me.raptor.resellingapp.model.Purchase;
import me.raptor.resellingapp.view.ProductListListener;
import me.raptor.resellingapp.view.PurchaseList;

public class PurchasesActivity extends AppCompatActivity implements PurchaseList.PurchaseListListener, ProductListListener {

    PurchaseListFragment plf;
    PurchaseDetailFragment pdf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchases);
        if (savedInstanceState == null) {
            plf = new PurchaseListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, plf)
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
        if(fragment instanceof PurchaseListFragment){
            saveItem.setVisible(false);
            newItem.setVisible(true);
            editItem.setVisible(false);
        } else if(fragment instanceof PurchaseDetailFragment){
            saveItem.setVisible(false);
            newItem.setVisible(true);
            editItem.setVisible(false);
        } else if (fragment instanceof ProductDetailFragment){
            saveItem.setVisible(false);
            newItem.setVisible(false);
            editItem.setVisible(true);
        } else if (fragment instanceof PurchaseProductEditFragment){
            saveItem.setVisible(false);
            newItem.setVisible(false);
            editItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onPurchaseSelected(Purchase purchase) {
        pdf = new PurchaseDetailFragment().setPurchase(purchase);
        pdf.setPurchasesListener(this);
        navTo(pdf);
    }

    @Override
    public void onPurchasesListChanged() {
        plf.onPurchasesListChanged();
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
        if(fragment instanceof PurchaseListFragment) {
            if (item.getItemId() == R.id.action_new_button) {
                Toast.makeText(getApplicationContext(), "nueva compra", Toast.LENGTH_SHORT).show();
                pdf = new PurchaseDetailFragment().newPurchase();
                onPurchasesListChanged();
                pdf.setPurchasesListener(this);
                navTo(pdf);
            }
        } else if(fragment instanceof PurchaseDetailFragment){
            if (item.getItemId() == R.id.action_new_button) {
                PurchaseProductEditFragment f  = new PurchaseProductEditFragment();
                f.setPurchase(((PurchaseDetailFragment)fragment).getPurchase());
                f.setProductListener((PurchaseDetailFragment)fragment);
                f.setNew(true);
                navTo(f);
                Toast.makeText(getApplicationContext(), "nuevo producto", Toast.LENGTH_SHORT).show();
            }
        } else if(fragment instanceof ProductDetailFragment){
            if (item.getItemId() == R.id.action_edit_button) {
                PurchaseProductEditFragment f = new PurchaseProductEditFragment();
                f.setProduct(((ProductDetailFragment)fragment).product);
                f.setProductListener((ProductDetailFragment)fragment);
                navTo(f);
                Toast.makeText(getApplicationContext(), "editando producto", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    public void onProductSelected(Product product) {
        navTo(new ProductDetailFragment().setProduct(product));
    }

    @Override
    public void onProductListChanged() {
        pdf.onProductListChanged();
        Toast.makeText(getApplicationContext(), "CALLING...", Toast.LENGTH_SHORT).show();
    }
}
