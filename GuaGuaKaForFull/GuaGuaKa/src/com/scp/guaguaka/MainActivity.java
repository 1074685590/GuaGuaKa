package com.scp.guaguaka;

import com.scp.guaguaka.view.GuaGuaKa;
import com.scp.guaguaka.view.GuaGuaKa.OnGuaGuaKaCompleteListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity implements
		OnGuaGuaKaCompleteListener {
	private GuaGuaKa mGuaGuaKa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_main);
		initView();
		initEvent();
	}

	private void initView() {
		mGuaGuaKa = (GuaGuaKa) findViewById(R.id.guaguaka);
	}

	private void initEvent() {
		mGuaGuaKa.setOnGuaGuaKaCompleteListener(this);
		mGuaGuaKa.setText("���Ƕ��Ǻú���");
	}

	@Override
	public void complete() {
		Toast.makeText(this, "�û��εò����", 0).show();
	}
}
