package com.example.yonseinotice;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class splash extends Activity
{
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splash);
	        Handler mhd = new Handler();
	        mhd.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					finish();
				}
			}, 2000);
	        //�ε�ȭ���� 2�ʸ� �����ְ� ����ǵ��� ����
	    }
}