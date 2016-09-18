package me.raptor.resellingapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import me.raptor.resellingapp.R;

public class MainActivity extends Activity {

    @BindView(R.id.purchasesButton) LinearLayout purchases;
    @BindView(R.id.salesButton) LinearLayout sales;
    @BindView(R.id.statsButton) LinearLayout stats;
    @BindView(R.id.clientsButton) LinearLayout clients;
    @BindView(R.id.settingsButton) LinearLayout settings;
    @BindView(R.id.productsButton) LinearLayout products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        purchases.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, PurchasesActivity.class);
                //startActivity(intent);
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, SalesActivity.class);
                //startActivity(intent);
            }
        });
        stats.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                //startActivity(intent);
            }
        });
        clients.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this,ClientsActivity.class);
                //startActivity(intent);
            }
        });
        products.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this,ProductListActivity.class);
                //startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                //startActivity(intent);
            }
        });
    }
}
