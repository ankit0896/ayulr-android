package com.oxygen.micro.ayulr.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.oxygen.micro.ayulr.R;

public class ServiceActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    LinearLayout BloodDonor, EquipmentDonor, Audiometrist, Optometrist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BloodDonor = (LinearLayout) findViewById(R.id.L1);
        EquipmentDonor = (LinearLayout) findViewById(R.id.L2);
        Audiometrist = (LinearLayout) findViewById(R.id.L3);
        Optometrist = (LinearLayout) findViewById(R.id.L4);
        BloodDonor.setOnClickListener(this);
        EquipmentDonor.setOnClickListener(this);
        Audiometrist.setOnClickListener(this);
        Optometrist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.L1:
                Intent inten = new Intent(ServiceActivity.this, ViewBloodDonorActivity.class);
                startActivity(inten);
                finish();
                break;
            case R.id.L2:
                Intent intent = new Intent(getApplicationContext(), ViewMedicatedEquipmentActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
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
