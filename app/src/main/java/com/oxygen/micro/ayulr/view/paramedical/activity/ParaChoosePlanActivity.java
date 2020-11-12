package com.oxygen.micro.ayulr.view.paramedical.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.constant.SharedPrefManagerpara;
import com.oxygen.micro.ayulr.view.paramedical.model.Para;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParaChoosePlanActivity extends AppCompatActivity implements View.OnClickListener {
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
        setContentView(R.layout.activity_para_choose_plan);
        initViews();
    }

    private void initViews() {
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Para para = SharedPrefManagerpara.getInstance(getApplicationContext()).getUserPara();
        EmailHolder = para.getParaEmail();
        Log.e("value", "" + EmailHolder);

        quater.setOnClickListener(this);
        year.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == quater) {
            Intent intent = new Intent(ParaChoosePlanActivity.this, ParaStandardPlanActivity.class);
            intent.putExtra("email", EmailHolder);
            startActivity(intent);
        }
        if (v == year) {
            Intent intent = new Intent(ParaChoosePlanActivity.this, ParaPremiumPlanActivity.class);
            intent.putExtra("email", EmailHolder);
            startActivity(intent);
        }
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

}
