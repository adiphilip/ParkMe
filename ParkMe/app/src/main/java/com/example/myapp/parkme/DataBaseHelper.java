package com.example.myapp.parkme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = Helper.DB_NAME;

    private static final String SQL_CREATE_DATABASE =
            "CREATE TABLE " + Helper.TablePark.TABLE_NAME  + " (" +
                    Helper .TablePark.COLUMN_NAME_PARK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Helper .TablePark.COLUMN_NAME_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + " ," +
                    Helper .TablePark.COLUMN_NAME_CAR_ID + " TEXT" + " ," +
                    Helper .TablePark.COLUMN_NAME_DESCRIPTION + " TEXT" + " ," +
                    Helper .TablePark.COLUMN_NAME_DRIVER_NAME + " TEXT" + " ," +
                    Helper .TablePark.COLUMN_NAME_PARK_LOC + " TEXT" + " ," +
                    Helper .TablePark.COLUMN_NAME_PARK_NAME + " TEXT" + " )";
    private static final String SQL_DELETE_DATABASE =
            "DROP TABLE IF EXISTS " + Helper .TablePark.TABLE_NAME;
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATABASE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_DATABASE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
