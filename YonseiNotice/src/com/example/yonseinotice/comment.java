package com.example.yonseinotice;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import java.net.Socket;
import android.os.AsyncTask;

public class comment extends Activity{
	
	private ListView				m_listview;
	private CustomAdapter           m_Adapter;
	private ArrayList<String>		item; 	
	private EditText				m_edittext;
	private readworkerC rd;
   private int check = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		rd = new readworkerC(login.socket);
		String mSequence[] = {"","","","","","","","","",""} ;
	    String mUrl[] = {"","","","","","","","","",""} ;
	    int mSeq = 0;
	    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    boolean yonsei_home = pref.getBoolean("YONSEI_HOME", false);
		boolean yonsei_cs = pref.getBoolean("YONSEI_CS", false);
		boolean yonsei_library = pref.getBoolean("YONSEI_LIBRARY", false);
		boolean yonsei_engineering = pref.getBoolean("YONSEI_ENGINEERING", false);
		boolean yonsei_admission = pref.getBoolean("YONSEI_ADMISSION", false);
		boolean yonsei_scsc = pref.getBoolean("YONSEI_SCSC", false);
		boolean yonsei_wonju = pref.getBoolean("YONSEI_WONJU", false);
		
		Intent intent = getIntent();
		int group = intent.getExtras().getInt("group");
		
		if(yonsei_home){
	        mSequence[mSeq++] = "notice_yonsei";
		}
		if(yonsei_cs){
	        mSequence[mSeq++] = "notice_cs";
		}
		if(yonsei_library){
	        mSequence[mSeq++] = "notice_lib";
		}
		if(yonsei_engineering){
			mSequence[mSeq++] = "notice_eng";
		}
        if(yonsei_admission){
	        mSequence[mSeq++] = "notice_admission";
        }
        if(yonsei_scsc){
	        mSequence[mSeq++] = "notice_scsc";
        }
        if(yonsei_wonju){
	        mSequence[mSeq++] = "notice_wonju";
        }
        
        TextView tv = (TextView)findViewById(R.id.test123);
        tv.setText(mSequence[group]);
        
        if(mSequence[group].equals(new String("notice_yonsei")))
        {
        	check = 1;
        }
        if(mSequence[group].equals(new String("notice_cs")))
        {
        	check = 2;
        }
        if(mSequence[group].equals(new String("notice_lib")))
        {
        	check = 3;
        }
        if(mSequence[group].equals(new String("notice_eng")))
        {
        	check = 4;
        }
        if(mSequence[group].equals(new String("notice_admission")))
        {
        	check = 5;
        }
        if(mSequence[group].equals(new String("notice_scsc")))
        {
        	check = 6;
        }
        if(mSequence[group].equals(new String("notice_wonju")))
        {
        	check = 7;
        }
		
        item = new ArrayList<String>();
        // Ŀ���� ����� ����
        m_Adapter = new CustomAdapter();
         
        CAsyn cs = new CAsyn();
        cs.execute();
        
        // Xml���� �߰��� ListView ����
        m_listview = (ListView) findViewById(R.id.comment_lv);
 
        // ListView�� ����� ����
        m_listview.setAdapter(m_Adapter);
        
        
        m_edittext = (EditText)findViewById(R.id.comment_et);
        
	}
	
	
	private void list_refresh() {
		// TODO Auto-generated method stub
		login.sout.println("reqcomment");
		login.sout.println(String.valueOf(check));
		rd.run();
		try{
			rd.join();
		}
		catch(Exception e){ }
		int size = Integer.parseInt(rd.getoutput());
		for(int i=0; i<size; i++){
			try
			{
			rd.run();
			try
			{
				rd.join();
			}
			catch(Exception e) { login.sout.println("ex 1");}
			String commid = rd.getoutput();
			rd.run();
			try{
				rd.join();
			}
			catch(Exception e){}
			String content = rd.getoutput();
			m_Adapter.add(commid,content);
			}
			catch(Exception e){   }
        }
	}


	public void mOnClick(View v)
    {
		if(v.getId()==R.id.comment_btn)
    	{
			login.sout.println("addcomment");
			login.sout.println(login.myid);
			login.sout.println(m_edittext.getText().toString());
			login.sout.println(String.valueOf(check));
			item.add(m_edittext.getText().toString());
			m_listview = null;
			m_listview = (ListView) findViewById(R.id.comment_lv);
			 m_Adapter= null;
			 m_Adapter = new CustomAdapter();
			 m_listview.setAdapter(m_Adapter);
			 CAsyn cs = new CAsyn();
		     cs.execute();
			m_edittext.setText("");
			InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(m_edittext.getWindowToken(), 0);
    	}
    }


	
	class CAsyn extends AsyncTask<Void,Void,Void>
	{
		
		private ProgressDialog dialog;
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(comment.this);
			dialog.show();
			
		}
		protected Void doInBackground(Void... params) 
		{
			//super.onPreExecute();
			list_refresh();
			return null;
		}
		
		
		protected void onPostExecute(Void r) {
			dialog.dismiss();
			super.onPostExecute(null);
		}
	}
}


class readworkerC extends Thread
{
	private BufferedReader sin;
	private Socket sc;
	String output;
	
	
	public readworkerC(Socket s)
	{
		sc = s;
		try{
			sin = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		}
		catch(IOException e){}
	}
	
	public void run()
	{
		while(true){
			try{
				output = sin.readLine();
				if(output!=null){
					break;
				}
			}	
			catch(IOException e){	
			}
		}
	}
	
	public String getoutput(){
		return output;
	}
	
	
}


	