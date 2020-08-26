package com.example.myapp.parkme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import java.util.ArrayList;

public class DataBaseService {
    private DataBaseHelper helper;
    private SQLiteDatabase dbWrite;
    private SQLiteDatabase dbRead;

    public DataBaseService(Context context){
        helper= new DataBaseHelper(context);
        dbWrite = helper.getWritableDatabase();
        dbRead = helper.getReadableDatabase();
    }

    public boolean savePark(String carId, String carDescription, String driverName, String parkLoc, String parkName, String time){
        ContentValues values = new ContentValues();
        values.put(Helper.TablePark.COLUMN_NAME_TIMESTAMP, time);
        values.put(Helper.TablePark.COLUMN_NAME_CAR_ID, carId);
        values.put(Helper.TablePark.COLUMN_NAME_DESCRIPTION, carDescription);
        values.put(Helper.TablePark.COLUMN_NAME_DRIVER_NAME, driverName);
        values.put(Helper.TablePark.COLUMN_NAME_PARK_LOC, parkLoc);
        values.put(Helper.TablePark.COLUMN_NAME_PARK_NAME, parkName);
        long newRowId;
        newRowId = dbWrite.insert(
                Helper.TablePark.TABLE_NAME,
                null,
                values);
        return (newRowId != -1);
    }

    public ArrayList<JSONObject> getParksByDriverName(String driverName){
        Cursor cursor =
                dbRead.query(Helper.TablePark.TABLE_NAME, // a. table
                        new String[]{
                                Helper.TablePark.COLUMN_NAME_DESCRIPTION,
                                Helper.TablePark.COLUMN_NAME_CAR_ID,
                                Helper.TablePark.COLUMN_NAME_TIMESTAMP,
                                Helper.TablePark.COLUMN_NAME_DRIVER_NAME,
                                Helper.TablePark.COLUMN_NAME_PARK_NAME,
                                Helper.TablePark.COLUMN_NAME_PARK_LOC
                        }, // b. column names
                        Helper.TablePark.COLUMN_NAME_DRIVER_NAME + "=?", // c. selections
                        new String[]{driverName}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        Helper.TablePark.COLUMN_NAME_PARK_ID + " DESC", // g. order by
                        null); // h. limit
        return convertCursorToJsonArray(cursor);
    }
    public ArrayList<JSONObject> getParksByCarId(String carId){
        Cursor cursor =
                dbRead.query(Helper.TablePark.TABLE_NAME, // a. table
                        new String[]{
                                Helper.TablePark.COLUMN_NAME_DESCRIPTION,
                                Helper.TablePark.COLUMN_NAME_TIMESTAMP,
                                Helper.TablePark.COLUMN_NAME_CAR_ID,
                                Helper.TablePark.COLUMN_NAME_DRIVER_NAME,
                                Helper.TablePark.COLUMN_NAME_PARK_NAME,
                                Helper.TablePark.COLUMN_NAME_PARK_LOC
                        }, // b. column names
                        Helper.TablePark.COLUMN_NAME_CAR_ID + "=?", // c. selections
                        new String[]{carId}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        Helper.TablePark.COLUMN_NAME_PARK_ID  + " DESC", // g. order by
                        null); // h. limit
        return convertCursorToJsonArray(cursor);
    }
    public JSONObject getLastPark(){
        Cursor cursor =
                dbRead.query(Helper.TablePark.TABLE_NAME, // a. table
                        new String[]{
                                Helper.TablePark.COLUMN_NAME_DESCRIPTION,
                                Helper.TablePark.COLUMN_NAME_TIMESTAMP,
                                Helper.TablePark.COLUMN_NAME_CAR_ID,
                                Helper.TablePark.COLUMN_NAME_DRIVER_NAME,
                                Helper.TablePark.COLUMN_NAME_PARK_NAME,
                                Helper.TablePark.COLUMN_NAME_PARK_LOC
                        }, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        Helper.TablePark.COLUMN_NAME_PARK_ID  + " DESC", // g. order by
                        "1"); // h. limit
        return convertCursorToJsonObject(cursor);
    }

    public JSONObject getCarsDescription(){
        Cursor cursor =
                dbRead.query(Helper.TablePark.TABLE_NAME, // a. table
                        new String[]{"DISTINCT " + Helper.TablePark.COLUMN_NAME_CAR_ID, Helper.TablePark.COLUMN_NAME_DESCRIPTION}, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        return convertCursorToJsonObject(cursor);
    }

    public JSONObject getDriversNames(){
        Cursor cursor =
                dbRead.query(Helper.TablePark.TABLE_NAME, // a. table
                        new String[]{"DISTINCT " + Helper.TablePark.COLUMN_NAME_DRIVER_NAME}, // b. column names
                        null, // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        return convertCursorToJsonObject(cursor);
    }


    public ArrayList<JSONObject> convertCursorToJsonArray(Cursor cursor)
    {
        ArrayList<JSONObject> resultSet = new ArrayList<>();
        while (cursor.moveToNext()) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if(cursor.getString(i) != null){
                            rowObject.put(cursor.getColumnName(i),cursor.getString(i));
                        }
                        else{
                            rowObject.put(cursor.getColumnName(i),"");
                        }
                    } catch (Exception e) {
                }
            }
            }
            resultSet.add(rowObject);
        }
        cursor.close();
        return resultSet;
    }

    public JSONObject convertCursorToJsonObject(Cursor cursor)
    {
        JSONObject resultSet = new JSONObject();
        if (cursor.moveToFirst()) {
            int totalColumn = cursor.getColumnCount();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if(cursor.getString(i) != null){
                            resultSet.put(cursor.getColumnName(i),cursor.getString(i));
                        }
                        else{
                            resultSet.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        cursor.close();
        return resultSet;
    }
    public void clearDb(){
        helper.onUpgrade(dbRead,1,2);
    }



}
