package com.example.junhaozeng.testdesign.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.junhaozeng.testdesign.Activity.MainActivity;
import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.StepService.StepService;
import com.example.junhaozeng.testdesign.Utils.DbManager;
import com.example.junhaozeng.testdesign.Utils.SharedPreferencesUtils;
import com.example.junhaozeng.testdesign.Utils.UpdateUiCallBack;
import com.example.junhaozeng.testdesign.Views.StepArcView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HealthFragment extends Fragment {

    private final String TAG = "healthFrag";
    private View view;
    private StepArcView dylanRing;
    private Thread thread;
    // private int counter = 0;
    // private MainActivity mCallBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_health, container, false);

        updateVitalSigns();

        dylanRing = view.findViewById(R.id.dylan_ring);
        // Assume goal = 7000
        dylanRing.setCurrentCount(7000, StepService.CURSTEPS);
        mThread();

        return view;
    }

    public void updateHealthFragView(int goalSteps) {
        // Assume goal = 7000
        dylanRing.setCurrentCount(goalSteps, StepService.CURSTEPS);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // updateHealthFragView(String.valueOf(counter++), "145");
            updateHealthFragView(7000);
        }
    };

    private void mThread() {
        if (thread == null) {
            thread = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = new Message();
                        handler.sendMessage(msg);
                    }
                }
            });
            thread.start();
        }
    }

    private void setTvValues(TextView v, int value) {
        if (value != -1) {
            v.setText(String.valueOf(value));
        }
    }

    private void setTvDate(TextView v, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date.getTime() != new Long(0)) {
            v.setText(sdf.format(date));
        }
    }


    /**
     * Be CAREFUL to call this function!
     * View might be NULL such that the app crashes!
     */
    private void updateVitalSigns() {
        TextView tvHr = view.findViewById(R.id.tv_hr);
        TextView tvRp = view.findViewById(R.id.tv_rp);
        TextView tvBp = view.findViewById(R.id.tv_bp);
        TextView tvO2 = view.findViewById(R.id.tv_o2);
        TextView tvHrDate = view.findViewById(R.id.tv_hr_date);
        TextView tvRpDate = view.findViewById(R.id.tv_rp_date);
        TextView tvBpDate = view.findViewById(R.id.tv_bp_date);
        TextView tvO2Date = view.findViewById(R.id.tv_o2_date);

        SharedPreferencesUtils sp = new SharedPreferencesUtils(getActivity());

        int hr = (int) sp.getParam("heart_beat", -1);
        int rp = (int) sp.getParam("respiratory_rate", -1);
        int bpSp = (int) sp.getParam("SP", -1);
        int bpDp = (int) sp.getParam("DP", -1);
        int o2 = (int) sp.getParam("o2_saturation", -1);

        Date hrDate = (Date) sp.getParam("last_hr_date", new Long(0));
        Date rpDate = (Date) sp.getParam("last_rp_date", new Long(0));
        Date bpDate = (Date) sp.getParam("last_bp_date", new Long(0));
        Date o2Date = (Date) sp.getParam("last_o2_date", new Long(0));

        setTvValues(tvHr, hr);
        setTvDate(tvHrDate, hrDate);
        setTvValues(tvRp, rp);
        setTvDate(tvRpDate, rpDate);
        if (bpSp != -1 && bpDp != -1) {
            tvBp.setText(bpSp + " / " + bpDp);
        }
        setTvDate(tvBpDate, bpDate);
        setTvValues(tvO2, o2);
        setTvDate(tvO2Date, o2Date);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateVitalSigns();
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mCallBack = (MainActivity) activity;
//        } catch (ClassCastException e){
//            throw new ClassCastException(activity.toString()
//                    + "must implement OnHeadLineSelectedListener!");
//        }
//    }
}
