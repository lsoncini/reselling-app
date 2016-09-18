package me.raptor.resellingapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.store.CategoryStore;
import me.raptor.resellingapp.store.ClientStore;
import me.raptor.resellingapp.store.GroupStore;
import me.raptor.resellingapp.store.ProductStore;
import me.raptor.resellingapp.store.PurchaseStore;
import me.raptor.resellingapp.store.SaleStore;

/**
 * Created by Lucas on 06/09/2016.
 */
public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeSQL(getApplicationContext());
        // Set portrait orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);

        getSharedPreferences("appData", MODE_PRIVATE).edit().putString("version", "1.1.0").commit();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                startMainActivity();
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    private void initializeSQL(Context ctx) {
        CategoryStore.getInstance(ctx);
        GroupStore.getInstance(ctx);
        SaleStore.getInstance(ctx);
        PurchaseStore.getInstance(ctx);
        ClientStore.getInstance(ctx);
        ProductStore.getInstance(ctx);
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent().setClass(
                SplashScreenActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
