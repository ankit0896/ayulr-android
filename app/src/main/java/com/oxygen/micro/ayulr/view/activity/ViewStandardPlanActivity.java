package com.oxygen.micro.ayulr.view.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oxygen.micro.ayulr.R;

public class ViewStandardPlanActivity extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_view_standard, container, false);
        return v;
    }
}
