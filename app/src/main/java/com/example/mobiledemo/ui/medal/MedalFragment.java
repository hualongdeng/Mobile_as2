package com.example.mobiledemo.ui.medal;

import android.content.Intent;
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
import com.example.mobiledemo.ui.account.AccountActivity;
import com.example.mobiledemo.ui.report.ReportActivity;
import com.example.mobiledemo.ui.setting.SettingActivity;
import com.example.mobiledemo.ui.type1.Type1Activity;
import com.example.mobiledemo.ui.type1_1.Type1_1Activity;
import com.example.mobiledemo.ui.type2.Type2Activity;
import com.example.mobiledemo.ui.type2_1.Type2_1Activity;

public class MedalFragment extends Fragment {

    private MedalViewModel medalViewModel;

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
        final Button type1Button = root.findViewById(R.id.medal_type1);
        final Button type1_1Button = root.findViewById(R.id.medal_type1_1);
        final Button type2Button = root.findViewById(R.id.medal_type2);
        final Button type2_1Button = root.findViewById(R.id.medal_type2_1);

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });
        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });
        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReportActivity.class);
                startActivity(intent);
            }
        });
        type1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Type1Activity.class);
                startActivity(intent);
            }
        });
        type2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Type2Activity.class);
                startActivity(intent);
            }
        });
        type1_1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Type1_1Activity.class);
                startActivity(intent);
            }
        });
        type2_1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Type2_1Activity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}