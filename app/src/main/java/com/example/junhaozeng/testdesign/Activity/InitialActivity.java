package com.example.junhaozeng.testdesign.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.Utils.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by sunjiajun on 17/10/3.
 */

public class InitialActivity extends AppCompatActivity {
    private ViewPager pager;
    private String gender;
    private String height;
    private String weight;
    private String age;
    private String goal;
    private long birthDate;
    static final int DATE_DIALOG_ID = 0;
    private View view1;
    private View view2;
    private View view3;
    private ArrayAdapter<String> heightInitialAdapter;
    private ArrayAdapter<String> weightInitialAdapter;
    private DatePicker datePicker;
    private Spinner heightSpinner;
    private Spinner weightSpinner;
    private EditText initialName;
    public SharedPreferencesUtils sharedPreferencesUtils;

    //每一个界面
    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesUtils = new SharedPreferencesUtils(this);
        if((Boolean) sharedPreferencesUtils.getParam("isInit", false)) {
            Intent intent = new Intent(InitialActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        sharedPreferencesUtils.setParam("gender", "Male");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        setContentView(R.layout.initial_main);
        LayoutInflater li = getLayoutInflater();
        pager = (ViewPager)findViewById(R.id.viewPager);
        views = new ArrayList<>();
        view1 = li.inflate(R.layout.initial_one,null);
        view2 = li.inflate(R.layout.initial_two,null);
        view3 = li.inflate(R.layout.initial_three,null);
        datePicker = view1.findViewById(R.id.dpPicker);
        datePicker.init(1995, 1, 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                birthDate = calendar.getTimeInMillis();
            }
        });
        RadioGroup radioGender = view2.findViewById(R.id.radioGender);
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                RadioButton r = (RadioButton) findViewById(checkedId);
                sharedPreferencesUtils.setParam("gender", r.getText().toString());
                gender = r.getText().toString();
            }
        });
        heightSpinner= view3.findViewById(R.id.height_initial);
        weightSpinner= view3.findViewById(R.id.weight_initial);
        String[] height_array = new String[200];
        String[] weight_array = new String[200];
        for (int i = 0; i <= 199 ;i++){
            int height = i + 50;
            int weight = 20 + i;
            height_array[i]=String.valueOf(height);
            weight_array[i]=String.valueOf(weight);
        }
        heightInitialAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, height_array);
        weightInitialAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weight_array);
        heightInitialAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        heightSpinner.setAdapter(heightInitialAdapter);
        weightSpinner.setAdapter(weightInitialAdapter);
        initialName = view1.findViewById(R.id.name_initial);
        Button startbutton = view3.findViewById(R.id.startButton);
        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesUtils.setParam("name", initialName.getText().toString());
                sharedPreferencesUtils.setParam("height",
                        Integer.parseInt(heightSpinner.getSelectedItem().toString()));
                sharedPreferencesUtils.setParam("weight",
                        Integer.parseInt(weightSpinner.getSelectedItem().toString()));
//                sharedPreferencesUtils.setParam("birth_year", initialyear.toString());
//                sharedPreferencesUtils.setParam("birth_month", initialmonth.toString());
//                sharedPreferencesUtils.setParam("birth_day", initialday.toString());
                sharedPreferencesUtils.setParam("birth_date", birthDate);
                sharedPreferencesUtils.setParam("isInit", true);

                /**
                 * Save current initial date
                 */
                sharedPreferencesUtils.setParam("init_date", System.currentTimeMillis());

                Intent intent = new Intent(InitialActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        views.add(view1);
        views.add(view2);
        views.add(view3);
        //需要给ViewPager设置适配器
        PagerAdapter adapter=new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                // TODO Auto-generated method stub
                return arg0==arg1;
            }
            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return views.size();
            }
            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(views.get(position));
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }
        };
        pager.setAdapter(adapter);
    }
}