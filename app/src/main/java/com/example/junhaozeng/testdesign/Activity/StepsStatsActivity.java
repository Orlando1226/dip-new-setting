package com.example.junhaozeng.testdesign.Activity;

import android.app.DatePickerDialog;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.Utils.DateStepsPair;
import com.example.junhaozeng.testdesign.Utils.DbManager;
import com.example.junhaozeng.testdesign.Utils.SharedPreferencesUtils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StepsStatsActivity extends AppCompatActivity
            implements DatePickerDialog.OnDateSetListener, View.OnClickListener{

    private final static String TAG = "stepstatsAc";
    private BarChart chart;
    private String dateSelected;

    private TextView tvDayOfWeek;
    private TextView tvMonth;
    private TextView tvDayOfDate;
    private TextView tvYear;
    private TextView tvDistance;
    private TextView tvCalories;

    private int startYear;
    private int startMonth;
    private int startDay;
    private String startDayOfWeek;

    DatePickerDialog datePickerDialog;

    private void initViews() {
        datePickerDialog = new DatePickerDialog(
                this, StepsStatsActivity.this, startYear, startMonth - 1, startDay);
        tvDayOfWeek = (TextView) findViewById(R.id.cv_dayOfWeek);
        tvMonth = (TextView) findViewById(R.id.cv_month);
        tvDayOfDate = (TextView) findViewById(R.id.cv_dayOfDate);
        tvYear = (TextView) findViewById(R.id.cv_year);
        tvDayOfWeek.setText(startDayOfWeek);
        tvMonth.setText(new DateFormatSymbols().getMonths()[startMonth-1]);
        tvDayOfDate.setText(String.valueOf(startDay));
        tvYear.setText(String.valueOf(startYear));
        tvDistance = (TextView) findViewById(R.id.tv_stepstats_distance);
        tvCalories = (TextView) findViewById(R.id.tv_stepstats_cal);
        tvDayOfWeek.setOnClickListener(this);
        tvMonth.setOnClickListener(this);
        tvDayOfDate.setOnClickListener(this);
        tvYear.setOnClickListener(this);
    }

    private void initDates() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateSelected = sdf.format(today);
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());
        startYear = calender.get(Calendar.YEAR);
        startMonth = calender.get(Calendar.MONTH) + 1;
        startDay = calender.get(Calendar.DATE);
        startDayOfWeek = new SimpleDateFormat("EE").format(today);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        dateSelected = convertToDbDate(i, i1 + 1, i2);
        try {
            String dateString = String.format("%d-%d-%d", i, i1 + 1, i2);
            Date tempDate = new SimpleDateFormat("yyyy-M-d").parse(dateString);
            String dayOfWeek = new SimpleDateFormat("EEEE").format(tempDate);
            tvDayOfWeek.setText(dayOfWeek);
        } catch (ParseException e) {
            tvDayOfWeek.setText("Parse exception...");
        }
        tvMonth.setText(new DateFormatSymbols().getMonths()[i1]);
        tvDayOfDate.setText(String.valueOf(i2));
        tvYear.setText(String.valueOf(i));
        initHealthInfo();
    }

    @Override
    public void onClick(View view) {
        datePickerDialog.show();
    }

    /**
     * @param year
     * @param month
     * @param dayOfMonth
     * @return date string in our database format
     */
    private String convertToDbDate(int year, int month, int dayOfMonth) {
        String sYear = "" + year;
        String sMonth;
        String sDayOfMonth;
        if (month < 10) {
            sMonth = "0" + month;
        } else {
            sMonth = "" + month;
        }
        if (dayOfMonth < 10) {
            sDayOfMonth = "0" + dayOfMonth;
        } else {
            sDayOfMonth = "" + dayOfMonth;
        }
        return sYear + "-" + sMonth + "-" + sDayOfMonth;
    }

//    private final static String TAG = "stepstatsAc";
//
//    private CalendarView cv;
//    private HorizontalBarChart chart;
//    private String dateSelected;
//
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_stats);
        initDates();
        initViews();
        initHealthInfo();
        initBarChart();
    }
//
//    /**
//     * Set dateSelected to be the date today
//     */
//
//    /**
//     * Add listener to calendar
//     */
//    private void initCalendarView() {
//        cv = (CalendarView) findViewById(R.id.cv_step_stats);
//        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//                // i: year
//                // i1: month (starting from 0)
//                // i2: day of month
//                dateSelected = convertToDbDate(i, i1 + 1, i2);
//                initHealthInfo();
//                // Log.d(TAG, dateSelected);
//            }
//        });
//
//        // for my s7e and AVD
////        Long minDateTime = getInitDate().getTime();
////        if (minDateTime != 0) {
////            cv = (CalendarView) findViewById(R.id.cv_step_stats);
////            cv.setMinDate(minDateTime);
////        }
//
//        // for ss's super weird phone
//        // setMaxDate works, System.currentTimeMillis works
//        // but setMinDate doesn't work with current time
//        // so just leave it alone without attempting to set the view
//    }
//


//    private Date getInitDate() {
//        long defaultReturn = 0;
//        SharedPreferencesUtils spUtils = new SharedPreferencesUtils(this);
//        return (Date) spUtils.getParam("init_date", defaultReturn);
//    }

    private void initHealthInfo() {

        // get basic info about user
        int weight;
        int height;
        String gender;
        SharedPreferencesUtils spUtils = new SharedPreferencesUtils(this);
        weight = (int) spUtils.getParam("weight", -1);
        height = (int) spUtils.getParam("height", -1);
        gender = (String) spUtils.getParam("gender", "null");

        // get user's steps on date selected
        DbManager dbManager = new DbManager(this);
        int tempSteps =  dbManager.readStepRecord(dateSelected);

        Log.d(TAG, String.valueOf(isAllInfoExist(weight, height, gender)));

        if (isAllInfoExist(weight, height, gender) && tempSteps != -1) {
            double stepLength;
            double calories;
            double distance;
            if (gender.equals("Male")) {
                stepLength = 0.415 * height;
            } else {
                stepLength = 0.413 * height;
            }
            distance = stepLength * tempSteps * 0.01; // in meters
            calories = (weight * 2.20462) * (distance * 0.62137) * 0.468; // in cal
            tvCalories.setText(String.format("%.2f", calories) + " cal");
            tvDistance.setText(String.format("%.2f", distance) + " m");
        } else {
            tvCalories.setText("N.A");
            tvDistance.setText("N.A");
        }
    }

    private Boolean isAllInfoExist(int weight, int height, String gender) {
        return weight != -1 &&
                height != -1 &&
                (!gender.equals("null"));
    }

    private void initBarChart() {
        chart = (BarChart) findViewById(R.id.bc_steps);

        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        String[] mLabels = new String[] {
                "2017-10-01", "2017-10-02", "2017-10-03", "2017-10-04", "2017-10-05",
                "2017-10-06", "2017-10-07", "2017-10-08", "2017-10-09", "2017-10-10",
                "2017-10-11", "2017-10-12", "2017-10-13"
        };

        /**
         * TESTING ONLY!!!
         * Driest initializing...
         * With MP chart v2.2.2
         */
        entries.add(new BarEntry(0, 12340, "2017-10-01"));
        entries.add(new BarEntry(1, 123, "2017-10-02"));
        entries.add(new BarEntry(2, 2132, "2017-10-03"));
        entries.add(new BarEntry(3, 3367, "2017-10-04"));
        entries.add(new BarEntry(4, 447, "2017-10-05"));
        entries.add(new BarEntry(5, 5457, "2017-10-06"));
        entries.add(new BarEntry(6, 616, "2017-10-07"));
        entries.add(new BarEntry(7, 7859, "2017-10-08"));
        entries.add(new BarEntry(8, 83, "2017-10-09"));
        entries.add(new BarEntry(9, 992, "2017-10-10"));
        entries.add(new BarEntry(10, 10, "2017-10-11"));
        entries.add(new BarEntry(11, 1145, "2017-10-12"));
        entries.add(new BarEntry(12, 1200, "2017-10-13"));
        labels.add("2017-10-01");
        labels.add("2017-10-02");
        labels.add("2017-10-03");
        labels.add("2017-10-04");
        labels.add("2017-10-05");
        labels.add("2017-10-06");
        labels.add("2017-10-07");
        labels.add("2017-10-08");
        labels.add("2017-10-09");
        labels.add("2017-10-10");
        labels.add("2017-10-11");
        labels.add("2017-10-12");
        labels.add("2017-10-13");


        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(new LabelFormatter(mLabels));
        // xAxis.setTextSize(0.5f);
        BarDataSet barDataSet = new BarDataSet(entries, "steps");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(barDataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.5f);
        // chart.setTouchEnabled(false);
        chart.setData(data);
        chart.animateY(3000);
        // chart.setVisibleYRangeMaximum(7000f, YAxis.AxisDependency.RIGHT);
        chart.setVisibleXRangeMaximum(3f);
        chart.moveViewToX(-1);

//        BarData barData = new BarData(labels, barDataSet);
//        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//        chart.setData(barData);


        /**
         * What I actually should do
         */
//        DbManager dbManager = new DbManager(this);
//        List<DateStepsPair> records = dbManager.readAllStepRecords();
//        if (records.size() > 0) {
//            for (int i = 0; i < records.size(); i++) {
//                entries.add(new BarEntry(records.get(i).getSteps(), i));
//                labels.add(records.get(i).getDate());
//            }
//            BarDataSet barDataSet = new BarDataSet(entries, "steps");
//            BarData barData = new BarData(labels, barDataSet);
//            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//            chart.setData(barData);
//            chart.animateY(3000);
//            chart.setVisibleYRangeMaximum(7000f, YAxis.AxisDependency.RIGHT);
//            chart.moveViewToY(0, YAxis.AxisDependency.RIGHT);
//        }
    }

    public class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        public LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mLabels[(int) value];
        }
    }
}
