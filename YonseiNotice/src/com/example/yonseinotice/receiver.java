package com.example.yonseinotice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class receiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		
		//���ý� ���� ����, ����� ���� ���� 
		if(action.equals(Intent.ACTION_BOOT_COMPLETED))
		{
			context.startService(new Intent("yonseinotice_service"));
		}
		else if(action.equals(Intent.ACTION_SHUTDOWN))
		{
			context.stopService(new Intent("yonseinotice_service"));
		}
	}

}
