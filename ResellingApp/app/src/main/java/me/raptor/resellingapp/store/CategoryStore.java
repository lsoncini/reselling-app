package me.raptor.resellingapp.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucas on 18/09/2016.
 */
public class CategoryStore extends RaptorStore {

    public static final String CATEGORY_COLUMN_NAME = "name";

    private static CategoryStore mInstance = null;

    public static CategoryStore getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new CategoryStore(context.getApplicationContext());
        }
        return mInstance;
    }

    private CategoryStore(Context context)
    {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CATEGORIES_TABLE_NAME + " (" + CATEGORY_COLUMN_NAME + " text primary key)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME);
        onCreate(db);
    }

    public void addCategory  (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORY_COLUMN_NAME, name);

        db.insert(CATEGORIES_TABLE_NAME, null, contentValues);
    }

    public void deleteCategory (String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CATEGORIES_TABLE_NAME, CATEGORY_COLUMN_NAME + " = ? ", new String[] { name });
    }

    public List<String> getCategories()
    {
        List<String> result_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CATEGORIES_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            result_list.add(res.getString(res.getColumnIndex(CATEGORY_COLUMN_NAME)));
            res.moveToNext();
        }
        return result_list;
    }
}