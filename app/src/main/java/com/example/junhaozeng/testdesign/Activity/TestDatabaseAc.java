package com.example.junhaozeng.testdesign.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.Utils.DateHrPair;
import com.example.junhaozeng.testdesign.Utils.DateStepsPair;
import com.example.junhaozeng.testdesign.Utils.DbManager;

import java.util.List;

public class TestDatabaseAc extends AppCompatActivity {

    private static final String TAG = "TestingDb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);
        test();
    }

    private void test() {
        DbManager dbManager = new DbManager(this);
        List<DateStepsPair> stepRecords;
        List<DateHrPair> heartRecords;

        Log.d(TAG, "start");

        dbManager.insertStepRecord("2017-10-01", 4350);
        dbManager.insertStepRecord("2017-10-02", 1234);
        dbManager.insertStepRecord("2017-10-03", 43152);
        dbManager.insertStepRecord("2017-10-04", 135123);
        dbManager.insertStepRecord("2017-10-05", 123);
        dbManager.insertStepRecord("2017-10-06", 2592);
        dbManager.insertStepRecord("2017-10-07", 1205);

        Log.d(TAG, "step inserted");

        dbManager.insertHeartRecord("2017-10-01", "true");
        dbManager.insertHeartRecord("2017-10-02", "true");
        dbManager.insertHeartRecord("2017-10-03", "true");
        dbManager.insertHeartRecord("2017-10-04", "true");
        dbManager.insertHeartRecord("2017-10-05", "true");
        dbManager.insertHeartRecord("2017-10-06", "true");
        dbManager.insertHeartRecord("2017-10-07", "true");

        Log.d(TAG, "heart inserted");

        Log.d(TAG, String.valueOf(dbManager.readStepRecord("2017-10-01")));
        Log.d(TAG, dbManager.readHeartRecord("2017-10-01"));
        Log.d(TAG, String.valueOf(dbManager.readStepRecord("2017-10-08")));
        Log.d(TAG, dbManager.readHeartRecord("2017-10-08"));

        Log.d(TAG, String.valueOf(dbManager.updateStepRecord("2017-10-01", 0)));
        Log.d(TAG, String.valueOf(dbManager.updateHeartRecord("2017-10-01", "null")));
        Log.d(TAG, String.valueOf(dbManager.updateStepRecord("2017-10-08", 1008)));
        Log.d(TAG, String.valueOf(dbManager.updateHeartRecord("2017-10-08", "1008")));

        stepRecords = dbManager.readAllStepRecords();
        heartRecords = dbManager.readAllHeartRecord();
        for (DateStepsPair record: stepRecords) {
            Log.d(record.getDate(), String.valueOf(record.getSteps()));
        }
        for (DateHrPair record: heartRecords) {
            Log.d(record.getDate(), record.getIsMeasured());
        }

    }
}
