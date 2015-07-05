package com.scp.guaguaka.view;

import com.scp.guaguaka.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class GuaGuaKa extends View {
	private Canvas mCanvas;// ����
	private Bitmap mBitmap;// ͼƬ
	private Bitmap mOutterBitmap;// �������ͼ
	private Paint mOutterPaint;// �������
	private Path mPath;// �û����Ƶ�·��
	private int mLastX;// ��¼�û���һ�δ�����X����
	private int mLastY;// ��¼�û���һ�δ�����Y����
	// ԭ��View��Ҫ�ı���
	private Bitmap bitmap;// ����ͼ
	private Paint mBackPaint;// �����ı��Ļ���
	private Rect mTextBound;// ����һ�����Σ���¼�ν���Ϣ�ı��Ŀ�͸�
	private String mText;// �ı�
	private int mTextSize;// ���������С
	private int mTextColor;// �ı���ɫ
	// ����û������������60%��ô���ǾͲ�����·���Լ�Ϳ��
	// �������������Ĳ�������
	// ʹ��volatile�ؼ��ֽ������Σ��Ӷ���֤���ڱ����߳��޸ĺ����̻߳��ܶ�����һ���ɼ���
	private volatile boolean isComplete;

	public GuaGuaKa(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
		TypedArray typedArray = null;
		try {
			// ͨ��TypedArray����ȡ���ǵ��Զ�������
			typedArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.GuaGuaKa, defStyleAttr, 0);
			int count = typedArray.getIndexCount();
			for (int i = 0; i < count; i++) {
				int attr = typedArray.getIndex(i);
				switch (attr) {
				case R.styleable.GuaGuaKa_text:
					mText = typedArray.getString(attr);
					break;
				case R.styleable.GuaGuaKa_textColor:
					mTextColor = typedArray.getColor(attr,
							Color.parseColor("#000000"));
					break;
				case R.styleable.GuaGuaKa_textSize:
					mTextSize = (int) typedArray.getDimension(attr, TypedValue
							.applyDimension(TypedValue.COMPLEX_UNIT_SP, 22,
									getResources().getDisplayMetrics()));
					break;
				}
			}
		} finally {
			if (typedArray != null) {
				typedArray.recycle();
			}
		}

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
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.hyomin2);
		mOutterBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.guaguaka);
		mText = "лл�ݹ�";
		mTextBound = new Rect();
		mBackPaint = new Paint();
		// ת��Ϊ22sp
		mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				22, getResources().getDisplayMetrics());
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
		// ���û��ƻ���Ϣ�Ļ���
		setBackPaint();
		// mCanvas.drawColor(Color.parseColor("#c0c0c0"));
		mCanvas.drawRoundRect(new RectF(0, 0, height, width), 30, 30,
				mOutterPaint);
		mCanvas.drawBitmap(mOutterBitmap, null, new Rect(0, 0, width, height),
				null);
	}

	/**
	 * ���û���path���ʵ�һЩ����
	 */
	private void setOutPaint() {
		// mOutterPaint.setColor(Color.RED);// ������ɫ
		mOutterPaint.setColor(Color.parseColor("#c0c0c0"));
		mOutterPaint.setAntiAlias(true);// �����
		mOutterPaint.setDither(true);// ������
		mOutterPaint.setStrokeJoin(Paint.Join.ROUND);// �������ӷ�ʽΪԲ��
		mOutterPaint.setStrokeCap(Paint.Cap.ROUND);// ���û��ʱ�ˢ����
		// mOutterPaint.setStyle(Style.STROKE);// ���û��ʵ���䷽ʽΪ���
		mOutterPaint.setStyle(Style.FILL);
		mOutterPaint.setStrokeWidth(20);// ���û��ʵ�һ�����
	}

	/**
	 * ���ƻ���Ϣ�Ļ���
	 */
	private void setBackPaint() {
		mBackPaint.setColor(mTextColor);// ������ɫ
		mBackPaint.setAntiAlias(true);// �����
		mBackPaint.setDither(true);// ������
		mBackPaint.setStyle(Style.FILL);// ���û��ʵ���䷽ʽΪʵ��
		mBackPaint.setTextSize(mTextSize);// �����ı������С
		// ��õ�ǰ���ʻ����ı��Ŀ�͸�
		mBackPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
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
			new Thread(mRunnable).start();
			break;

		}
		invalidate();
		return true;
	}

	/**
	 * �첽�����û����������
	 */
	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			int w = getWidth();
			int h = getHeight();
			// ��������Ĵ�С
			float wipeArea = 0;
			// �ؼ������ܹ�������ֵ
			float totalArea = w * h;
			Bitmap bitmap = mBitmap;// Ϳ��������������ǵ�bitmap��
			// ��ȡbitmap������������Ϣ
			int[] mPixels = new int[w * h];
			bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);

			for (int i = 0; i < w; i++) {
				for (int j = 0; j < h; j++) {
					int index = i + j * w;
					if (mPixels[index] == 0) {
						wipeArea++;
					}
				}
			}

			if (wipeArea > 0 && totalArea > 0) {
				int percent = (int) (wipeArea * 100 / totalArea);
				System.out.println(percent);
				// ��������������60%
				if (percent > 60) {
					// �����Ϳ������
					isComplete = true;
					// ��Ϊ�����߳��У�����ʹ��invalidate(),����ʹ��postInvalidate()
					postInvalidate();
				}
			}

		}
	};

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawBitmap(bitmap, 0, 0, null);
		canvas.drawText(mText, getWidth() / 2 - mTextBound.width() / 2,
				getHeight() / 2 + mTextBound.height() / 2, mBackPaint);
		if (isComplete) {
			if (mListener != null) {
				mListener.complete();
			}
		}
		if (!isComplete) {
			// ����û����ϵͳ��Canvas��ͼ
			drawPath();
			canvas.drawBitmap(mBitmap, 0, 0, null);
		}
	}

	private void drawPath() {
		mOutterPaint.setStyle(Style.STROKE);
		// �����ڻ���·��֮ǰ�������ǵ�mOutterPaint
		mOutterPaint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
		mCanvas.drawPath(mPath, mOutterPaint);
	}

	public void setText(String mText) {
		this.mText = mText;
		// ��õ�ǰ���ʻ����ı��Ŀ�͸�
		mBackPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
	}

	/**
	 * �ιο������һ���ص�
	 * 
	 * @author �δ���
	 *
	 */
	public interface OnGuaGuaKaCompleteListener {
		void complete();
	}

	private OnGuaGuaKaCompleteListener mListener;

	public void setOnGuaGuaKaCompleteListener(
			OnGuaGuaKaCompleteListener mListener) {
		this.mListener = mListener;
	}

}
