package com.example.junhaozeng.testdesign.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.junhaozeng.testdesign.R;

public class HealthFunctions extends AppCompatActivity {

    private String user;
    private int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_functions);

        Button HeartRate = (Button)this.findViewById(R.id.HR);
        Button BloodPressure = (Button)this.findViewById(R.id.BP);
        Button Ox2 = (Button)this.findViewById(R.id.O2);
        Button RRate = (Button)this.findViewById(R.id.RR);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            user = extras.getString("Usr");
//            //The key argument here must match that used in the other activity
//        }

        //Every Test Button sends the username + the test number, to go to the wanted test after the instructions activity
        HeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p=1;
                Intent i = new Intent(v.getContext(),MeasuringInstructionActivity.class);
//                i.putExtra("Usr", user);
                i.putExtra("Page", p);
                startActivity(i);
                //finish();
            }
        });

        BloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p=2;
                Intent i = new Intent(v.getContext(),MeasuringInstructionActivity.class);
//                i.putExtra("Usr", user);
                i.putExtra("Page", p);
                startActivity(i);
                //finish();
            }
        });

        RRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p=3;
                Intent i = new Intent(v.getContext(),MeasuringInstructionActivity.class);
//                i.putExtra("Usr", user);
                i.putExtra("Page", p);
                startActivity(i);
                //finish();
            }
        });

        Ox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p=4;
                Intent i = new Intent(v.getContext(),MeasuringInstructionActivity.class);
//                i.putExtra("Usr", user);
                i.putExtra("Page", p);
                startActivity(i);
                //finish();

            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle("Really Exit?")
//                .setMessage("Are you sure you want to exit?")
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                        HealthFunctions.super.onBackPressed();
//                        finish();
//                        System.exit(0);
//                    }
//                }).create().show();
//        super.onBackPressed();
//        finish();
//    }
}
