package com.example.junhaozeng.testdesign.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by junhaozeng on 2017/9/10.
 */

public class DbManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_health";
    private static final String TABLE_STEPS = "tb_steps";
    private static final String TABLE_HEART = "tb_heart";
    private static final String TABLE_RESPIRATORY = "tb_respiratory";
    private static final String TABLE_BLOODPRESSURE = "tb_bloodpressure";
    private static final String TABLE_O2SATURATION = "tb_oxygensaturation";
    private static final String COL_DATE = "date";
    private static final String COL_STEPS = "steps";
    private static final String COL_HR = "isMeasured";
    private static final String COL_RP = "isMeasured";
    private static final String COL_BP = "isMeasured";
    private static final String COL_O2 = "isMeasured";

    private static final String SQL_CREATE_TABLE_STEPS
            = "CREATE TABLE " + TABLE_STEPS + " ("
            + COL_DATE + " TEXT PRIMARY KEY,"
            + COL_STEPS + " INTEGER" + " )";

    private static final String SQL_CREATE_TABLE_HEART
            = "CREATE TABLE " + TABLE_HEART + " ("
            + COL_DATE + " TEXT PRIMARY KEY,"
            + COL_HR + " TEXT" + " )";

    private static final String SQL_CREATE_TABLE_RESPIRATORY
            = "CREATE TABLE " + TABLE_RESPIRATORY + " ("
            + COL_DATE + " TEXT PRIMARY KEY,"
            + COL_RP + " TEXT" + " )";

    private static final String SQL_CREATE_TABLE_BLOODPRESSURE
            = "CREATE TABLE " + TABLE_BLOODPRESSURE + " ("
            + COL_DATE + " TEXT PRIMARY KEY,"
            + COL_BP + " TEXT" + " )";

    private static final String SQL_CREATE_TABLE_O2SATURATION
            = "CREATE TABLE " + TABLE_O2SATURATION + " ("
            + COL_DATE + " TEXT PRIMARY KEY,"
            + COL_O2 + " TEXT" + " )";

    public DbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_STEPS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_HEART);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_RESPIRATORY);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_BLOODPRESSURE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_O2SATURATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HEART);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPIRATORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOODPRESSURE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_O2SATURATION);
        onCreate(sqLiteDatabase);
    }

    public void insertStepRecord(String date, int steps) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_STEPS, steps);
        sqLiteDatabase.insert(TABLE_STEPS, null, contentValues);
        sqLiteDatabase.close();
    }

    public void insertHeartRecord(String date, String isMeasured) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_HR, isMeasured);
        sqLiteDatabase.insert(TABLE_HEART, null, contentValues);
        sqLiteDatabase.close();
    }

    public void insertRespiratoryRecord(String date, String isMeasured) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_RP, isMeasured);
        sqLiteDatabase.insert(TABLE_RESPIRATORY, null, contentValues);
        sqLiteDatabase.close();
    }

    public void insertBloodPressureRecord(String date, String isMeasured) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_BP, isMeasured);
        sqLiteDatabase.insert(TABLE_BLOODPRESSURE, null, contentValues);
        sqLiteDatabase.close();
    }

    public void insertO2Record(String date, String isMeasured) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_O2, isMeasured);
        sqLiteDatabase.insert(TABLE_O2SATURATION, null, contentValues);
        sqLiteDatabase.close();
    }

    public int readStepRecord(String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int steps = -1;
        Cursor cursor = sqLiteDatabase.query(TABLE_STEPS,
                new String[]{COL_DATE, COL_STEPS},
                COL_DATE + " = ?",
                new String[]{date},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            steps = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STEPS));
        }
        sqLiteDatabase.close();
        return steps;
    }

    public String readHeartRecord(String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String isMeasured = "null";
        Cursor cursor = sqLiteDatabase.query(TABLE_HEART,
                new String[]{COL_DATE, COL_HR},
                COL_DATE + " = ?",
                new String[]{date},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            isMeasured = cursor.getString(cursor.getColumnIndexOrThrow(COL_HR));
        }
        sqLiteDatabase.close();
        return isMeasured;
    }

    public String readRespiratoryRecord(String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String isMeasured = "null";
        Cursor cursor = sqLiteDatabase.query(TABLE_RESPIRATORY,
                new String[]{COL_DATE, COL_RP},
                COL_DATE + " = ?",
                new String[]{date},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            isMeasured = cursor.getString(cursor.getColumnIndexOrThrow(COL_RP));
        }
        sqLiteDatabase.close();
        return isMeasured;
    }

    public String readBloodPressureRecord(String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String isMeasured = "null";
        Cursor cursor = sqLiteDatabase.query(TABLE_BLOODPRESSURE,
                new String[]{COL_DATE, COL_BP},
                COL_DATE + " = ?",
                new String[]{date},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            isMeasured = cursor.getString(cursor.getColumnIndexOrThrow(COL_BP));
        }
        sqLiteDatabase.close();
        return isMeasured;
    }

    public String readO2Record(String date) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String isMeasured = "null";
        Cursor cursor = sqLiteDatabase.query(TABLE_O2SATURATION,
                new String[]{COL_DATE, COL_O2},
                COL_DATE + " = ?",
                new String[]{date},
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            isMeasured = cursor.getString(cursor.getColumnIndexOrThrow(COL_O2));
        }
        sqLiteDatabase.close();
        return isMeasured;
    }

    public int updateStepRecord(String date, int steps) {
        int flag = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_STEPS, steps);
        flag =  sqLiteDatabase.update(TABLE_STEPS, contentValues,
                COL_DATE + "=?", new String[]{date});
        sqLiteDatabase.close();
        return flag;
    }

    public int updateHeartRecord(String date, String isMeasured) {
        int flag = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_HR, isMeasured);
        flag =  sqLiteDatabase.update(TABLE_HEART, contentValues,
                COL_DATE + "=?", new String[]{date});
        sqLiteDatabase.close();
        return flag;
    }

    public int updateRespiratoryRecord(String date, String isMeasured) {
        int flag = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_RP, isMeasured);
        flag =  sqLiteDatabase.update(TABLE_RESPIRATORY, contentValues,
                COL_DATE + "=?", new String[]{date});
        sqLiteDatabase.close();
        return flag;
    }

    public int updateBloodPressureRecord(String date, String isMeasured) {
        int flag = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_BP, isMeasured);
        flag =  sqLiteDatabase.update(TABLE_BLOODPRESSURE, contentValues,
                COL_DATE + "=?", new String[]{date});
        sqLiteDatabase.close();
        return flag;
    }

    public int updateO2Record(String date, String isMeasured) {
        int flag = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_O2, isMeasured);
        flag =  sqLiteDatabase.update(TABLE_O2SATURATION, contentValues,
                COL_DATE + "=?", new String[]{date});
        sqLiteDatabase.close();
        return flag;
    }

    public List<DateStepsPair> readAllStepRecords() {
        List<DateStepsPair> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_STEPS
                + " ORDER BY "
                + COL_DATE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DateStepsPair record = new DateStepsPair();
                record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
                record.setSteps(cursor.getInt(cursor.getColumnIndexOrThrow(COL_STEPS)));
                records.add(record);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return records;
    }

    public List<DateHrPair> readAllHeartRecord() {
        List<DateHrPair> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_HEART
                + " ORDER BY "
                + COL_DATE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DateHrPair record = new DateHrPair();
                record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
                record.setIsMeasured(cursor.getString(cursor.getColumnIndexOrThrow(COL_HR)));
                records.add(record);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return records;
    }

    public List<DateRpPair> readAllRespiratoryRecord() {
        List<DateRpPair> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_RESPIRATORY
                + " ORDER BY "
                + COL_DATE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DateRpPair record = new DateRpPair();
                record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
                record.setIsMeasured(cursor.getString(cursor.getColumnIndexOrThrow(COL_RP)));
                records.add(record);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return records;
    }

    public List<DateBpPair> readAllBloodPressureRecord() {
        List<DateBpPair> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_BLOODPRESSURE
                + " ORDER BY "
                + COL_DATE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DateBpPair record = new DateBpPair();
                record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
                record.setIsMeasured(cursor.getString(cursor.getColumnIndexOrThrow(COL_BP)));
                records.add(record);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return records;
    }

    public List<DateO2Pair> readAllO2Record() {
        List<DateO2Pair> records = new ArrayList<>();
        String selectQuery = "SELECT * FROM "
                + TABLE_O2SATURATION
                + " ORDER BY "
                + COL_DATE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DateO2Pair record = new DateO2Pair();
                record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
                record.setIsMeasured(cursor.getString(cursor.getColumnIndexOrThrow(COL_O2)));
                records.add(record);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return records;
    }

    public void deleteStepRecord(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_STEPS, COL_DATE + " = ?",
                new String[]{date});
        sqLiteDatabase.close();
    }

    public void deleteHeartRecord(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_HEART, COL_DATE + " = ?",
                new String[]{date});
        sqLiteDatabase.close();
    }

    public void deleteRespiratoryRecord(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_RESPIRATORY, COL_DATE + " = ?",
                new String[]{date});
        sqLiteDatabase.close();
    }

    public void deleteBloodPressureRecord(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_BLOODPRESSURE, COL_DATE + " = ?",
                new String[]{date});
        sqLiteDatabase.close();
    }

    public void deleteO2Record(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_O2SATURATION, COL_DATE + " = ?",
                new String[]{date});
        sqLiteDatabase.close();
    }

//    public void insertRecord(String date, int steps) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_DATE, date);
//        contentValues.put(COL_STEPS, steps);
//        sqLiteDatabase.insert(TABLE_STEPS, null, contentValues);
//        sqLiteDatabase.close();
//    }
//
//    public int readRecord(String date) {
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        int steps = -1;
//        Cursor cursor = sqLiteDatabase.query(TABLE_STEPS,
//                new String[]{COL_DATE, COL_STEPS},
//                COL_DATE + " = ?",
//                new String[]{date},
//                null, null, null, null);
//        if (cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            steps = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STEPS));
//        }
//        sqLiteDatabase.close();
//        return steps;
//    }
//
//    public int updateRecord(String date, int steps) {
//        int flag = 0;
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_DATE, date);
//        contentValues.put(COL_STEPS, steps);
//        flag =  sqLiteDatabase.update(TABLE_STEPS, contentValues,
//                COL_DATE + "=?", new String[]{date});
//        sqLiteDatabase.close();
//        return flag;
//    }
//
//    public List<DateStepsPair> readAllRecords() {
//        List<DateStepsPair> records = new ArrayList<>();
//        String selectQuery = "SELECT * FROM "
//                + TABLE_STEPS
//                + " ORDER BY "
//                + COL_DATE;
//        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                DateStepsPair record = new DateStepsPair();
//                record.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)));
//                record.setSteps(cursor.getInt(cursor.getColumnIndexOrThrow(COL_STEPS)));
//                records.add(record);
//            } while (cursor.moveToNext());
//        }
//        sqLiteDatabase.close();
//        return records;
//    }
}
