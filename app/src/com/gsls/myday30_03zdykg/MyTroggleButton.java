package com.gsls.myday30_03zdykg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyTroggleButton extends View implements View.OnClickListener {

	public MyTroggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	public MyTroggleButton(Context context) {
		super(context);
		initView();
	}
	/**
	 *
	 */
	Bitmap mapBg;
	Bitmap mapTroggle;

	/*
	 * 按钮距离view的左边距
	 */
	private float btnToLeft;//默认在左边    0

	/**
	 * 记录开关按钮的状态toggleStage,
	 * false 是关的状态，默认false
	 * true是开
	 */
	boolean toggleStage;

	/**
	 * 按钮开关的宽
	 */
	private int btnTroggleWidth;
	/**
	 * view的宽，也即是背景图片的宽
	 */
	private int viewWidth;

	/**
	 * 滑动按钮   距离左边距的最大值
	 */
	private long toLeftMax;

	private void initView(){

		//初始化是否是拖动
		isDrag = false;

		mapBg = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
		//11-26 00:58:36.334: I/System.out(1556): getResources()======android.content.res.Resources@b657a700

		System.out.println("getResources()======"+getResources());
		mapTroggle = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);

		toggleStage =  false;
		//设置点击事件
		setOnClickListener(this);
	}

	@Override
	/*
	 * 当系统需要测量控件的大小时调用此方法
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		viewWidth = mapBg.getWidth();

//		System.out.println("背景图片的宽度：：："+viewWidth);//120

		int height = mapBg.getHeight();
		btnTroggleWidth = mapTroggle.getWidth();
		//滑动按钮   距离左边距的最大值   两个图片宽相减
		toLeftMax = mapBg.getWidth()- mapTroggle.getWidth();
		setMeasuredDimension(viewWidth, height);
	}

	@Override
	/**
	 * 绘制内容，相对于veiw左上角坐标（0,0）
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
		canvas.drawBitmap(mapBg, 0, 0, paint);

		// 绘制按钮
		// 参数二、三，是图片左上角在view坐标系中的坐标，
		canvas.drawBitmap(mapTroggle, btnToLeft, 0, paint);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
							int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}

	/**
	 * 是否是拖动，
	 * 如果按下，移动，距离小于5个象素，我们认为是点击的动作，按点击的逻辑处理
	 * 如果超过5个像素，我们认为发生了拖动，按拖动的逻辑处理

	 */
	private boolean isDrag;

	@Override
	/**
	 * <pre>
	 * 1.按钮点击事件，点击切换开关；
	 * 2.也就是更改 canvas.drawBitmap(mapTroggle, 50, 0, paint)中的x坐标
	 * </pre>
	 */
	public void onClick(View v) {
		//方法1：
		//记录开关按钮的状态toggleStage
//		if(toggleStage){
//			btnToLeft = 0f;
//			toggleStage = false;
//		}else{
//			btnToLeft = viewWidth - btnTroggleWidth;//
//			toggleStage = true;
//		}
//		invalidate();

		//方法2：
		if(isDrag){//如果是拖拽，不作为点击事件处理，直接返回。
			return;
		}
		toggleStage = !toggleStage;
		flushState();

	}

	/**
	 * down时的X坐标
	 */
	float downX;
	/*
	 * 上一次点击的位置
	 */
	float lastX;

	/**
	 * 当前滑动的位置
	 */
	float currentMoveX;

	/**
	 * 记录拖动距离，为了判断是否是拖动
	 */
	float dragDistance = 0;


	/*
	 * 滑动切换开关，处理触摸事件
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		System.out.println("onTouchEvent:");
		/**
		 * 点击事件其实也是从触摸事件中解析而来的，单击，双击等根据，但系统对点击的解析丰富粗糙；
		 * 只要发生了UP事件，系统就认为发生了点击的动作
		 */
		super.onTouchEvent(event);

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
//			System.out.println("MotionEvent.ACTION_DOWN:");
				dragDistance = 0;

				isDrag = false;//按下的后肯定没有拖动。重置一下值

				downX = lastX = event.getX();

				break;
			case MotionEvent.ACTION_MOVE:
//			System.out.println("MotionEvent.ACTION_MOVE:");
//			System.out.println("ACTION_MOVE::一个像素多大啊：："+event.getX());
				currentMoveX = event.getX();
				float disX = currentMoveX - lastX;//两次事件的，距离
				//通过手指在屏幕上滑动的距离，改变按钮在屏幕上显示的位置
				btnToLeft +=disX;

				//刚点击是btnToLeft的值是0，或者toLeftMax；因为之前的状态确定，值就确定了
//			System.out.println("btnToLeft的值：："+btnToLeft);

				//打印结果：11-26 04:42:09.980: I/System.out(2012): 滑动距离：：1.003128
				//System.out.println("滑动距离：："+disX);

//			dragDistance = dragDistance + Math.abs(disX);

				/**
				 * 或者这样计算
				 */
				dragDistance = Math.abs(currentMoveX-downX);

				//刷新之前判断是否越界，view的坐标系是无穷的.

				flushView();//自己抽取的方法

				lastX = currentMoveX;//重新赋值，当前事件的位置，付给上一次事件的位置。可以认为1个像素一个像素的渐变

//			抬起手时，距离大于十个像素，就认为拖动
				if(dragDistance>5){
					isDrag = true;
				}else{
					isDrag = false;
				}

				break;
			case MotionEvent.ACTION_UP:

//			System.out.println("MotionEvent.ACTION_UP:");
				//如果是拖动就按拖动的逻辑处理
//			if(isDrag){
				/**
				 * <pre>
				 * 自己定义规则。
				 * 当松手的时候，如果滑动按钮的左边距大于 toLeftMax 的一半【不考虑中间滑动过程,和路程；只通过最后的状态判断】  就开
				 * 否则就关
				 * </pre>
				 */
				if(btnToLeft>toLeftMax/2){//
					toggleStage = true;

				}else{
					toggleStage = false;
				}


				System.out.println("距离左边距的最大值：："+toLeftMax);
				System.out.println("btnToLeft的值：："+btnToLeft);
				System.out.println("拖动距离：："+dragDistance);

				flushState();
//			}
				break;
			default:
				break;
		}
		return true;
	}

	/**
	 * 刷新当前状态，就是根据设定的btnToLeft 值  来间接调用系统  刷新view方法
	 */
	private void flushState() {
		if(toggleStage){
			btnToLeft = toLeftMax;
		}else{
			btnToLeft = 0f;
		}

		flushView();
	}

	/**
	 * 1.解决滑动超出view的边界问题。
	 * 2.刷新之前判断是否越界，view的坐标系是无穷的.//抽取方法
	 */
	public void flushView() {
		if(btnToLeft<0){
			btnToLeft = 0;
		}
		if(btnToLeft>toLeftMax){//大于距离左边距的最大值
			btnToLeft = toLeftMax;
		}
		//再刷新  view
		invalidate();
	}

}
