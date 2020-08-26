package com.example.myapp.parkme;

import android.provider.BaseColumns;

public final class Helper {

    public static final String DB_NAME = "MyDatabase.db";
    /* Inner class that defines the table contents */

    public static final String PREFS_NAME = "myPrefFile";

    public static  abstract  class  PreferencesNames{
        public static final String CAR_ID = "car_id";
        public static final String DRIVER_NAME = "driver_name";
        public static final String DESCRIPTION = "car_description";
        public static final String PARK_LOC = "park_loc";
        public static final String PARK_NAME = "park_name";

    }
    public static abstract class TablePark implements BaseColumns {
        public static final String TABLE_NAME = "parks";
        public static final String COLUMN_NAME_PARK_ID = "park_id";
        public static final String COLUMN_NAME_TIMESTAMP ="timestamp";
        public static final String COLUMN_NAME_CAR_ID = "car_id";
        public static final String COLUMN_NAME_DESCRIPTION = "car_description";
        public static final String COLUMN_NAME_DRIVER_NAME = "driver_name";
        public static final String COLUMN_NAME_PARK_LOC = "park_loc";
        public static final String COLUMN_NAME_PARK_NAME = "park_name";
    }



}
