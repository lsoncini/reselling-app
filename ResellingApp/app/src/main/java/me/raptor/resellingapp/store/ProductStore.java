package me.raptor.resellingapp.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.raptor.resellingapp.model.Client;
import me.raptor.resellingapp.model.Product;
import me.raptor.resellingapp.model.Purchase;
import me.raptor.resellingapp.model.Sale;

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

    private static ProductStore mInstance = null;
    private Context context;

    public static ProductStore getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new ProductStore(context.getApplicationContext());
        }
        return mInstance;
    }

    private ProductStore(Context context)
    {
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + PRODUCTS_TABLE_NAME + " (" +
                        PRODUCTS_COLUMN_ID + " integer not null, " +
                        PRODUCTS_COLUMN_NAME + " text not null, " +
                        PRODUCTS_COLUMN_CATEGORY + " text not null, " +
                        PRODUCTS_COLUMN_BUYER + " integer, " +
                        PRODUCTS_COLUMN_SALEID + " integer, " +
                        PRODUCTS_COLUMN_PURCHASEID + " integer not null, " +
                        PRODUCTS_COLUMN_PURCHASE_PRICE + " real not null, " +
                        PRODUCTS_COLUMN_SALE_PRICE + " real, " +
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
        Client client = product.getBuyer();
        contentValues.put(PRODUCTS_COLUMN_BUYER, client==null?null:client.getClientID());
        Sale sale = product.getSale();
        contentValues.put(PRODUCTS_COLUMN_SALEID, sale == null?null:sale.getSaleID());
        contentValues.put(PRODUCTS_COLUMN_PURCHASEID, product.getPurchase().getPurchaseID());
        contentValues.put(PRODUCTS_COLUMN_PURCHASE_PRICE, product.getPurchasePrice());
        contentValues.put(PRODUCTS_COLUMN_SALE_PRICE, product.getSalePrice());
        db.insert(PRODUCTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Product getProduct(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_ID + "="+id, null );
        res.moveToFirst();
        Integer buyerIndex = res.getColumnIndex(PRODUCTS_COLUMN_BUYER);
        Integer saleIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALEID);
        Integer salePriceIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALE_PRICE);
        return new Product(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_ID)),
                res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                res.getString(res.getColumnIndex(PRODUCTS_COLUMN_CATEGORY)),
                res.isNull(buyerIndex)? null : ClientStore.getInstance(context).getClient(res.getInt(buyerIndex)),
                res.isNull(saleIndex) ? null : SaleStore.getInstance(context).getSale(res.getInt(saleIndex)),
                PurchaseStore.getInstance(context).getPurchase(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASEID))),
                res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASE_PRICE)),
                res.isNull(salePriceIndex) ? null : res.getDouble(salePriceIndex));
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
        Client client = product.getBuyer();
        contentValues.put(PRODUCTS_COLUMN_BUYER, client==null? null : client.getClientID());
        Sale sale = product.getSale();
        contentValues.put(PRODUCTS_COLUMN_SALEID, sale == null ? null : sale.getSaleID());
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

        while(!res.isAfterLast()){
            Integer buyerIndex = res.getColumnIndex(PRODUCTS_COLUMN_BUYER);
            Integer saleIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALEID);
            Integer salePriceIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALE_PRICE);
            products_list.add(new Product(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_ID)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_CATEGORY)),
                    res.isNull(buyerIndex)? null : ClientStore.getInstance(context).getClient(res.getInt(buyerIndex)),
                    res.isNull(saleIndex) ? null : SaleStore.getInstance(context).getSale(res.getInt(saleIndex)),
                    PurchaseStore.getInstance(context).getPurchase(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASEID))),
                    res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASE_PRICE)),
                    res.isNull(salePriceIndex) ? null : res.getDouble(salePriceIndex)));
            res.moveToNext();
        }
        return products_list;
    }

    public Integer getProductCountForPurchase(Purchase purchase) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select count(*) from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_PURCHASEID + "="+purchase.getPurchaseID(), null );
        res.moveToFirst();
        return res.getInt(0);
    }

    public List<Product> getProductsForPurchase(Purchase purchase) {
        List<Product> products_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_PURCHASEID + "=" + purchase.getPurchaseID(), null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Integer buyerIndex = res.getColumnIndex(PRODUCTS_COLUMN_BUYER);
            Integer saleIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALEID);
            Integer salePriceIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALE_PRICE);
            products_list.add(new Product(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_ID)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_CATEGORY)),
                    res.isNull(buyerIndex)? null : ClientStore.getInstance(context).getClient(res.getInt(buyerIndex)),
                    res.isNull(saleIndex) ? null : SaleStore.getInstance(context).getSale(res.getInt(saleIndex)),
                    purchase,
                    res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASE_PRICE)),
                    res.isNull(salePriceIndex) ? null : res.getDouble(salePriceIndex)));
            res.moveToNext();
        }
        return products_list;
    }

    public Integer getNextID(){
        return numberOfRows()+1;
    }

    public Integer getInvestmentForPurchase(Purchase purchase) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select total(" + PRODUCTS_COLUMN_PURCHASE_PRICE + ") from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_PURCHASEID + "="+purchase.getPurchaseID(), null );
        res.moveToFirst();
        return res.getInt(0);
    }


    public Integer getProductCountForSale(Sale sale) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select count(*) from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_SALEID + "="+sale.getSaleID(), null );
        res.moveToFirst();
        return res.getInt(0);
    }

    public List<Product> getProductsForSale(Sale sale) {
        List<Product> products_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_SALEID + "=" + sale.getSaleID(), null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Integer buyerIndex = res.getColumnIndex(PRODUCTS_COLUMN_BUYER);
            Integer saleIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALEID);
            Integer salePriceIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALE_PRICE);
            products_list.add(new Product(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_ID)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_CATEGORY)),
                    ClientStore.getInstance(context).getClient(res.getInt(buyerIndex)),
                    sale,
                    PurchaseStore.getInstance(context).getPurchase(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASEID))),
                    res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASE_PRICE)),
                    res.getDouble(salePriceIndex)));
            res.moveToNext();
        }
        return products_list;
    }

    public Integer getTotalForSale(Sale sale) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select total(" + PRODUCTS_COLUMN_SALE_PRICE + ") from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_SALEID + "="+sale.getSaleID(), null );
        res.moveToFirst();
        return res.getInt(0);
    }

    public List<Product> getAllProductsOnSale() {
        List<Product> products_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PRODUCTS_TABLE_NAME + " where " + PRODUCTS_COLUMN_SALEID + " IS NULL order by " + PRODUCTS_COLUMN_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Integer buyerIndex = res.getColumnIndex(PRODUCTS_COLUMN_BUYER);
            Integer saleIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALEID);
            Integer salePriceIndex = res.getColumnIndex(PRODUCTS_COLUMN_SALE_PRICE);
            products_list.add(new Product(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_ID)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(PRODUCTS_COLUMN_CATEGORY)),
                    res.isNull(buyerIndex)? null : ClientStore.getInstance(context).getClient(res.getInt(buyerIndex)),
                    res.isNull(saleIndex) ? null : SaleStore.getInstance(context).getSale(res.getInt(saleIndex)),
                    PurchaseStore.getInstance(context).getPurchase(res.getInt(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASEID))),
                    res.getDouble(res.getColumnIndex(PRODUCTS_COLUMN_PURCHASE_PRICE)),
                    res.isNull(salePriceIndex) ? null : res.getDouble(salePriceIndex)));
            res.moveToNext();
        }
        return products_list;
    }
}
