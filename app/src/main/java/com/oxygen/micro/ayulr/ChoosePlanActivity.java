package com.oxygen.micro.ayulr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChoosePlanActivity extends AppCompatActivity {
    RelativeLayout  year, quater;

    String EmailHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        User user = SharedPrefManager.getInstance(this).getUser();
        EmailHolder= user.getEmail();
        Log.e("value",""+EmailHolder);
        quater = (RelativeLayout) findViewById(R.id.standardplan);
        year = (RelativeLayout) findViewById(R.id.premiumplan);
        quater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoosePlanActivity.this, StandardPlan.class);
                intent.putExtra("email", EmailHolder);
                startActivity(intent);
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChoosePlanActivity.this, PremiumPlan.class);
                intent.putExtra("email", EmailHolder);
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
}
