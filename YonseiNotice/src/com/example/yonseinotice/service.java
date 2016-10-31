package com.example.yonseinotice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

public class service extends Service{

	//Broadcast receiver
	private BroadcastReceiver mReceiver;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				 String action = intent.getAction();
				 if(action.equals(Intent.ACTION_TIME_TICK)){
					 //1�и��� ��׶��忡�� �̺�Ʈ �߻�
					 
					final SharedPreferences notice = getSharedPreferences("notice",1); 		
					int renew_time = notice.getInt("renew_time", 30);
					int current_time = notice.getInt("current_time", renew_time);
					
					final SharedPreferences.Editor editor = notice.edit();
					
					current_time++;
					if(current_time>=renew_time){
						renew();
					}
					current_time = current_time % renew_time;
					editor.putInt("current_time", current_time);
					editor.commit();
					
					Log.v("ssw", "current : " + current_time + ", renew_time : "+renew_time);
					
					
					
				 }
			}
		};
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_TIME_TICK);
		registerReceiver(mReceiver, intentFilter);

	}
	

	private class JsoupAsyncTask extends AsyncTask<Void,Void,Void>{
		@Override    
	        protected void onPreExecute() {    
			 super.onPreExecute();
	        }    
	      
		@Override
		protected Void doInBackground(Void... params) {
			
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			boolean yonsei_home = pref.getBoolean("NOTICE", false);
			
			if(yonsei_home)
				saveitem();
			
			return null;
		}
		private void saveitem() {
			// TODO Auto-generated method stub
			////////////////////////////////////////////////////////////////////////////////
			// ���� ���� ����
			////////////////////////////////////////////////////////////////////////////////
			
			SharedPreferences.Editor editor ;
			try{
				Document doc;
				Elements Ele;
				
				
				
				//�������б�
				doc= Jsoup.connect("http://yonsei.ac.kr/sc/support/notice.jsp").get();
				Ele = doc.select("li a[href*=?mode]");
				SharedPreferences notice_yonsei = getSharedPreferences("notice_yonsei",1); 	//
				editor = notice_yonsei.edit();
				
				String tmp1="";
				String tmp2="";
				
				tmp1 = notice_yonsei.getString("notice_yonsei1_url", "yonsei.ac.kr");
				tmp2 = Ele.get(0).attr("href");
				if( !(tmp1.equals(tmp2)) ){
					notification("yonsei");
				}
				/*
				if( !(notice_yonsei.getString("notice_yonsei1", "yonsei����1").equals(Ele.get(0).text())) ){
					notification("yonsei");
				}
				*/
				
				
				
				for(int i = 1; i<=10; i++){
					editor.putString("notice_yonsei"+i, Ele.get(i-1).text());
					editor.putString("notice_yonsei"+i+"_url", Ele.get(i-1).attr("href") );
				}
				editor.commit();
				
				//�İ�
				doc= Jsoup.connect("http://cs.yonsei.ac.kr/cs/sub05_1.php?nSeq=1").get();
				Ele = doc.select("td.subject a");
				SharedPreferences notice_cs = getSharedPreferences("notice_cs",1); 	//
				editor = notice_cs.edit();
								
				tmp1 = notice_cs.getString("notice_cs1_url", "cs.yonsei.ac.kr");
				tmp2 = Ele.get(0).attr("href");
				if( !(tmp1.equals(tmp2)) ){
					notification("cs");
				}
				
				for(int i = 1; i<=10; i++){
					editor.putString("notice_cs"+i,  Ele.get(i-1).text());
					editor.putString("notice_cs"+i+"_url", Ele.get(i-1).attr("href") );
				}
				editor.commit();
				
				//�м�������
				doc= Jsoup.connect("http://library.yonsei.ac.kr/bbs/list/1").get();
				Ele = doc.select("td.title a");
				SharedPreferences notice_lib = getSharedPreferences("notice_lib",1); 	//
				editor = notice_lib.edit();
								
				tmp1 = notice_lib.getString("notice_lib1_url", "lib����1");
				tmp2 = Ele.get(0).attr("href");
				if( !(tmp1.equals(tmp2)) ){
					notification("lib");
				}
				
				for(int i = 1; i<=10; i++){
					editor.putString("notice_lib"+i, Ele.get(i-1).text());
					editor.putString("notice_lib"+i+"_url", Ele.get(i-1).attr("href") );
				}
				editor.commit();
				
				//��������
				doc = Jsoup.connect("http://engineering.yonsei.ac.kr/new/list.asp?mid=m00_01").get();
				Ele = doc.select("td a.home");
				SharedPreferences notice_eng = getSharedPreferences("notice_eng",1); 	//
				editor = notice_eng.edit();
				
				
				tmp1 = notice_eng.getString("notice_eng1_url", "eng����1");
				tmp2 = Ele.get(0).attr("href");
				if( !(tmp1.equals(tmp2)) ){
					notification("eng");
				}
				
				for(int i = 1; i<=10; i++){
					editor.putString("notice_eng"+i,Ele.get(i-1).text());
					editor.putString("notice_eng"+i+"_url", Ele.get(i-1).attr("href") );
				}
				editor.commit();
				
				//����ó
				doc = Jsoup.connect("http://admission.yonsei.ac.kr/seoul/admission/html/counsel/notice.asp").get();
				Ele = doc.select("td.subject a");
				
				SharedPreferences notice_admission = getSharedPreferences("notice_admission",1); 	//
				editor = notice_admission.edit();
								
				tmp1 = notice_admission.getString("notice_admission1_url", "admission����1");
				tmp2 = Ele.get(0).attr("href");
				if( !(tmp1.equals(tmp2)) ){
					notification("admission");
				}
				
				
				for(int i = 1; i<=10; i++){
					editor.putString("notice_admission"+i, Ele.get(i-1).getElementsByClass("tit").text() );
					editor.putString("notice_admission"+i+"_url", Ele.get(i-1).attr("href") );
					
				}
				editor.commit();

				
				//SCSC
				doc = Jsoup.connect("http://cs.yonsei.ac.kr/scsc/?forum=%ed%8f%ac%eb%9f%bc").get();
				Ele = doc.select("li a.bbp-topic-permalink");
				SharedPreferences notice_scsc = getSharedPreferences("notice_scsc",1); 	//
				editor = notice_scsc.edit();
				
				tmp1 = notice_scsc.getString("notice_scsc1_url", "scsc����1");
				tmp2 = Ele.get(0).attr("href");
				if( !(tmp1.equals(tmp2)) ){
					notification("scsc");
				}
				
				for(int i = 1; i<=10; i++){
					editor.putString("notice_scsc"+i, Ele.get(i-1).text());
					editor.putString("notice_scsc"+i+"_url", Ele.get(i-1).attr("href") );
					
				}
				editor.commit();
				
				//����ķ
				doc = doc = Jsoup.connect("http://www.yonsei.ac.kr/wj/support/notice.jsp").get();
				Ele =Ele = doc.select("li a[href*=?mode]");
				SharedPreferences notice_wonju = getSharedPreferences("notice_wonju",1); 	//
				editor = notice_wonju.edit();
				
				Log.v("ssw", notice_wonju.getString("notice_wonju1_url", "wonju����1"));
				Log.v("ssw", Ele.get(0).attr("href") );
				
				tmp1 = notice_wonju.getString("notice_wonju1_url", "wonju����1");
				tmp2 = Ele.get(0).attr("href");
				if( !(tmp1.equals(tmp2)) ){
					notification("wonju");
				}
				
				
				for(int i = 1; i<=10; i++){
					editor.putString("notice_wonju"+i, Ele.get(i-1).getElementsByTag("Strong").text());
					editor.putString("notice_wonju"+i+"_url", Ele.get(i-1).attr("href") );
				}
				editor.commit();
				
			}catch (Exception ex) {
				
			}

			Log.v("ssw", "���� ���� �߻�!!!!!");
			
			////////////////////////////////////////////////////////////////////////////////
			// ���� ���� ����
			////////////////////////////////////////////////////////////////////////////////
		}

		@Override
		protected void onPostExecute(Void result){
	        super.onPostExecute(result);
		}
		
	}

	
	// �����ֱⰡ �Ǿ����� �̺�Ʈ ��� ������ �޾ƿ� Preference�� �ϴ� ����
	public void renew(){
		JsoupAsyncTask Task = new JsoupAsyncTask();
   	 	Task.execute();
	}
	
	
	
	//ȭ���ܿ� �˸�â�� ����ִ� �Լ�
		public void notification(String site)
		{			
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		    boolean yonsei_home = pref.getBoolean("YONSEI_HOME", false);
			boolean yonsei_cs = pref.getBoolean("YONSEI_CS", false);
			boolean yonsei_library = pref.getBoolean("YONSEI_LIBRARY", false);
			boolean yonsei_engineering = pref.getBoolean("YONSEI_ENGINEERING", false);
			boolean yonsei_admission = pref.getBoolean("YONSEI_ADMISSION", false);
			boolean yonsei_scsc = pref.getBoolean("YONSEI_SCSC", false);
			boolean yonsei_wonju = pref.getBoolean("YONSEI_WONJU", false);
			
			NotificationManager nm=(NotificationManager)getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
			if(site.equals("yonsei")&&yonsei_home)
			{
				Notification notification =new Notification(R.drawable.ic_launcher,"���� �˸�",System.currentTimeMillis());
				PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this,MainActivity.class),0);		
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(getApplicationContext(),"�������б�","���ο� ������ �ö�Խ��ϴ�", pi);
				nm.notify(0,notification);
			}
			else if(site.equals("cs")&&yonsei_cs)
			{
				Notification notification =new Notification(R.drawable.ic_launcher,"���� �˸�",System.currentTimeMillis());
				PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this,MainActivity.class),0);		
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(getApplicationContext(),"��ǻ�Ͱ��а�","���ο� ������ �ö�Խ��ϴ�", pi);
				nm.notify(1,notification);
			}
			else if(site.equals("lib")&&yonsei_library){
				Notification notification =new Notification(R.drawable.ic_launcher,"���� �˸�",System.currentTimeMillis());
				PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this,MainActivity.class),0);		
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(getApplicationContext(),"�м�������","���ο� ������ �ö�Խ��ϴ�", pi);
				nm.notify(2,notification);
			}
			else if(site.equals("eng")&&yonsei_engineering){
				Notification notification =new Notification(R.drawable.ic_launcher,"���� �˸�",System.currentTimeMillis());
				PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this,MainActivity.class),0);		
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(getApplicationContext(),"��������","���ο� ������ �ö�Խ��ϴ�", pi);
				nm.notify(3,notification);
			}
			else if(site.equals("admission")&&yonsei_admission){
				Notification notification =new Notification(R.drawable.ic_launcher,"���� �˸�",System.currentTimeMillis());
				PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this,MainActivity.class),0);		
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(getApplicationContext(),"����ó","���ο� ������ �ö�Խ��ϴ�", pi);
				nm.notify(4,notification);
			}
			else if(site.equals("scsc")&&yonsei_scsc){
				Notification notification =new Notification(R.drawable.ic_launcher,"���� �˸�",System.currentTimeMillis());
				PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this,MainActivity.class),0);		
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(getApplicationContext(),"SCSC","���ο� ������ �ö�Խ��ϴ�", pi);
				nm.notify(5,notification);
			}
			else if(site.equals("wonju")&&yonsei_wonju){
				Notification notification =new Notification(R.drawable.ic_launcher,"���� �˸�",System.currentTimeMillis());
				PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, new Intent(this,MainActivity.class),0);		
				notification.flags=Notification.FLAG_AUTO_CANCEL;
				notification.setLatestEventInfo(getApplicationContext(),"����ķ�۽�","���ο� ������ �ö�Խ��ϴ�", pi);
				nm.notify(6,notification);
			}
			
			
		}
	
	
	
}
