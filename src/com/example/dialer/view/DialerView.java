package com.example.dialer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DialerView extends View
{

	
	private double mAngle;
	private float mTouchX, mTouchY;
	private float[] mPosition, mAutoX, mAutoY, mAutoRadius; 
	public int mIsAutomaticMode;
	private int mAutoCounter, mAutoFlag, mCenterX;
		
	Paint mBoundaryCirclePaint, mOuterCirclePaint, mInnercirclePaint, mSmallTouchableCirclePaint;
	
	public DialerView(Context context) {
		
		super(context);
		init();
	}
	
	public DialerView(Context context, AttributeSet attrs){
		
		super(context, attrs);
		init();
	}
	
	public DialerView(Context context, AttributeSet attrs, int defStyleAttr){
	
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init(){
		
	//	touchX = 0;
		//touchY = 0;
		
		mAngle = 0;
		mAutoFlag = 1;
		mAutoCounter = 0;
		mPosition = new float[3];
		mIsAutomaticMode = 0;
				 
		mBoundaryCirclePaint = new Paint();
		mBoundaryCirclePaint.setColor(Color.GRAY);
		mBoundaryCirclePaint.setAntiAlias(true);
		mBoundaryCirclePaint.setStrokeWidth(9.5f);
		
		mOuterCirclePaint = new Paint();
		mOuterCirclePaint.setColor(Color.GREEN);
		mOuterCirclePaint.setAntiAlias(true);
		
		mInnercirclePaint = new Paint();
		mInnercirclePaint.setColor(Color.WHITE);
		mInnercirclePaint.setAntiAlias(true);
	
		mSmallTouchableCirclePaint = new Paint();
		mSmallTouchableCirclePaint.setColor(Color.BLUE);
		mSmallTouchableCirclePaint.setAntiAlias(true);
		

	}
	
	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int Height = getHeight();
		int Width = getWidth();
		//int minside = getSmallestSide(Height, Width);
		mCenterX = Width / 2;
	}*/

	public void setTouchValue(float tx, float ty){
		
		mTouchX = tx;
		mTouchY = ty;
	}
	
	public int getSmallestSide(int Height, int Width){
		
		if(Height < Width)
			return Height;
		else
			return Width;
	}
	
	public boolean getchecktouch(float topLeftRectX, float topLeftRectY, float bottomRightRectX, float bottomRightRectY){
		
		if(((mTouchX > topLeftRectX)&&( mTouchX < bottomRightRectX))&&((mTouchY > topLeftRectY)&&(mTouchY < bottomRightRectY))){
			
			return true;
		}else{
		
			return false;
		}	
	}
	
	/**
	 * 
	 * @param x
	 *            - center x coordinate
	 * @param y
	 *            - center y coordinate
	 * @param radius
	 *            - minScreenSide / 12 for Small_Rotating_Circle
	 * @param noOfDividingPoints
	 *            - for rotation in arc
	 */
	private void getPointsSectionT(int x, int y, double radius,
			int noOfDividingPoints, int minsizeRadius) {

		double angle = 0;

		mAutoX = new float[noOfDividingPoints + 200];
		mAutoY = new float[noOfDividingPoints + 200];
		mAutoRadius= new float[noOfDividingPoints + 200];
		int j = 0;
		for (int i = 0; i < noOfDividingPoints + 200; i++) {
			angle = i * (360 / noOfDividingPoints);
			if (i >= 0) {
				mAutoX[j] = (float) (x + radius * Math.cos(Math.toRadians(angle)));
				mAutoY[j] = (float) (y + radius * Math.sin(Math.toRadians(angle)));
				mAutoRadius[j] = (float) (minsizeRadius + (0.5) * j);
				j++;
			}
			// Log.d("j =  ", "" + j);
		}

	}

	/**
	 * 
	 * @param circleCenterX
	 * @param circleCenterY
	 * @param x
	 *            - touch X coordinate
	 * @param y
	 *            - touch Y coordinate
	 * @return
	 */
	private double getMovementAngleFromCentreInRad(float circleCenterX,float circleCenterY) {
		
		double angle = Math.atan2(circleCenterY - mTouchY, mTouchX - circleCenterX);
		if ((Math.toDegrees(angle) <= 180))
			return angle;
		else
			return angle + 360;
	}
	
	/**
	 * get angle for x and y point
	 */
	public double getAngle()
	{
		return Math.atan2(mCenterX - mTouchX, mTouchY - mCenterX);
	}
	

	/**
	 * 
	 * @param circleCenterX
	 * @param circlecenterY
	 * @param orbitRadius
	 *            - Radius of Rotating_Small_circle
	 * @param angleInRad
	 * @param minsizeRadius
	 * @return
	 */
	protected float[] getCurrentIndicatorPosition(int circleCenterX,
			int circlecenterY, float orbitRadius, double angleInRad,
			float minSideRadius) {

		mPosition[0] = (float) (circleCenterX + orbitRadius * (Math.cos(angleInRad)));
		mPosition[1] = (float) (circlecenterY - orbitRadius * (Math.sin(angleInRad)));
		
		
//		Log.d("TAG", ""+Math.toDegrees(angleInRad));
		
			
	//	mPosition[2] = (float) (minSideRadius + Math.toDegrees(angleInRad) + 90);
		
	///////
		if (angleInRad >= 0) {
			
			mPosition[2] = (float) (minSideRadius + (0.5) * Math.toDegrees(angleInRad));
			// Log.d("angleInrad"," "+Math.toDegrees(angleInRad));
		
		} else {

			mPosition[2] = (float) (minSideRadius + (0.5) * (Math.toDegrees(angleInRad)+360));
		}

	/////////

//		Log.d("Radius", ""+position[2]);
		
		return mPosition;
	}
	
	
	boolean clockFlag = false;
	public boolean boundCircle()
	{
		boolean clockWise = false;
		Log.i("TAG", Math.toDegrees(mAngle)+" "+mTouchX+" "+mTouchY);
		
		if(mAngle > 0)
		{
			if(!clockWise)
			{
				clockWise = true;
				if(mTouchX > 410)
				{
				//	clockFlag = true;
					return false;					
				}
			}
		}
		else
		{
			if(clockWise)
			{
				clockWise = false;
				if(mTouchX < 425)
				{
				//	clockFlag=true;
					return false;
				}
			}
		}
		
	//	Log.i("TAG", clockFlag+"");
		
		if(clockFlag)
			return false;
		else
			return true;
	}

	public void onDraw(Canvas canvas){
		
		super.onDraw(canvas);
		
	//	Log.d("TAG", ""+touchX);
	//	Log.d("TAG",""+touchY);
		
		int viewHeight = getHeight();
		int viewWidth = getWidth();
		int centerViewY = viewHeight / 2;
		int centerViewX = viewWidth / 2;
		int minViewSide = getSmallestSide(viewHeight, viewWidth);
		int outerCircleRadius = (minViewSide * 5) / 11 ;
		int touchableCircleAreaRadius = minViewSide / 8;
		int smallTouchableCircleRadius = minViewSide / 35;
		int smallTouchableCircleOrbitRadius = minViewSide / 12;
		float[] position = new float[3];
		
		
		// Automatic movement 
		getPointsSectionT(centerViewX, centerViewY,
				smallTouchableCircleOrbitRadius, 360, touchableCircleAreaRadius);
		
		// Rectangle Coordinated touchable area
		float topLeftRectX		= centerViewX - touchableCircleAreaRadius;
		float topLeftRectY		= centerViewY - touchableCircleAreaRadius;
		float bottomRightRectX 	= centerViewX + touchableCircleAreaRadius;
		float bottomRightRectY 	= centerViewY + touchableCircleAreaRadius;
	
		boolean var = getchecktouch(topLeftRectX, topLeftRectY, bottomRightRectX, bottomRightRectY);
		if (var){
			
			mAngle = getMovementAngleFromCentreInRad(centerViewX,centerViewY);		
		}
		
//		Log.d("TOUCH", getAngle()+"");
		
		position = getCurrentIndicatorPosition(centerViewX, centerViewY, smallTouchableCircleOrbitRadius, mAngle, touchableCircleAreaRadius);
		
//		Log.d("TOUCH", position[0]+"");
//		Log.d("TOUCH", position[1]+"");
			
		// Boundary Radius of Outer Circle
		canvas.drawCircle(centerViewX, centerViewY, outerCircleRadius, mBoundaryCirclePaint);
		mBoundaryCirclePaint.setShadowLayer(outerCircleRadius, centerViewX, centerViewY, Color.WHITE);
		
		// Outer Circle which Zoom in and Zoom out
		if(mIsAutomaticMode == 0){

			canvas.drawCircle(centerViewX, centerViewY,	(float)(position[2]), mOuterCirclePaint);
		}else{

			canvas.drawCircle(centerViewX, centerViewY,	(float)mAutoRadius[mAutoCounter], mOuterCirclePaint);
		}
		
		// Inner Circle which is touchable 
			canvas.drawCircle(centerViewX, centerViewY, touchableCircleAreaRadius, mInnercirclePaint);

		// starting point of moving touchable circle 
		canvas.drawLine(centerViewX+ minViewSide / 8, centerViewY, centerViewX, centerViewY, mBoundaryCirclePaint);

		// Small_Rotating_Circle 
		if(mIsAutomaticMode == 0){
		
			//touchable circle
				canvas.drawCircle(position[0], position[1], smallTouchableCircleRadius, mSmallTouchableCirclePaint);
		}else {
			
			canvas.drawCircle(mAutoX[mAutoCounter], mAutoY[mAutoCounter], smallTouchableCircleRadius, mSmallTouchableCirclePaint);
		}
		
		
		if (mIsAutomaticMode == 1) {

			if (mAutoFlag == 1) {
			
				mAutoCounter++;
			} else {
			
				mAutoCounter--;
			}

			if (mAutoCounter == 350) {
			
				mAutoFlag = 0;
			} else if (mAutoCounter == 0) {
			
				mAutoFlag = 1;

			}
			invalidate();
		}
			
		
		
	}

	/*@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		setTouchValue(event.getX(), event.getY());
		
		Log.d("TAG", "Check");
		Log.d("ANGLE", getAngle()+"");
		
		
		return true;
	}*/
}
/////////////////

/*
(360-105-(TOTAL_ROTATION_ANGLE/MAX_BRIGHTNESS)*currentDimLevel)*(Math.PI/180);
*/


///////////////////
/*private boolean isIndicatorRotationAllowed(float x, float y){

boolean isMovementAllowed = true;
double angleInDeg = getMovementAngleFromCentreInRad(x, y)*(180/Math.PI);

// at dim level 0
if (mCurrentDimLevel == 0 && angleInDeg >= -105){
isMovementAllowed = false;
}else if (mCurrentDimLevel == 100 && angleInDeg <= -75){
isMovementAllowed = false;
}
return isMovementAllowed;
}

			float x1 = ((bottomRightRectX - topLeftRectX)*3)/4;  
			float y1 = (bottomRightRectY - topLeftRectY)/2;
			float x2 =  ((bottomRightRectX - topLeftRectX)*5)/4;
			float y2 = (bottomRightRectY - topLeftRectY)*3/4;
			
			if(((touchX > x1)&&(touchX < x2))&&(touchY > y1)&&(touchY < y2 )){

	private boolean isIndicatorRotationAllowed(float x, float y){

		boolean isMovementAllowed = true;
		double angleInDeg = getMovementAngleFromCentreInRad(x, y);

		Log.d("TAG", ""+angleInDeg);
		// at dim level 0
		if (angleInDeg >= 25){
		isMovementAllowed = false;
		}else if (angleInDeg <= -25){
		isMovementAllowed = false;
		}
		return isMovementAllowed;
		}





*/	


  

