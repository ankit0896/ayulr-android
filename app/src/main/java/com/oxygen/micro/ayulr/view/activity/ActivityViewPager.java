package com.oxygen.micro.ayulr.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oxygen.micro.ayulr.commonactivity.CategoryActivity;

import androidx.appcompat.app.AppCompatActivity;

import com.oxygen.micro.ayulr.constant.Intromanager;
import com.oxygen.micro.ayulr.constant.PrefManager;
import com.oxygen.micro.ayulr.R;

/**
 * Created by MICRO on 1/11/2018.
 */

public class ActivityViewPager extends AppCompatActivity {
    private ViewPager viewPager;
    private Intromanager intromanager;
    private MyCustomPager customPager;
    Button next,skip;
    private TextView[]dots;
    private LinearLayout dotslayout;
    private int[] layouts;
    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
      /*  intromanager = new Intromanager(this);
        if (!intromanager.Check()) {
            intromanager.setFirst(false);
            Intent i = new Intent(ActivityViewPager.this, ActivityLogin.class);
            startActivity(i);
            finish();
        }*/
        setContentView(R.layout.viewpager);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        dotslayout=(LinearLayout)findViewById(R.id.layoutdot);
        skip=(Button)findViewById(R.id.skip);
        next=(Button)findViewById(R.id.next);
        layouts = new int[]{R.layout.activityfirst,R.layout.activitysecond,R.layout.activitythird};
        addBottomDots(0);
       customPager =new MyCustomPager();
        viewPager.setAdapter(customPager);
        viewPager.addOnPageChangeListener(viewListener);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int current=getItem(+1);
                if (current<layouts.length){
                    viewPager.setCurrentItem(current);
                }else {
                    launchHomeScreen();
                }
            }
        });
    }

private void addBottomDots(int position){
    dots=new TextView[layouts.length];
    int[]colorActive=getResources().getIntArray(R.array.dot_active);
    int[]colorInactive=getResources().getIntArray(R.array.dot_inactive);
    dotslayout.removeAllViews();
    for (int i=0;i<dots.length;i++){
        dots[i]=new TextView(this);
        dots[i].setText(Html.fromHtml("&#8226;"));
        dots[i].setTextSize(35);
        dots[i].setTextColor(colorInactive[position]);
        dotslayout.addView(dots[i]);
    }if (dots.length>0)
dots[position].setTextColor(colorActive[position]);

}
private int getItem(int i){
    return viewPager.getCurrentItem()+1;

}
    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        Intent i = new Intent(ActivityViewPager.this, CategoryActivity.class);
        startActivity(i);
        finish();
    }


        ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               addBottomDots(position);
                if (position==layouts.length-1){
                    next.setText("PROCEED");
                    skip.setVisibility(View.GONE);

                }else
                {
                    next.setText("NEXT");
                    skip.setVisibility(View.VISIBLE);
                }

                }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    private  class MyCustomPager extends PagerAdapter{
        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=layoutInflater.inflate(layouts[position],container,false);
            container.addView(v);
            return v;
        }
        @Override
        public int getCount() {

            return layouts.length;

        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v=(View)object;
            container.removeView(v);

        }
    }

}
