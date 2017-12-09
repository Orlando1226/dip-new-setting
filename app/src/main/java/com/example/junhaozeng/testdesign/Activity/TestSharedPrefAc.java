package com.example.junhaozeng.testdesign.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.Utils.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestSharedPrefAc extends AppCompatActivity {

    private static final String TAG = "TestingSPrefUtils";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_shared_pref);


        // test...
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(this);
        Log.d(TAG, "created class SharedPreferencesUtils");

        sharedPreferencesUtils.setParam("name", "junhaozeng");
        sharedPreferencesUtils.setParam("height", 180);
        sharedPreferencesUtils.setParam("weight", 90);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1996);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 22);
        Date birthDate = cal.getTime();
        sharedPreferencesUtils.setParam("birth_date", birthDate.getTime());
        sharedPreferencesUtils.setParam("step_goal", 8000);
        Log.d(TAG, "set SP");

        int height = (int) sharedPreferencesUtils.getParam("height", 0);
        int weight = (int) sharedPreferencesUtils.getParam("weight", 0);
        birthDate = (Date) sharedPreferencesUtils.getParam("birth_date",
                    new Date(System.currentTimeMillis()).getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String birth = sdf.format(birthDate);
        int goal = (int) sharedPreferencesUtils.getParam("step_goal", 0);
        Log.d("name", sharedPreferencesUtils.getParam("name", "null").toString());
        Log.d("height", String.valueOf(height));
        Log.d("weight", String.valueOf(weight));
        Log.d("birth date", birth);
        Log.d("goal", String.valueOf(goal));
        Log.d(TAG, "get SP");
    }
}
