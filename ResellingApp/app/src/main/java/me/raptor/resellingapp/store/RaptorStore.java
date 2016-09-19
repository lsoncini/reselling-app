package me.raptor.resellingapp.store;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by Lucas on 18/09/2016.
 */
public abstract class RaptorStore extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "MyDBName.db";
    protected static final String PRODUCTS_TABLE_NAME = "products";
    protected static final String CATEGORIES_TABLE_NAME = "categories";
    protected static final String CLIENTS_TABLE_NAME = "clients";
    protected static final String SALES_TABLE_NAME = "sales";
    protected static final String PURCHASES_TABLE_NAME = "purchases";
    protected static final String GROUPS_TABLE_NAME = "groups";
    protected static final SimpleDateFormat sdf= new SimpleDateFormat("dd:MM:yyyy");

    private HashMap hp;

    public RaptorStore(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
}
