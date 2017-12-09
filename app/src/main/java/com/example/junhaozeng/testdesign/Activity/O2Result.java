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

public class O2Result extends AppCompatActivity {

    //private String user,Date;
    //DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    //java.util.Date today = Calendar.getInstance().getTime();
    int O2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o2_result);

        //Date = df.format(today);
        TextView RO2 = (TextView) this.findViewById(R.id.O2R);
        //ImageButton SO2 = (ImageButton)this.findViewById(R.id.SendO2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            O2 = bundle.getInt("O2R");
            // user = bundle.getString("Usr");
            RO2.setText(String.valueOf(O2));

            // TODO: save to Database and SharedPreferences
            DbManager dbManager = new DbManager(this);
            SharedPreferencesUtils sp = new SharedPreferencesUtils(this);
            String today = getTodayDate();
            sp.setParam("o2_saturation", O2);
            sp.setParam("last_o2_date", System.currentTimeMillis());
            if (dbManager.readO2Record(today).equals("null")) {
                dbManager.insertO2Record(today, "true");
            }
        }

//        SO2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Intent.ACTION_SEND);
//                i.setType("message/rfc822");
//                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
//                i.putExtra(Intent.EXTRA_SUBJECT, "Health Watcher");
//                i.putExtra(Intent.EXTRA_TEXT   , user+"'s Oxygen Saturation Level "+"\n"+" at "+ Date +" is :   "+O2);
//                try {
//                    startActivity(Intent.createChooser(i, "Send mail..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(O2Result.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
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
//        Intent i = new Intent(O2Result.this, HealthFunctions.class);
//        // i.putExtra("Usr", user);
//        startActivity(i);
//        finish();
//        super.onBackPressed();
//    }
}
