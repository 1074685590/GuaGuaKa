package com.scp.guaguaka.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GuaGuaKa extends View {
	private Canvas mCanvas;// ����
	private Bitmap mBitmap;// ͼƬ
	private Paint mOutterPaint;// �������
	private Path mPath;// �û����Ƶ�·��
	private int mLastX;// ��¼�û���һ�δ�����X����
	private int mLastY;// ��¼�û���һ�δ�����Y����

	public GuaGuaKa(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public GuaGuaKa(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GuaGuaKa(Context context) {
		this(context, null);
	}

	/**
	 * ����һЩ��ʼ������
	 */
	private void init() {
		mOutterPaint = new Paint();
		mPath = new Path();
	}

	/**
	 * �����ؼ��ĳߴ磬�����ǵ�View����super.onMeasure�Ժ����ǾͿ��Ի�ÿؼ��Ŀ�͸�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		// ��ʼ��Bitmap
		mBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
		// ���û���path���ʵ�һЩ���ԣ�Ҳ������Ƥ��
		setOutPaint();
	}

	/**
	 * ���û���path���ʵ�һЩ����
	 */
	private void setOutPaint() {
		mOutterPaint.setColor(Color.RED);
		mOutterPaint.setAntiAlias(true);// �����
		mOutterPaint.setDither(true);// ������
		mOutterPaint.setStrokeJoin(Paint.Join.ROUND);// �������ӷ�ʽΪԲ��
		mOutterPaint.setStrokeCap(Paint.Cap.ROUND);// ���û��ʱ�ˢ����
		mOutterPaint.setStyle(Style.STROKE);// ���û��ʵ���䷽ʽΪ���
		mOutterPaint.setStrokeWidth(20);// ���û��ʵ�һ�����
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastX = x;
			mLastY = y;
			mPath.moveTo(mLastX, mLastY);
			break;
		case MotionEvent.ACTION_MOVE:
			int dx = Math.abs(x - mLastX);// ����û����������ƶ��ľ���ֵ
			int dy = Math.abs(y - mLastY);// ����û����������ƶ��ľ���ֵ
			// ���������������ƶ�������ֵ����3����˵��·���п�������(���Ż���·��)
			if (dx > 3 || dy > 3) {
				mPath.lineTo(x, y);
			}

			mLastX = x;
			mLastY = y;

			break;
		case MotionEvent.ACTION_UP:
			break;

		default:
			break;
		}
		invalidate();
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// ����û����ϵͳ��Canvas��ͼ
		drawPath();
		canvas.drawBitmap(mBitmap, 0, 0, null);
	}

	private void drawPath() {
		mCanvas.drawPath(mPath, mOutterPaint);
	}

}
