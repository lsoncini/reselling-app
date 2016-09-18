package me.raptor.resellingapp.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import me.raptor.resellingapp.model.Sale;

/**
 * Created by Lucas on 18/09/2016.
 */
public class SaleStore extends RaptorStore{

    public static final String SALES_COLUMN_ID = "salesID";
    public static final String SALES_COLUMN_DATE = "date";
    public static final String SALES_COLUMN_GROUP = "group";

    private static SaleStore mInstance = null;

    public static SaleStore getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new SaleStore(context.getApplicationContext());
        }
        return mInstance;
    }

    private SaleStore(Context context)
    {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + SALES_TABLE_NAME + " (" +
                        SALES_COLUMN_ID + " integer not null, " +
                        SALES_COLUMN_DATE + " text not null, " +
                        SALES_COLUMN_GROUP + " text not null," +
                        "PRIMARY KEY (" + SALES_COLUMN_ID + "), " +
                        "FOREIGN KEY (" + SALES_COLUMN_GROUP + ") REFERENCES " + GROUPS_TABLE_NAME + " ON DELETE CASCADE ON UPDATE RESTRICT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSale (Sale sale)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SALES_COLUMN_ID, sale.getSaleID());
        contentValues.put(SALES_COLUMN_DATE, sdf.format(sale.getDate()));
        contentValues.put(SALES_COLUMN_GROUP, sale.getGroup());
        db.insert(SALES_TABLE_NAME, null, contentValues);
        return true;
    }

    public Sale getSale(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + SALES_TABLE_NAME + " where " + SALES_COLUMN_ID + "="+id, null );
        Sale sale = null;
        try {
            sale = new Sale(id,
                    sdf.parse(res.getString(res.getColumnIndex(SALES_COLUMN_DATE))),
                    res.getString(res.getColumnIndex(SALES_COLUMN_GROUP)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sale;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SALES_TABLE_NAME);
        return numRows;
    }

    public boolean updateSale (Sale sale)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SALES_COLUMN_DATE, sdf.format(sale.getDate()));
        contentValues.put(SALES_COLUMN_GROUP, sale.getGroup());
        db.update(SALES_TABLE_NAME, contentValues, SALES_COLUMN_ID + " = ? ", new String[] { Integer.toString(sale.getSaleID()) } );
        return true;
    }

    public Integer deleteSale (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(SALES_TABLE_NAME,
                SALES_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public List<Sale> getAllSales()
    {
        List<Sale> sales_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + SALES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            try {
                sales_list.add(new Sale(res.getInt(res.getColumnIndex(SALES_COLUMN_ID)),
                        sdf.parse(res.getString(res.getColumnIndex(SALES_COLUMN_DATE))),
                        res.getString(res.getColumnIndex(SALES_COLUMN_GROUP))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.moveToNext();
        }
        return sales_list;
    }
}
