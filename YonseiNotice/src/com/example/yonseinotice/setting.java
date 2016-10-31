package com.example.yonseinotice;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

public class setting extends PreferenceActivity implements OnPreferenceClickListener{
	
	private int type=1;
	private Preference max_pref, renew_pref;
	
	/*
				final SharedPreferences notice = getSharedPreferences("notice",1); 	공지사항 프리퍼런스
				int max_notice = notice.getInt("max_notice", 5);	최대공지사항개수
				int renew_time = notice.getInt("renew_time", 30);	갱신주기
	 */
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting);
        
        init();
    }

    
    public void init(){
    	max_pref = (Preference)findPreference("MAX_NOTICE");
    	max_pref.setOnPreferenceClickListener(this);
    	renew_pref = (Preference)findPreference("RENEW");
    	renew_pref.setOnPreferenceClickListener(this);
    }
    
    public boolean onPreferenceClick(android.preference.Preference preference) {
		// TODO Auto-generated method stub
		if(preference.hasKey()){
			if("MAX_NOTICE".equals(preference.getKey()))
			{
				type=1;
				AlertDialog dialog = createDialog_numpick();				
				dialog.show();
			}
			else if("RENEW".equals(preference.getKey()))
			{
				type=2;
				AlertDialog dialog = createDialog_numpick();				
				dialog.show();
			}
		}
		
		return false;
	}
    
    private AlertDialog createDialog_numpick() 
	{
		LayoutInflater li = LayoutInflater.from(this);
		View view = li.inflate(R.layout.numpick, null);
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);


		final SharedPreferences notice = getSharedPreferences("notice",1);
		final SharedPreferences.Editor editor = notice.edit();
		

		int max_notice = notice.getInt("max_notice", 5);
		int renew_time = notice.getInt("renew_time", 30);
		
		final NumberPicker np = (NumberPicker)view.findViewById(R.id.numpick);
		
		if(type==1){
			//최소값 1, 최대값 10으로 설정
			np.setMinValue(1);
			np.setMaxValue(10);
			np.setValue(max_notice);
			builder.setTitle("최대 공지사항 수 설정");	
		}
		else{
			//최소값 1, 최대값 60으로 설정
			np.setMinValue(1);
			np.setMaxValue(60);
			np.setValue(renew_time);
			builder.setTitle("갱신주기 설정");	
		}
			
			
		builder.setView(view);
		builder.setNegativeButton("확인",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(type==1){
					editor.putInt("max_notice", np.getValue());
					editor.commit();
					Toast.makeText(getApplicationContext(), "최대 공지사항 수가 "+np.getValue()+"개로 설정되었습니다", Toast.LENGTH_SHORT).show();
				}
				else{
					editor.putInt("renew_time", np.getValue());
					editor.putInt("curren_time", 0);
					editor.commit();
					Toast.makeText(getApplicationContext(), "갱신 주기가 "+np.getValue()+"분으로 설정되었습니다", Toast.LENGTH_SHORT).show();
				}
				
				
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("취소",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		
		return dialog;
	}

}
