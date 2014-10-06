package com.example.dialer.activity;

import com.example.dialer.R;
import com.example.dialer.view.DialerView;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	DialerView mView;
	float mScreenTouchX, mScreenTouchY;
	Button mAutomatic, mManual, mReset;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mView = (DialerView) findViewById(R.id.dialerView);
		mAutomatic = (Button)findViewById(R.id.mAutomatic);
		mManual = (Button)findViewById(R.id.mManual);
		mReset = (Button)findViewById(R.id.mReset);
		
		mManual.setOnClickListener(new OnClickListener() {
			
		public void onClick(View v) {
			
			
			
			mView.setOnTouchListener(new View.OnTouchListener() {
			
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					mView.mIsAutomaticMode = 0;
					
					mScreenTouchX = event.getX();
					mScreenTouchY = event.getY();
					mView.setTouchValue(mScreenTouchX, mScreenTouchY);
					
					if(mView.boundCircle()){
						mView.invalidate();
					}
					
					Log.d("TOUCH", mView.getAngle()+"");
					return true;
				}
			});
		}
		});
		
		mAutomatic.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				mView.setOnTouchListener(new View.OnTouchListener() {
				
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						mView.mIsAutomaticMode = 1;
						mScreenTouchX = event.getX();
						mScreenTouchY = event.getY();
						mView.setTouchValue(mScreenTouchX, mScreenTouchY);
						mView.invalidate();
						return true;
					}
				});
			}
			});
			
	}	

}
