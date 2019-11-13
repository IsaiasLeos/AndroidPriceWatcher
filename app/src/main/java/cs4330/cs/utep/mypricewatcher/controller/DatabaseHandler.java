package cs4330.cs.utep.mypricewatcher.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cs4330.cs.utep.mypricewatcher.model.Product;

/**
 * Author Isaias Leos, Stackoverflower
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MyPriceWatcherDB";
    private static final String PRODUCT_TABLE = "products";
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL = "url";
    private static final String KEY_CURRENT = "current";
    private static final String KEY_CHANGE = "change";
    private static final String KEY_DATE = "date";
    private static final String KEY_INITIAL = "initial";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * This method initializes the database by creating
     * the table with all of the fields of an item.
     *
     * @param database a read/write database object
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        String table = "CREATE TABLE " + PRODUCT_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT, "
                + KEY_URL + " TEXT, "
                + KEY_CURRENT + " REAL, "
                + KEY_CHANGE + " REAL, "
                + KEY_DATE + " TEXT, "
                + KEY_INITIAL + " REAL" + ")";
        database.execSQL(table);
    }

    /**
     * This method is called when the database needs to be updated.
     *
     * @param database The database.
     * @param old      The old database version.
     * @param updated  The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int old, int updated) {
        database.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE);
        onCreate(database);
    }

    /**
     * This method creates a list containing all of the elements inside of the database.
     *
     * @return List with all the items
     */
    public List<Product> getAll() {
        List<Product> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PRODUCT_TABLE;
        SQLiteDatabase database = this.getReadableDatabase();
        try (Cursor cursor = database.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String url = cursor.getString(2);
                    double current = cursor.getDouble(3);
                    double change = cursor.getDouble(4);
                    String date = cursor.getString(5);
                    double initial = cursor.getDouble(6);
                    Product item = new Product(name, url, current, change, date, initial, id);
                    list.add(item);
                }
                while (cursor.moveToNext());
            }
        }
        return list;
    }

    /**
     * This method creates a new entry in the database using the item
     * provided in the parameters.
     *
     * @param product item added to the database.
     */
    public void add(Product product) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, product.getName());
        values.put(KEY_URL, product.getURL());
        values.put(KEY_CURRENT, product.getCurrentPrice());
        values.put(KEY_CHANGE, product.getChange());
        values.put(KEY_DATE, product.getDate());
        values.put(KEY_INITIAL, product.getInitialPrice());
        long id = database.insert(PRODUCT_TABLE, null, values);
        product.setId((int) id);
        database.close();
    }

    /**
     * This method deletes the product from the database.
     *
     * @param id the id of the item being deleted
     */
    public void delete(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(PRODUCT_TABLE, KEY_ID + " = ?", new String[]{Integer.toString(id)});
        database.close();
    }

    /**
     * This method updates an product from the database.
     *
     * @param item item being edited.
     */
    public void update(Product item) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_URL, item.getURL());
        values.put(KEY_CURRENT, item.getCurrentPrice());
        values.put(KEY_CHANGE, item.getChange());
        values.put(KEY_DATE, item.getDate());
        values.put(KEY_INITIAL, item.getInitialPrice());
        database.update(PRODUCT_TABLE, values, KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});
        database.close();
    }
}
