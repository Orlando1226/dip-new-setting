package com.example.junhaozeng.testdesign.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class RespirationResult extends AppCompatActivity {

    //private String Date;
    int RR;
    //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    //java.util.Date today = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respiration_result);

        //Date = df.format(today);
        TextView RRR = (TextView) this.findViewById(R.id.RRR);
        //ImageButton SRR = (ImageButton)this.findViewById(R.id.SendRR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            RR = bundle.getInt("bpm");
            //user = bundle.getString("Usr");
            RRR.setText(String.valueOf(RR));

            // TODO: save to Database and SharedPreferences
            DbManager dbManager = new DbManager(this);
            SharedPreferencesUtils sp = new SharedPreferencesUtils(this);
            String today = getTodayDate();
            sp.setParam("respiratory_rate", RR);
            sp.setParam("last_rp_date", System.currentTimeMillis());
            if (dbManager.readRespiratoryRecord(today).equals("null")) {
                dbManager.insertRespiratoryRecord(today, "true");
            }
        }

//        SRR.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
//                i.putExtra(Intent.EXTRA_SUBJECT, "Health Watcher");
//                i.putExtra(Intent.EXTRA_TEXT   , user+"'s Respiration Rate "+"\n"+" at "+ Date +" is :  "+RR);
//                try {
//                    startActivity(Intent.createChooser(i, "Send mail..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(RespirationResult.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
//        Intent i = new Intent(RespirationResult.this, HealthFunctions.class);
//        // i.putExtra("Usr", user);
//        startActivity(i);
//        finish();
//        super.onBackPressed();
//
//    }
}
