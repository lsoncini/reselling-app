package me.raptor.resellingapp.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.raptor.resellingapp.model.Product;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ProductStore extends RaptorStore {
    public static final String PRODUCTS_COLUMN_ID = "productID";
    public static final String PRODUCTS_COLUMN_NAME = "name";
    public static final String PRODUCTS_COLUMN_CATEGORY = "category";
    public static final String PRODUCTS_COLUMN_BUYER = "buyer";
    public static final String PRODUCTS_COLUMN_SALEID = "saleID";
    public static final String PRODUCTS_COLUMN_PURCHASEID = "purchaseID";
    public static final String PRODUCTS_COLUMN_PURCHASE_PRICE = "purchasePrice";
    public static final String PRODUCTS_COLUMN_SALE_PRICE = "salePrice";

    private static ClientStore clientStore;
    private static SaleStore saleStore;
    private static PurchaseStore purchaseStore;

    private static ProductStore mInstance = null;

    public static ProductStore getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new ProductStore(context.getApplicationContext());
        }
        return mInstance;
    }

    private ProductStore(Context context)
    {
        super(context);
        clientStore = ClientStore.getInstance(context);
        saleStore = SaleStore.getInstance(context);
        purchaseStore = PurchaseStore.getInstance(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CLIENTS_TABLE_NAME + " (" +
                        PRODUCTS_COLUMN_ID + " integer not null, " +
                        PRODUCTS_COLUMN_NAME + " text not null, " +
                        PRODUCTS_COLUMN_CATEGORY + " text not null, " +
                        PRODUCTS_COLUMN_BUYER + " integer, " +
                        PRODUCTS_COLUMN_SALEID + " integer, " +
                        PRODUCTS_COLUMN_PURCHASEID + " integer not null, " +
                        PRODUCTS_COLUMN_PURCHASE_PRICE + " real not null, " +
                        PRODUCTS_COLUMN_SALE_PRICE + " real," +
                        "PRIMARY KEY (" + PRODUCTS_COLUMN_ID + "), " +
                        "FOREIGN KEY (" + PRODUCTS_COLUMN_CATEGORY + ") REFERENCES " + CATEGORIES_TABLE_NAME + " ON DELETE CASCADE ON UPDATE RESTRICT, " +
                        "FOREIGN KEY (" + PRODUCTS_COLUMN_BUYER + ") REFERENCES " + CLIENTS_TABLE_NAME + " ON DELETE CASCADE ON UPDATE RESTRICT, " +
                        "FOREIGN KEY (" + PRODUCTS_COLUMN_SALEID + ") REFERENCES " + SALES_TABLE_NAME + " ON DELETE CASCADE ON UPDATE RESTRICT, " +
                        "FOREIGN KEY (" + PRODUCTS_COLUMN_PURCHASEID + ") REFERENCES " + PURCHASES_TABLE_NAME + " ON DELETE CASCADE ON UPDATE RESTRICT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertProduct (Product product)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCTS_COLUMN_ID, product.getProductID());
        contentValues.put(PRODUCTS_COLUMN_NAME, product.getName());
        contentValues.put(PRODUCTS_COLUMN_CATEGORY, product.getCategory());
        contentValues.put(PRODUCTS_COLUMN_BUYER, product.getBuyer().getClientID());
        contentValues.put(PRODUCTS_COLUMN_SALEID, product.getSale().getSaleID());
        contentValues.put(PRODUCTS_COLUMN_PURCHASEID, product.getPurchase().getPurchaseID());
        contentValues.put(PRODUCTS_COLUMN_PURCHASE_PRICE, product.getPurchasePrice());
        contentValues.put(PRODUCTS_COLUMN_SALE_PRICE, product.getSalePrice());
        db.insert(PRODUCTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Product getProduct(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_ID + "="+id, null );
        return new Product(id,
                res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                res.getString(res.getColumnIndex(PRODUCTS_COLUMN_CATEGORY)),
                clientStore.getClient(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_BUYER))),
                saleStore.getSale(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_SALEID))),
                purchaseStore.getPurchase(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASEID))),
                res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASE_PRICE)),
                res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_SALE_PRICE)));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PRODUCTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateProduct (Product product)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCTS_COLUMN_NAME, product.getName());
        contentValues.put(PRODUCTS_COLUMN_CATEGORY, product.getCategory());
        contentValues.put(PRODUCTS_COLUMN_BUYER, product.getBuyer().getClientID());
        contentValues.put(PRODUCTS_COLUMN_SALEID, product.getSale().getSaleID());
        contentValues.put(PRODUCTS_COLUMN_PURCHASEID, product.getPurchase().getPurchaseID());
        contentValues.put(PRODUCTS_COLUMN_PURCHASE_PRICE, product.getPurchasePrice());
        contentValues.put(PRODUCTS_COLUMN_SALE_PRICE, product.getSalePrice());
        db.update(PRODUCTS_TABLE_NAME, contentValues, PRODUCTS_COLUMN_ID + " = ? ", new String[] { Integer.toString(product.getProductID()) } );
        return true;
    }

    public Integer deleteProduct (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PRODUCTS_TABLE_NAME,
                PRODUCTS_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public List<Product> getAllProducts()
    {
        List<Product> products_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PRODUCTS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            products_list.add(new Product(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_ID)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_CATEGORY)),
                    clientStore.getClient(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_BUYER))),
                    saleStore.getSale(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_SALEID))),
                    purchaseStore.getPurchase(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASEID))),
                    res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASE_PRICE)),
                    res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_SALE_PRICE))));
            res.moveToNext();
        }
        return products_list;
    }
}