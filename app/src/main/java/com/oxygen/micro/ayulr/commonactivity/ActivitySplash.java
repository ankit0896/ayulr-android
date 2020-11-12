package com.oxygen.micro.ayulr.commonactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.oxygen.micro.ayulr.ActivityViewPager;
import com.oxygen.micro.ayulr.R;

public class ActivitySplash extends Activity {
	ImageView bounceImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		@SuppressLint("ResourceType") final Animation animBounce = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.fadein);
		bounceImage = (ImageView)findViewById(R.id.splashscreen);
		bounceImage.startAnimation(animBounce);
		new CountDownTimer(5000,1000) {
        	
			@Override
			public void onFinish() {
				Intent intent = new Intent(getBaseContext(), ActivityViewPager.class);
				startActivity(intent);
				finish();
			}
			@Override
			public void onTick(long millisUntilFinished) {
								
			}
		}.start();
        
    }
}