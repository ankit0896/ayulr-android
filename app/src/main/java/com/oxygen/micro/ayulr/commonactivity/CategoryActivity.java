package com.oxygen.micro.ayulr.commonactivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.oxygen.micro.ayulr.ActivityLoginpara;
import com.oxygen.micro.ayulr.NursingActivityLogin;
import com.oxygen.micro.ayulr.R;
import com.oxygen.micro.ayulr.doctor.activity.ActivityLogin;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {
ImageView b1,b2,b3;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        imageView=(ImageView)findViewById(R.id.logo);
        @SuppressLint("ResourceType") final Animation animbounce = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.bounce);
        b1=(ImageView) findViewById(R.id.doc);
        b2=(ImageView) findViewById(R.id.para);
        b3=(ImageView) findViewById(R.id.nur);
        b1.startAnimation(animbounce);
        b2.startAnimation(animbounce);
        b3.startAnimation(animbounce);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(CategoryActivity.this,ActivityLogin.class);
                Intent intent=new Intent(CategoryActivity.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoryActivity.this, ActivityLoginpara.class);
                startActivity(intent);
                finish();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CategoryActivity.this, NursingActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
