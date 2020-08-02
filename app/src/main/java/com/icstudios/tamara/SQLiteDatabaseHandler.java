package com.icstudios.tamara;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "bleDB";
    private static final String TABLE_NAME = "ble";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DATE = "date";
    private static final String KEY_RSSI = "rssi";
    private static final String[] COLUMNS = { KEY_NAME, KEY_ADDRESS, KEY_DATE, KEY_RSSI };

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE ble ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, " + "address TEXT, "
                + "date TEXT, " + "rssi TEXT )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void deleteOne(orderListItem.ScanItem scanItem) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "address = ?", new String[] { String.valueOf(scanItem.getAddress()) });
        db.close();
    }

    public orderListItem.ScanItem getbleResult(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " address = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        orderListItem.ScanItem bleResult = new orderListItem.ScanItem();
        bleResult.setName(cursor.getString(1));
        bleResult.setAddress(cursor.getString(2));
        bleResult.setDate(cursor.getString(3));
        bleResult.setRssi(cursor.getString(4));
        return bleResult;
    }

    public List<orderListItem.ScanItem> allbleResults() {

        List<orderListItem.ScanItem> bleResults = new LinkedList<orderListItem.ScanItem>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        orderListItem.ScanItem bleResult = null;

        if (cursor.moveToFirst()) {
            do {
                bleResult = new orderListItem.ScanItem();
                bleResult.setName(cursor.getString(1));
                bleResult.setAddress(cursor.getString(2));
                bleResult.setDate(cursor.getString(3));
                bleResult.setRssi(cursor.getString(4));
                bleResults.add(bleResult);
            } while (cursor.moveToNext());
        }

        return bleResults;
    }

    public List<orderListItem.ScanItem> allbleResultsByDate() {

        List<orderListItem.ScanItem> bleResults = new LinkedList<orderListItem.ScanItem>();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY datetime(date) DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        orderListItem.ScanItem bleResult = null;

        if (cursor.moveToFirst()) {
            do {
                bleResult = new orderListItem.ScanItem();
                bleResult.setName(cursor.getString(1));
                bleResult.setAddress(cursor.getString(2));
                bleResult.setDate(cursor.getString(3));
                bleResult.setRssi(cursor.getString(4));
                bleResults.add(bleResult);
            } while (cursor.moveToNext());
        }

        return bleResults;
    }

    public void addbleResult(orderListItem.ScanItem bleResult) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, bleResult.getName());
        values.put(KEY_ADDRESS, bleResult.getAddress());
        values.put(KEY_DATE, bleResult.getDate());
        values.put(KEY_RSSI, bleResult.getRssi());
        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public int updatebleResult(orderListItem.ScanItem bleResult) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, bleResult.getName());
        values.put(KEY_ADDRESS, bleResult.getAddress());
        values.put(KEY_DATE, bleResult.getDate());
        values.put(KEY_RSSI, bleResult.getRssi());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "address = ?", // selections
                new String[] { String.valueOf(bleResult.getAddress()) });

        db.close();

        return i;
    }

}