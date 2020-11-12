package com.oxygen.micro.ayulr.view.activity;

import android.content.Intent;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.oxygen.micro.ayulr.view.paramedical.activity.ParamedicalPriceingTable;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.view.nursing.activity.NursingPricingTable;

public class PacakageDealActivity extends AppCompatActivity {
    CardView month, year, quater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacakage_deal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        month = findViewById(R.id.basicplan);
        year =findViewById(R.id.premiumplan);
        quater = findViewById(R.id.standardplan);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PacakageDealActivity.this, DoctorPricingActivity.class);
                startActivity(intent);
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PacakageDealActivity.this, NursingPricingTable.class);
                startActivity(intent);
            }
        });
        quater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PacakageDealActivity.this, ParamedicalPriceingTable.class);
                startActivity(intent);
            }
        });


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
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), Main2Activity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
