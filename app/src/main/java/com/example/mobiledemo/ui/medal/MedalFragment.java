package com.example.mobiledemo.ui.medal;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mobiledemo.R;
import com.example.mobiledemo.data.recordTime;
import com.example.mobiledemo.ui.account.AccountActivity;
import com.example.mobiledemo.ui.report.ReportActivity;
import com.example.mobiledemo.ui.setting.SettingActivity;
import com.example.mobiledemo.ui.type1.Type1Activity;
import com.example.mobiledemo.ui.type1_1.Type1_1Activity;
import com.example.mobiledemo.ui.type2.Type2Activity;
import com.example.mobiledemo.ui.type2_1.Type2_1Activity;
import com.example.mobiledemo.utils.DbUtils;

import java.util.List;

public class MedalFragment extends Fragment {

    private MedalViewModel medalViewModel;
    private long time;
    private long frequency;
    private Button type1Button;
    private Button type1_1Button;
    private Button type2Button;
    private Button type2_1Button;
    private boolean type1Achieved = false;
    private boolean type1_1Achieved =false;
    private boolean type2Achieved = false;
    private boolean type2_1Achieved = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        medalViewModel =
                ViewModelProviders.of(this).get(MedalViewModel.class);
        View root = inflater.inflate(R.layout.fragment_medal, container, false);
        final TextView textView = root.findViewById(R.id.medal_page_title);
        medalViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        final Button dailyButton = root.findViewById(R.id.medal_daily);
        final Button weeklyButton = root.findViewById(R.id.medal_weekly);
        final Button monthlyButton = root.findViewById(R.id.medal_monthly);
        type1Button = root.findViewById(R.id.medal_type1);
        type1_1Button = root.findViewById(R.id.medal_type1_1);
        type2Button = root.findViewById(R.id.medal_type2);
        type2_1Button = root.findViewById(R.id.medal_type2_1);

        initData();

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("type", "day");
                startActivity(intent);
            }
        });
        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("type", "week");
                startActivity(intent);
            }
        });
        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                intent.putExtra("type", "month");
                startActivity(intent);
            }
        });
        type1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Type1Activity.class);
                intent.putExtra("achieved", type1Achieved);
                startActivity(intent);
            }
        });
        type2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Type2Activity.class);
                intent.putExtra("achieved", type2Achieved);
                startActivity(intent);
            }
        });
        type1_1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Type1_1Activity.class);
                intent.putExtra("achieved", type1_1Achieved);
                startActivity(intent);
            }
        });
        type2_1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Type2_1Activity.class);
                intent.putExtra("achieved", type2_1Achieved);
                startActivity(intent);
            }
        });

        return root;
    }

    private void initData() {
        long sum = 0;
        long frequencySum = 0;
        List<recordTime> recordTimeList = DbUtils.getQueryAll(recordTime.class);
        for (recordTime record :
                recordTimeList) {
            sum+=record.getTimeLength()/1000;
            frequencySum+=1;
        }
        time = sum;
        frequency = frequencySum;

        Drawable top1 = getResources().getDrawable(R.drawable.medal_icon_shine);
        Drawable top2 = getResources().getDrawable(R.drawable.medal_icon_grey);
        if (time >60*60) {
            type1Button.setCompoundDrawablesWithIntrinsicBounds(null, top1 , null, null);
//            type1Button.setEnabled(true);
            type1_1Button.setCompoundDrawablesWithIntrinsicBounds(null, top1 , null, null);
//            type1_1Button.setEnabled(true);
            type1Achieved = true;
            type1_1Achieved = true;
        } else if (time >10*60) {
            type1Button.setCompoundDrawablesWithIntrinsicBounds(null, top1 , null, null);
//            type1Button.setEnabled(true);
            type1_1Button.setCompoundDrawablesWithIntrinsicBounds(null, top2 , null, null);
//            type1_1Button.setEnabled(false);
            type1Achieved = true;
            type1_1Achieved = false;
        } else {
            type1Button.setCompoundDrawablesWithIntrinsicBounds(null, top2 , null, null);
//            type1Button.setEnabled(false);
            type1_1Button.setCompoundDrawablesWithIntrinsicBounds(null, top2 , null, null);
//            type1_1Button.setEnabled(false);
            type1Achieved = false;
            type1_1Achieved = false;
        }

        if (frequency > 100) {
            type2Button.setCompoundDrawablesWithIntrinsicBounds(null, top1 , null, null);
//            type2Button.setEnabled(true);
            type2_1Button.setCompoundDrawablesWithIntrinsicBounds(null, top1 , null, null);
//            type2_1Button.setEnabled(true);
            type2Achieved = true;
            type2_1Achieved = true;
        } else if (frequency > 10) {
            type2Button.setCompoundDrawablesWithIntrinsicBounds(null, top1 , null, null);
//            type2Button.setEnabled(true);
            type2_1Button.setCompoundDrawablesWithIntrinsicBounds(null, top2 , null, null);
//            type2_1Button.setEnabled(false);
            type2Achieved = true;
            type2_1Achieved = false;
        } else {
            type2Button.setCompoundDrawablesWithIntrinsicBounds(null, top2 , null, null);
//            type2Button.setEnabled(false);
            type2_1Button.setCompoundDrawablesWithIntrinsicBounds(null, top2 , null, null);
//            type2_1Button.setEnabled(false);
            type2Achieved = false;
            type2_1Achieved = false;
        }
    }
}