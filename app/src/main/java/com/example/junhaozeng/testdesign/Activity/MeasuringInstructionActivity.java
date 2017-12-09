package com.example.junhaozeng.testdesign.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.junhaozeng.testdesign.R;

public class MeasuringInstructionActivity extends AppCompatActivity {

    private String user;
    private int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measuring_instruction);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            user = extras.getString("Usr");
            p = extras.getInt("Page");
        }

        Button VS = (Button) this.findViewById(R.id.measuring_instruction_btn_start);

        VS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //switch is to decide which activity must be opened
                switch(p){

                    case 1:  {Intent i = new Intent(v.getContext(),HeartRateProcess.class);
                        //i.putExtra("Usr", user);
                        startActivity(i);
                        finish();}
                    break;

                    case 2:  {Intent i = new Intent(v.getContext(),BloodPressureProcess.class);
                        //i.putExtra("Usr", user);
                        startActivity(i);
                        finish();}
                    break;

                    case 3:  {Intent i = new Intent(v.getContext(),RespirationProcess.class);
                        //i.putExtra("Usr", user);
                        startActivity(i);
                        finish();}
                    break;

                    case 4:  {Intent i = new Intent(v.getContext(),O2Process.class);
                        //i.putExtra("Usr", user);
                        startActivity(i);
                        finish();}
                    break;
                }

            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        Intent i = new Intent(MeasuringInstructionActivity.this, HealthFunctions.class);
//        startActivity(i);
//        finish();
//        super.onBackPressed();
//    }
}
