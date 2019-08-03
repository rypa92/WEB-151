package com.example.linda.databasewithlistexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CountriesDbAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_CODE = "code";
    public static final String KEY_NAME = "name";
    public static final String KEY_URI = "uri";

    private static final String TAG = "CountriesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "World";
    private static final String SQLITE_TABLE = "Country";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_CODE + "," +
                    KEY_NAME + "," +
                    KEY_URI+ "," +
                     " UNIQUE (" + KEY_CODE +"));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public CountriesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public CountriesDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createCountry(String code, String name, String uri) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CODE, code);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_URI, uri);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteOneCountry(String inputCode) {
        Cursor myCursor;
        myCursor  = fetchCountriesByName(inputCode);
        if (myCursor.getCount() <= 0) {
            myCursor.close();
            return false;
        } else {
            mDb.delete(SQLITE_TABLE, KEY_CODE + "=?", new String[]{inputCode});
            myCursor.close();
            return true;
        }
    }


    public boolean deleteAllCountries() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME, KEY_URI},
                    null, null, null, null,KEY_CODE,null );
        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME, KEY_URI},
                    KEY_CODE + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public int getNumOfCountry(String code, String name) throws SQLException {
        Log.w(TAG, code + name);
        Cursor mCursor;
        mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_CODE, KEY_NAME, KEY_URI},
                  KEY_CODE + " like '%" + code + "%' or " + KEY_NAME + " LIKE '%"
                          + name + "%'", null,
                  null, null, null, null);
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public Cursor fetchAllCountries() {
        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_CODE, KEY_NAME, KEY_URI},
                null, null, KEY_CODE, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }



    public void insertSomeCountries() {
        createCountry("AFG", "Afghanistan", "http://www.wikipedia.com/wiki/Afghanistan");
        createCountry("ALB", "Albania", "http://www.wikipedia.com/wiki/Albania");
        createCountry("DZA", "Algeria", "http://www.wikipedia.com/wiki/Algeria");
        createCountry("USA", "United States", "http://www.wikipedia.com/wiki/USA");
        createCountry("JPN", "Japan", "http://www.wikipedia.com/wiki/Japan");
        createCountry("CAD", "Canada", "http://www.wikipedia.com/wiki/Canada");
        createCountry("GER", "Germany", "http://www.wikipedia.com/wiki/Germany");
        createCountry("UKM", "United Kingdom", "http://www.wikipedia.com/wiki/United_Kingdom");
    }
}
