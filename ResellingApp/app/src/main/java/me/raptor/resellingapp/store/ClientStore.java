package me.raptor.resellingapp.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.raptor.resellingapp.model.Client;

/**
 * Created by Lucas on 18/09/2016.
 */
public class ClientStore extends RaptorStore {
    public static final String CLIENTS_COLUMN_ID = "clientID";
    public static final String CLIENTS_COLUMN_NAME = "name";
    public static final String CLIENTS_COLUMN_PHONE = "phone";
    public static final String CLIENTS_COLUMN_EMAIL = "email";
    public static final String CLIENTS_COLUMN_GROUP = "groupName";

    private static ClientStore mInstance = null;

    public static ClientStore getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new ClientStore(context.getApplicationContext());
        }
        return mInstance;
    }

    private ClientStore(Context context)
    {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CLIENTS_TABLE_NAME + " (" +
                        CLIENTS_COLUMN_ID + " integer not null, " +
                        CLIENTS_COLUMN_NAME + " text not null, " +
                        CLIENTS_COLUMN_PHONE + " text, " +
                        CLIENTS_COLUMN_EMAIL + " text, " +
                        CLIENTS_COLUMN_GROUP + " text," +
                        "PRIMARY KEY (" + CLIENTS_COLUMN_ID + "), " +
                        "FOREIGN KEY (" + CLIENTS_COLUMN_GROUP + ") REFERENCES " + GROUPS_TABLE_NAME + " ON DELETE CASCADE ON UPDATE RESTRICT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CLIENTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertClient (Client client)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLIENTS_COLUMN_ID, client.getClientID());
        contentValues.put(CLIENTS_COLUMN_NAME, client.getName());
        contentValues.put(CLIENTS_COLUMN_PHONE, client.getPhone());
        contentValues.put(CLIENTS_COLUMN_EMAIL, client.getEmail());
        contentValues.put(CLIENTS_COLUMN_GROUP, client.getGroup());
        db.insert(CLIENTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Client getClient(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CLIENTS_TABLE_NAME + " where " + CLIENTS_COLUMN_ID + "="+id, null );
        return new Client(id,
                res.getString(res.getColumnIndex(CLIENTS_COLUMN_NAME)),
                res.getString(res.getColumnIndex(CLIENTS_COLUMN_PHONE)),
                res.getString(res.getColumnIndex(CLIENTS_COLUMN_EMAIL)),
                res.getString(res.getColumnIndex(CLIENTS_COLUMN_GROUP)));
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CLIENTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateClient (Client client)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLIENTS_COLUMN_NAME, client.getName());
        contentValues.put(CLIENTS_COLUMN_PHONE, client.getPhone());
        contentValues.put(CLIENTS_COLUMN_EMAIL, client.getEmail());
        contentValues.put(CLIENTS_COLUMN_GROUP, client.getGroup());
        db.update(CLIENTS_TABLE_NAME, contentValues, CLIENTS_COLUMN_ID + " = ? ", new String[] { Integer.toString(client.getClientID()) } );
        return true;
    }

    public Integer deleteClient (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CLIENTS_TABLE_NAME,
                CLIENTS_COLUMN_ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public List<Client> getAllClients()
    {
        List<Client> clients_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + CLIENTS_TABLE_NAME, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            clients_list.add(new Client(res.getInt(res.getColumnIndex(CLIENTS_COLUMN_ID)),
                    res.getString(res.getColumnIndex(CLIENTS_COLUMN_NAME)),
                    res.getString(res.getColumnIndex(CLIENTS_COLUMN_PHONE)),
                    res.getString(res.getColumnIndex(CLIENTS_COLUMN_EMAIL)),
                    res.getString(res.getColumnIndex(CLIENTS_COLUMN_GROUP))));
            res.moveToNext();
        }
        return clients_list;
    }
}
