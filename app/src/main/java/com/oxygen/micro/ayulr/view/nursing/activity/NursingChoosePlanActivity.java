package com.oxygen.micro.ayulr.view.nursing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.SharedPrefManagernur;
import com.oxygen.micro.ayulr.view.nursing.model.Nur;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NursingChoosePlanActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.premiumplan)
    RelativeLayout year;
    @BindView(R.id.standardplan)
    RelativeLayout quater;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    String EmailHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursing_choose_plan);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Nur nur = SharedPrefManagernur.getInstance(this).getUserNur();
        EmailHolder = nur.getNurEmail();
        Log.e("value", "" + EmailHolder);

        quater.setOnClickListener(this);
        year.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view==year){
            Intent intent = new Intent(NursingChoosePlanActivity.this, NursingPremiumPlanActivity.class);
            intent.putExtra("email", EmailHolder);
            startActivity(intent);
        }
        if(view==quater){
            Intent intent = new Intent(NursingChoosePlanActivity.this, NursingStandardPlanActivity.class);
            intent.putExtra("email", EmailHolder);
            startActivity(intent);
        }
    }
}
