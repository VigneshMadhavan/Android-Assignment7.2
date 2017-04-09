package com.vigneshtraining.assignment72.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vigneshtraining.assignment72.utils.Constants;

/**
 * Created by vimadhavan on 4/9/2017.
 */

public class DBhelper extends SQLiteOpenHelper {
    private Context context;
    public DBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String table1 = "CREATE TABLE IF NOT EXISTS " + Constants.PRODUCT_TABLE_NAME + " ("
                + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.PRODUCT_NAME + " TEXT)";

        db.execSQL(table1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Constants.PRODUCT_TABLE_NAME);
        onCreate(db);
    }
}
