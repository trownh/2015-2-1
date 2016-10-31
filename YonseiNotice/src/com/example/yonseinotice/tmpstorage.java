package com.example.yonseinotice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class tmpstorage extends Activity {

	private Button post,url;
	private TextView tv;
	private WebView wv;
	private EditText et;
	private String myResult="http://www.naver.com";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//환경설정창열기

	        startActivity(new Intent(this, setting.class));

			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void init(){
		//ActionBar 창이 안보이는 경우에 보이도록 설정
    	try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {}
    	
    	post = (Button)findViewById(R.id.post);
    	tv = (TextView)findViewById(R.id.test1);
    	wv = (WebView)findViewById(R.id.test2);
    	et = (EditText)findViewById(R.id.test0);
    	url = (Button)findViewById(R.id.url);
    	      // 웹뷰에서 자바 스크립트 사용
    	
    	
    	//앱 종료 이벤트 등록
    	 backPressCloseHandler = new BackPressCloseHandler(this);
    	
	}
	
	 public void mOnClick(View v)
	    {
	    	if(v.getId()==R.id.post)
	    	{
	    		new Httppost().execute(null,null,null);
	    		//tv.setText("http://naver.com");
	    		tv.setText(myResult);
	    		Toast.makeText(tmpstorage.this,myResult , 0).show();    		
	    	}  
	    	else if(v.getId()==R.id.test1)
	    	{
	    		 et.setText(tv.getText().toString());
	    	}
	    	else if(v.getId()==R.id.url)
	    	{
	    		wv.getSettings().setJavaScriptEnabled(true);
	    		String tmp = et.getText().toString();
	    		tmp = tmp.replaceAll("\\r|\\n", "");
	    		wv.loadUrl(tmp);
	    	}
	    }
	 

	
		 
	 private class Httppost extends AsyncTask<Void,Void,Void>{
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try { 
	             //-------------------------- 
	             //   URL 설정하고 접속하기 
	             //-------------------------- 
	             URL url = new URL("http://183.96.226.170/index.php");       // URL 설정 
	             HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속 
	             //-------------------------- 
	             //   전송 모드 설정 - 기본적인 설정이다 
	             //-------------------------- 
	             http.setDefaultUseCaches(false);                                            
	             http.setDoInput(true);                         // 서버에서 읽기 모드 지정 
	             http.setDoOutput(true);                       // 서버로 쓰기 모드 지정  
	             http.setRequestMethod("POST");         // 전송 방식은 POST 

	             // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다 
	             http.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
	             //-------------------------- 
	             //   서버로 값 전송 
	             //-------------------------- 
	             StringBuffer buffer = new StringBuffer(); 
	             
	             buffer.append("id").append("=").append("123").append("&");                 // php 변수에 값 대입 
	             buffer.append("pword").append("=").append("456").append("&");   // php 변수 앞에 '$' 붙이지 않는다 
	             buffer.append("title").append("=").append("789").append("&");           // 변수 구분은 '&' 사용  
	             buffer.append("subject").append("=").append("012"); 
	            
	             
	             OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8"); 
	             PrintWriter writer = new PrintWriter(outStream); 
	             writer.write(buffer.toString()); 
	             writer.flush(); 
	             //-------------------------- 
	             //   서버에서 전송받기 
	             //-------------------------- 
	             InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "UTF-8");  
	             BufferedReader reader = new BufferedReader(tmp); 
	             StringBuilder builder = new StringBuilder(); 
	             String str; 
	             while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다 
	            	 builder.append(str); 
	            	 //builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가 
	             }
	             Log.v("yonsei", myResult);
	              myResult = builder.toString();                       // 전송결과를 전역 변수에 저장 
	             //tv.setText(myResult); 
	            //Toast.makeText(MainActivity.this, "전송 후 결과 받음", 0).show(); 
			 	} catch (MalformedURLException e) { 
	               // 
			 	} catch (IOException e) { 
	               //  
			 	} // try 
			
			
			return null;
		}
		 
	 }
	 
	 
	 private void HttpPostData() {
		// TODO Auto-generated method stub
		 
		 try { 
             //-------------------------- 
             //   URL 설정하고 접속하기 
             //-------------------------- 
             URL url = new URL("http://183.96.226.170/index.php");       // URL 설정 
             HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속 
             //-------------------------- 
             //   전송 모드 설정 - 기본적인 설정이다 
             //-------------------------- 
             http.setDefaultUseCaches(false);                                            
             http.setDoInput(true);                         // 서버에서 읽기 모드 지정 
             http.setDoOutput(true);                       // 서버로 쓰기 모드 지정  
             http.setRequestMethod("POST");         // 전송 방식은 POST 

             // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다 
             //http.setRequestProperty("content-type", "application/x-www-form-urlencoded"); 
             //-------------------------- 
             //   서버로 값 전송 
             //-------------------------- 
             StringBuffer buffer = new StringBuffer(); 
             /*
             buffer.append("id").append("=").append("123").append("&");                 // php 변수에 값 대입 
             buffer.append("pword").append("=").append("456").append("&");   // php 변수 앞에 '$' 붙이지 않는다 
             buffer.append("title").append("=").append("789").append("&");           // 변수 구분은 '&' 사용  
             buffer.append("subject").append("=").append("012"); 
            */
             
             OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR"); 
             PrintWriter writer = new PrintWriter(outStream); 
             writer.write(buffer.toString()); 
             writer.flush(); 
             //-------------------------- 
             //   서버에서 전송받기 
             //-------------------------- 
             InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");  
             BufferedReader reader = new BufferedReader(tmp); 
             StringBuilder builder = new StringBuilder(); 
             String str; 
             while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다 
                  builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가 
             } 
             String myResult = builder.toString();                       // 전송결과를 전역 변수에 저장 
             tv.setText(myResult); 
            Toast.makeText(tmpstorage.this, "전송 후 결과 받음", 0).show(); 
		 	} catch (MalformedURLException e) { 
               // 
		 	} catch (IOException e) { 
               //  
		 	} // try 
		 
		 
	}







	class WishWebViewClient extends WebViewClient{
	    	@Override

	    	public boolean shouldOverrideUrlLoading(WebView view, String url){
	    		view.loadUrl(url);
	    		return true;

	    	}
	   }
	
	
	
	
	// 앱 종료 이벤트
	
	public class BackPressCloseHandler extends Activity {	
	 	private long backKeyPressedTime = 0;
	 	private Toast toast;
	 	private Activity activity;
	 	public BackPressCloseHandler(tmpstorage context) {
	 		this.activity = context;
	        }
	 	
	        public void onBackPressed() {
	        	if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
	 	        	backKeyPressedTime = System.currentTimeMillis();
	 		        showGuide();
	        		return;
	        	}
	        	if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {			
	        		activity.finish();
	        		toast.cancel();			
	        	}
	        }
	        
	        
	        private void showGuide() {
	        	toast = Toast.makeText(activity, "뒤로 버튼을 한번 더 터치하시면 종료됩니다.",
	        	Toast.LENGTH_SHORT);
	        	toast.show();
	        }
	}
	
	private BackPressCloseHandler backPressCloseHandler;
	
	public void onBackPressed() {
	    backPressCloseHandler.onBackPressed();
	}
	
	// 앱 종료 이벤트
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
