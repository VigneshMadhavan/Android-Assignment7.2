package com.vigneshtraining.assignment72.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.SyncStateContract;

import com.vigneshtraining.assignment72.model.Product;
import com.vigneshtraining.assignment72.utils.Constants;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by vimadhavan on 4/9/2017.
 */

public class DBhandler {
    private SQLiteDatabase db;
    private final Context context;
    private final DBhelper dbHelper;
    private static DBhandler db_handler = null;

    public static DBhandler getInstance(Context context){
        try{
            if(db_handler == null){
                db_handler = new DBhandler(context);
                db_handler.open();
            }
        }catch(IllegalStateException e){
            //db_helper already open
        }
        return db_handler;
    }

    public DBhandler(Context context) {

        this.context = context;
        this.dbHelper = new DBhelper(context, Constants.DATABASE_NAME,null,Constants.DATABASE_VERSION);
    }
    public void close() {
        try {
            if (db.isOpen()) {
                db.close();
            }
        }catch (Exception e){

        }

    }

    public boolean dbOpenCheck() {
        try{
            return db.isOpen();
        }catch(Exception e){
            return false;
        }
    }

    /*
     * open database
     */
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            // Log.v("open database Exception", "error==" + e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }

    public long insertContentVals(String tableName, ContentValues content){
        long id=0;
        try{
            id = db.insert(tableName, null, content);
        }catch(Exception e){
            e.printStackTrace();
        }
        return id;
    }

    public Cursor getTableRecords(String tablename, String[] columns, String where, String orderby){
        Cursor cursor =  db.query(false, tablename, columns,where, null, null, null, orderby, null);
        return cursor;
    }

    /*
	 * Get count of all tables in a table as per the condition
	 */

    public int getFullCount(String table, String where) {
        int no = 0;
        Cursor cursor = db.query(false, table, null, where, null, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                no = cursor.getCount();
                cursor.close();
            }
        } finally {
            cursor.close();
        }
        return no;
    }

    public List<Product> getAllProduct(){
        List<Product> products=new LinkedList<Product>();

        String query = "SELECT  * FROM " + Constants.PRODUCT_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);


        Product product=null;

        if (cursor.moveToFirst()) {
            do {
                product = new Product();
                product.setId(cursor.getInt(0));
                product.setName(cursor.getString(1));
                // Add book to books
                products.add(product);
            } while (cursor.moveToNext());
        }


        return products;

    }
    public Boolean isProductExist(String productName){

        String query = "SELECT  * FROM " + Constants.PRODUCT_TABLE_NAME +" WHERE " + Constants.PRODUCT_NAME + " LIKE '" + productName +"'";
        Cursor cursor = db.rawQuery(query, null);
        int no = 0;
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                no = cursor.getCount();
                cursor.close();
            }
        } finally {
            cursor.close();
        }

        if(no>0){
            return true;
        }


        return false;

    }

    public long addProduct(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Constants.PRODUCT_NAME, name);
        return db.insert(Constants.PRODUCT_TABLE_NAME , null, initialValues);
    }


    public void deleteRecords(String table, String whereClause, String[] whereArgs){
        db.delete(table, whereClause, whereArgs);
    }
}
