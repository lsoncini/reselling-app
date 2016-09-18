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
public class GroupStore extends RaptorStore {

    public static final String GROUP_COLUMN_NAME = "name";

    private static GroupStore mInstance = null;

    public static GroupStore getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new GroupStore(context.getApplicationContext());
        }
        return mInstance;
    }

    private GroupStore(Context context)
    {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + GROUPS_TABLE_NAME + " (" + GROUP_COLUMN_NAME + " text primary key)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + GROUPS_TABLE_NAME);
        onCreate(db);
    }

    public void addGroup  (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GROUP_COLUMN_NAME, name);

        db.insert(GROUPS_TABLE_NAME, null, contentValues);
    }

    public void deleteGroups (String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(GROUPS_TABLE_NAME, GROUP_COLUMN_NAME + " = ? ", new String[] { name });
    }

    public List<String> getGroups()
    {
        List<String> result_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + GROUPS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            result_list.add(res.getString(res.getColumnIndex(GROUP_COLUMN_NAME)));
            res.moveToNext();
        }
        return result_list;
    }
}