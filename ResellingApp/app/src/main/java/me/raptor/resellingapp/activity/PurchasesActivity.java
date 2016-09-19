package me.raptor.resellingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Purchase;
import me.raptor.resellingapp.view.PurchaseList;

public class PurchasesActivity extends AppCompatActivity implements PurchaseList.PurchaseListListener{

    PurchaseListFragment plf;
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
        return true;
    }

    @Override
    public void onPurchaseSelected(Purchase purchase) {
        PurchaseDetailFragment f = new PurchaseDetailFragment().setPurchase(purchase);
        f.setPurchasesListener(this);
        navTo(f);
    }

    @Override
    public void onPurchasesListChanged() {
        Toast.makeText(getApplicationContext(),"HOLA", Toast.LENGTH_SHORT).show();
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
        if(fragment instanceof PurchaseListFragment) {
            if (item.getItemId() == R.id.action_new_button) {
                Toast.makeText(getApplicationContext(), "nueva compra", Toast.LENGTH_SHORT).show();
                PurchaseDetailFragment f = new PurchaseDetailFragment();
                f.setPurchasesListener(this);
                navTo(f);
            }
        } else if(fragment instanceof PurchaseDetailFragment){
            if (item.getItemId() == R.id.action_new_button) {
                Toast.makeText(getApplicationContext(), "nuevo producto", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }
}
