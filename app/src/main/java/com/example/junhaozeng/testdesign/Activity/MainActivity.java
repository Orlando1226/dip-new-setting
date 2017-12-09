package com.example.junhaozeng.testdesign.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.junhaozeng.testdesign.Fragments.HealthFragment;
import com.example.junhaozeng.testdesign.Fragments.PetFragment;
import com.example.junhaozeng.testdesign.Fragments.SettingsFragment;
import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.StepService.StepService;
import com.example.junhaozeng.testdesign.Utils.UpdateUiCallBack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainAc";

    public List<Fragment> fragments;
    private BottomNavigationView bottomNavigationView;
    private boolean isBind = false;
    private int currentId;
    private int fgContentId;

    int p;

    private void init() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        fragments = new ArrayList<>();
        fragments.add(new HealthFragment());
        fragments.add(new PetFragment());
        fragments.add(new SettingsFragment());
        fgContentId = R.id.fragment_container;
        displayDefault(0);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int naviIndex = 0;
            switch (item.getItemId()) {
                case R.id.navigation_health:
                    naviIndex = 0;
                    break;
                case R.id.navigation_pet:
                    naviIndex = 1;
                    break;
                case R.id.navigation_settings:
                    naviIndex = 2;
                    break;
            }
            Fragment fragment = fragments.get(naviIndex);
            FragmentTransaction ft = obtainFragmentTransaction();
            getCurrentFragment().onPause();
            if (fragment.isAdded()) {
                fragment.onResume();
            } else {
                ft.add(fgContentId, fragment);
            }
            showFragment(naviIndex);
            ft.commit();
            return true;
        }
    };

    private void displayDefault(int i) {
        currentId = i;
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(fgContentId, fragments.get(i));
        transaction.commit();
    }

    private void showFragment(int i) {
        for (int i1 = 0; i1 < fragments.size(); i1++) {
            Fragment fragment = fragments.get(i1);
            FragmentTransaction ft = obtainFragmentTransaction();
            if (i == i1) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentId = i;
    }

    private Fragment getCurrentFragment() {
        return fragments.get(currentId);
    }

    private FragmentTransaction obtainFragmentTransaction() {
        return getSupportFragmentManager().beginTransaction();
    }

    public void startStepsStatsActivity(View view) {
        startActivity(new Intent(this, StepsStatsActivity.class));
    }

    public void startHealthFunctions(View view) {
        //startActivity(new Intent(this, HeartbeatActivity.class));
        startActivity(new Intent(this, HealthFunctions.class));
    }

    public void startHeartRateProcess(View view) {
        p=1;
        Intent i = new Intent(this, MeasuringInstructionActivity.class);
        i.putExtra("Page", p);
        startActivity(i);
    }

    public void startBloodPressureProcess(View view) {
        p=2;
        Intent i = new Intent(this, MeasuringInstructionActivity.class);
        i.putExtra("Page", p);
        startActivity(i);
    }

    public void startO2Process(View view) {
        p=4;
        Intent i = new Intent(this, MeasuringInstructionActivity.class);
        i.putExtra("Page", p);
        startActivity(i);
    }

    public void startRespirationProcess(View view) {
        p=3;
        Intent i = new Intent(this, MeasuringInstructionActivity.class);
        i.putExtra("Page", p);
        startActivity(i);
    }


    public void setUpService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // Initialize the steps
            StepService stepService = ((StepService.StepBinder) iBinder).getService();
            // tv_steps.setText(String.valueOf(stepService.getStepCount()));

            stepService.registerCallBack(new UpdateUiCallBack() {
                @Override
                public void updateUi(int steps) {
                    // tv_steps.setText(String.valueOf(steps));
                    // TODO:
                    // 1. update progress bar
                    // 2. update --/--
                    // updateHealthFragment(String.valueOf(steps));
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpService();

        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(serviceConnection);
        }
    }
}
