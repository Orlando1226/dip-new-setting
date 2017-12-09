package com.example.junhaozeng.testdesign.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.Utils.DbManager;
import com.example.junhaozeng.testdesign.Utils.SharedPreferencesUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HeartRateResult extends AppCompatActivity {

    //private String Date;
    int HR;
    //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    //java.util.Date today = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_result);

        //Date = df.format(today);
        TextView RHR = (TextView) this.findViewById(R.id.HRR);
//        ImageButton SHR = (ImageButton)this.findViewById(R.id.SendHR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HR = bundle.getInt("bpm");
//            user = bundle.getString("Usr");
//            Log.d("DEBUG_TAG", "ccccc"+ user);
            RHR.setText(String.valueOf(HR));

            // TODO: save to Database and SharedPreferences
            DbManager dbManager = new DbManager(this);
            SharedPreferencesUtils sp = new SharedPreferencesUtils(this);
            String today = getTodayDate();
            sp.setParam("heart_beat", HR);
            sp.setParam("last_hr_date", System.currentTimeMillis());
            if (dbManager.readHeartRecord(today).equals("null")) {
                dbManager.insertHeartRecord(today, "true");
            }
        }

//        SHR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
//                i.putExtra(Intent.EXTRA_SUBJECT, "Health Watcher");
//                i.putExtra(Intent.EXTRA_TEXT   , user+"'s Heart Rate "+"\n"+" at "+ Date +" is :    "+HR);
//                try {
//                    startActivity(Intent.createChooser(i, "Send mail..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(HeartRateResult.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private String getTodayDate() {
        java.util.Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }



//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        Intent i = new Intent(HeartRateResult.this, HealthFunctions.class);
////        startActivity(i);
//        finish();
//    }
}
