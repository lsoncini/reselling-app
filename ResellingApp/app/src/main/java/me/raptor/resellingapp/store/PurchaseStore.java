package me.raptor.resellingapp.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import me.raptor.resellingapp.model.Purchase;

/**
 * Created by Lucas on 18/09/2016.
 */
public class PurchaseStore extends RaptorStore {
    public static final String PURCHASES_COLUMN_ID = "purchaseID";
    public static final String PURCHASES_COLUMN_DATE = "date";

    private static PurchaseStore mInstance = null;

    public static PurchaseStore getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new PurchaseStore(context.getApplicationContext());
        }
        return mInstance;
    }

    private PurchaseStore(Context context)
    {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + PURCHASES_TABLE_NAME + " (" +
                        PURCHASES_COLUMN_ID + " integer not null, " +
                        PURCHASES_COLUMN_DATE + " text not null, " +
                        "PRIMARY KEY (" + PURCHASES_COLUMN_ID + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + PURCHASES_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertPurchase (Purchase purchase)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PURCHASES_COLUMN_ID, purchase.getPurchaseID());
        contentValues.put(PURCHASES_COLUMN_DATE, sdf.format(purchase.getDate()));
        db.insert(PURCHASES_TABLE_NAME, null, contentValues);
        return true;
    }

    public Purchase getPurchase(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PURCHASES_TABLE_NAME + " where " + PURCHASES_COLUMN_ID + "="+id, null );
        Purchase purchase = null;
        try {
            purchase = new Purchase(id, sdf.parse(res.getString(res.getColumnIndex(PURCHASES_COLUMN_DATE))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return purchase;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, PURCHASES_TABLE_NAME);
        return numRows;
    }

    public boolean updatePurchase (Purchase purchase)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PURCHASES_COLUMN_DATE, sdf.format(purchase.getDate()));
        db.update(PURCHASES_TABLE_NAME, contentValues, PURCHASES_COLUMN_ID + " = ? ", new String[] { Integer.toString(purchase.getPurchaseID()) } );
        return true;
    }

    public Integer deletePurchase (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PURCHASES_TABLE_NAME,
                PURCHASES_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public List<Purchase> getAllPurchases()
    {
        List<Purchase> purchases_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PURCHASES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            try {
                purchases_list.add(new Purchase(res.getInt(res.getColumnIndex(PURCHASES_COLUMN_ID)),
                        sdf.parse(res.getString(res.getColumnIndex(PURCHASES_COLUMN_DATE)))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return purchases_list;
    }

    public List<Purchase> getAllPurchasesOrdered() {
        List<Purchase> purchases_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + PURCHASES_TABLE_NAME + " order by " + PURCHASES_COLUMN_ID, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            try {
                purchases_list.add(new Purchase(res.getInt(res.getColumnIndex(PURCHASES_COLUMN_ID)),
                        sdf.parse(res.getString(res.getColumnIndex(PURCHASES_COLUMN_DATE)))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return purchases_list;
    }

    public Integer getNextID() {
        if (numberOfRows() == 0){
            return 1;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select max(" + PURCHASES_COLUMN_ID + ") from " + PURCHASES_TABLE_NAME,null);
        res.moveToFirst();
        return res.getInt(0)+1;
    }
}
