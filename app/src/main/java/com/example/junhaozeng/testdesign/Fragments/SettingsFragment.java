package com.example.junhaozeng.testdesign.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.junhaozeng.testdesign.R;
import com.example.junhaozeng.testdesign.Utils.SharedPreferencesUtils;

public class SettingsFragment extends Fragment {
    SharedPreferencesUtils sharedPreferencesUtils;
    private String name;
    private Integer height ;
    private Integer weight;
    private String gender;
    private Integer goal;

    public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter adapter= spinner.getAdapter();
        int count= adapter.getCount();
        for(int i=0;i<count;i++){
            if(value.equals(adapter.getItem(i).toString())){
                spinner.setSelection(i,true);
                break;
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesUtils = new SharedPreferencesUtils(getActivity());
        gender = (String) sharedPreferencesUtils.getParam("gender", "Male");
        name = (String) sharedPreferencesUtils.getParam("name", "User");
        height = (Integer) sharedPreferencesUtils.getParam("height", 50);
        weight = (Integer) sharedPreferencesUtils.getParam("weight", 20);
        goal = (Integer)   sharedPreferencesUtils.getParam("goal", 4000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // final Button button = getActivity().findViewById(R.id.edit_button);
        EditText newName= getActivity().findViewById(R.id.edit_name);
        final Spinner newGender = getActivity().findViewById(R.id.edit_gender);
        final Spinner newHeight = getActivity().findViewById(R.id.edit_height);
        final Spinner newWeight = getActivity().findViewById(R.id.edit_weight);
        final Spinner newGoal = getActivity().findViewById(R.id.edit_goal);
        String[] height_array = new String[200];
        String[] weight_array = new String[200];
        String[] goal_array = new String[100];
        for (int i = 0;i <= 199; i++){
            int height = i + 50;
            int weight = 20 + i;
            height_array[i]=String.valueOf(height);
            weight_array[i]=String.valueOf(weight);
        }
        for (int i = 0;i <= 99; i++){
             int goal = i*100 + 3000;
           goal_array[i]=String.valueOf(goal);
        }
        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, height_array);
        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, weight_array);
        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, goal_array);
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array,android.R.layout.simple_spinner_item );
        newGender.setAdapter(genderAdapter);
        newHeight.setAdapter(heightAdapter);
        newWeight.setAdapter(weightAdapter);
        newGoal.setAdapter(goalAdapter);
        setSpinnerItemSelectedByValue(newGender, gender);
        setSpinnerItemSelectedByValue(newHeight, height.toString());
        setSpinnerItemSelectedByValue(newWeight, weight.toString());
        setSpinnerItemSelectedByValue(newGoal,goal.toString());
        newName.setText(name);
//       newName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // 输入的内容变化的监听
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//                // 输入前的监听
//               // Log.e("输入前确认执行该方法", "开始输入");
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // 输入后的监听
//                sharedPreferencesUtils.setParam("name",s.toString());
//            }
//        });


newGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
sharedPreferencesUtils.setParam("gender", newGender.getSelectedItem().toString());
setSpinnerItemSelectedByValue(newGender, newGender.getSelectedItem().toString());
}

public void onNothingSelected(AdapterView<?> parent) {

}
});

newGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
goal = Integer.parseInt(newGoal.getSelectedItem().toString());
sharedPreferencesUtils.setParam("goal", goal);
setSpinnerItemSelectedByValue(newGoal, goal.toString());
}

public void onNothingSelected(AdapterView<?> parent) {

}
});

newHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
height = Integer.parseInt(newHeight.getSelectedItem().toString());
sharedPreferencesUtils.setParam("height", height);
setSpinnerItemSelectedByValue(newHeight, height.toString());
}

public void onNothingSelected(AdapterView<?> parent) {

}
});

newWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
weight = Integer.parseInt(newWeight.getSelectedItem().toString());
sharedPreferencesUtils.setParam("weight", weight);
setSpinnerItemSelectedByValue(newWeight, weight.toString());
}

public void onNothingSelected(AdapterView<?> parent) {

}
});



    }
}