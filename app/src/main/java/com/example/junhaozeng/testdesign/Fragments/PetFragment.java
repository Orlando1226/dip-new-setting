package com.example.junhaozeng.testdesign.Fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.StepService.StepService;
import com.example.junhaozeng.testdesign.Utils.DbManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PetFragment extends Fragment {

    private ImageView img;
    private ImageView img2;
    private TextView textview;
    private ProgressBar progressBar;
    StepService service = new StepService();
    private int currentStep = service.CURSTEPS;
    private TextView currentLevelView; //get level from database
    private TextView pointView; //get current point
    private int existingPoint = 40; //get from database

    //functions to get today date
    Date today = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String TODAY = sdf.format(today);

    private int todayPoint;
    private int totalPoint;
    private String measured;
    private static int once = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DbManager database = new DbManager(getActivity());
        todayPoint = database.readStepRecord(TODAY); //must convert to point
        totalPoint = existingPoint + todayPoint;
        measured = database.readHeartRecord(TODAY);


        final View rootView = inflater.inflate(R.layout.fragment_pet, container,
                false);
        img = (ImageView) rootView.findViewById(R.id.idle);
        img2 = (ImageView) rootView.findViewById(R.id.walk);
        img2.setVisibility(View.INVISIBLE);
        progressBar = rootView.findViewById(R.id.stepProgressBar);
        currentLevelView =(TextView)rootView.findViewById(R.id.level);
        pointView = (TextView) rootView.findViewById(R.id.currentPoint);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyLevel", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Integer level = sharedPreferences.getInt("Level", 0);
        Log.d("level from sp",String.valueOf(level));

        currentLevelView.setText(String.valueOf(level));
        //need change to existing point + today point and reflect it. once bar full, reset zero
        if (totalPoint>100) {
            progressBar.setProgress(0);
            totalPoint = 0;
            level = level + 1;
            editor.putInt("Level", level);
            editor.commit();
            currentLevelView.setText(level);

        }
        else {
            progressBar.setProgress(totalPoint);
            pointView.setText(String.valueOf(totalPoint));
        }



        // need change the 12000 to variable goal
        if (currentStep < 12000){
            Log.d("current step", String.valueOf(currentStep));
            textview =(TextView)rootView.findViewById(R.id.dialog);
            textview.setText(R.string.nohitgoal);
        }
        else {
            textview.setText(R.string.hitgoal);
        }
        //need find a way to alternate this
        if (measured.equals("null")){
            Log.d("null", "true");
            textview =(TextView)rootView.findViewById(R.id.dialog);
            textview.setText(R.string.hadMeasuredHeart);
        }


        img.post(new Runnable() {

            @Override
            public void run() {
                ((AnimationDrawable) img.getBackground()).start();
            }

        });

        once=1;
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("once1",String.valueOf(once));
        if (once == 1) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
            once = once + 1;

        }

    }

}
