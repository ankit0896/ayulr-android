package com.oxygen.micro.ayulr;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ParaPlanActivity extends AppCompatActivity {
    RelativeLayout month, year, quater;

    String EmailHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_para_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent=getIntent();
        EmailHolder=intent.getStringExtra("email");
        Log.e("value",""+EmailHolder);
        month = (RelativeLayout) findViewById(R.id.basicplan);
        year = (RelativeLayout) findViewById(R.id.premiumplan);
        quater = (RelativeLayout) findViewById(R.id.standardplan);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParaPlanActivity.this, ParaBasicPlanActivity.class);
                intent.putExtra("email", EmailHolder);
                startActivity(intent);
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParaPlanActivity.this, ParaPremiumPlanActivity.class);
                intent.putExtra("email", EmailHolder);
                startActivity(intent);
            }
        });
        quater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParaPlanActivity.this, ParaStandardPlanActivity.class);
                intent.putExtra("email", EmailHolder);
                startActivity(intent);
            }
        });


    }
}
