package com.example.yonseinotice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class login extends Activity {
	
	static Socket socket;
	protected int port = 9999;
	protected String ip = "211.245.48.82";
	static BufferedReader sin;
	static PrintWriter sout;
	static String myid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this,splash.class));
		setContentView(R.layout.login);
		init();
	}
	
	public void init(){
		
		//占쏙옙 占쏙옙占쏙옙 占싱븝옙트 占쏙옙占�
   	 	backPressCloseHandler = new BackPressCloseHandler(this);
   	 	Thread worker = new Thread()
		{
			public void run()
			{
				try{
					socket = new Socket(ip, port);
					sout = new PrintWriter(socket.getOutputStream(), true);
					sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					sout.println("Application start.");
				}
				catch(IOException e){
				}
			}
		};
		worker.start();
   	 	
	}

	public void mOnClick(View v)
    {
		if(v.getId()==R.id.login_btn_login)
    	{
				sout.println("login");
				EditText et = (EditText)this.findViewById(R.id.login_et_id);
				String s = et.getText().toString();
				sout.println(s);
				et = (EditText)this.findViewById(R.id.login_et_password);
				s = et.getText().toString();
				sout.println(s);
				
				
				
				readworker rw = new readworker(socket);
				rw.start();
				try
				{
					rw.join();
				}
				catch(Exception e)
				{
					
				}
				
				s = rw.getoutput();
				
				if(s.equals(new String("success")))
				{
					myid = ((EditText)this.findViewById(R.id.login_et_id)).getText().toString();
					Toast.makeText(login.this, "Login success", Toast.LENGTH_LONG).show();
					startActivity(new Intent(this,MainActivity.class));
				}
				else
				{
					Toast.makeText(login.this, "Wrong id or password. Retype id or password", Toast.LENGTH_LONG).show();
				}
    	}
		else if(v.getId()==R.id.login_btn_signup)
		{
			
			 LayoutInflater inflater=getLayoutInflater();
			 final View dialogView= inflater.inflate(R.layout.signup, null);
			 AlertDialog.Builder buider= new AlertDialog.Builder(this); //AlertDialog.Builder 占쏙옙체 占쏙옙占쏙옙 
			 buider.setTitle("회원가입"); //Dialog 占쏙옙占쏙옙
			 buider.setIcon(R.drawable.signup); //占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占싱뱄옙占쏙옙(占쏙옙占싹댐옙 占싱뱄옙占쏙옙 占쏙옙占쏙옙)
			 buider.setView(dialogView); //占쏙옙占쏙옙占쏙옙 inflater占쏙옙 占쏙옙占쏙옙 dialogView 占쏙옙체 占쏙옙占쏙옙 (Customize)
			   buider.setPositiveButton("확인", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String toserver = "";
					toserver = toserver.concat("signup");
					
					
					EditText et_id = (EditText)dialogView.findViewById(R.id.signup_et_id);
					String s_id = et_id.getText().toString();
					
					
					EditText et_password = (EditText)dialogView.findViewById(R.id.signup_et_password);
					String s_password = et_password.getText().toString();
					
					
					EditText et_passwordcheck = (EditText)dialogView.findViewById(R.id.signup_et_passwordcheck);
					String s_passwordcheck = et_passwordcheck.getText().toString();
					
				
					EditText et_nick = (EditText)dialogView.findViewById(R.id.signup_et_nickname);
					String s_nick = et_nick.getText().toString();
					
					
					EditText et_depart = (EditText)dialogView.findViewById(R.id.signup_et_department);
					String s_depart = et_depart.getText().toString();
					
					
					if(s_id.equals("")||s_password.equals("")||s_passwordcheck.equals("")||s_nick.equals("")||s_depart.equals("")){
						Toast.makeText(login.this, "회원 정보를 입력하세요.", Toast.LENGTH_LONG).show();
					}
					else if(!(s_password.equals(s_passwordcheck)) ){
						Toast.makeText(login.this, "비밀번호를 다르게 입력하셨습니다.", Toast.LENGTH_LONG).show();
					}
					else{
						sout.println(toserver);
						sout.println(s_id);
						sout.println(s_password);
						sout.println(s_passwordcheck);
						sout.println(s_nick);
						sout.println(s_depart);
						
						readworker rw = new readworker(socket);
						rw.start();
						try{
							rw.join();
						}
						catch(Exception e){}
						String s = rw.getoutput();
						if(s.equals(new String("success")))
						{
							Toast.makeText(login.this, "Signed up successfully", Toast.LENGTH_LONG).show();
							myid = s_id;
						}
						else
						{
							Toast.makeText(login.this, "ID alrady exists, try new Id", Toast.LENGTH_LONG).show();
						}
						
					}
					
					  
				}
			   });
			   buider.setNegativeButton("취소", new OnClickListener() {
			    //Dialog占쏙옙 "Cancel"占싱띰옙占� 타占쏙옙틀占쏙옙 占쏙옙튼占쏙옙 占쏙옙占쏙옙
			      
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			     Toast.makeText(login.this, "회원가입을 취소합니다.", Toast.LENGTH_SHORT).show();       
			    }
			   });
			   
			   //占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 AlertDialog 占쏙옙체 占쏙옙占쏙옙
			   AlertDialog dialog=buider.create();
			   //Dialog占쏙옙 占쌕깍옙占쏙옙占쏙옙 占쏙옙치占쏙옙占쏙옙 占쏙옙 Dialog占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
			   dialog.setCanceledOnTouchOutside(false);//占쏙옙占쏙옙占쏙옙占쏙옙 占십듸옙占쏙옙 占쏙옙占쏙옙
			   //Dialog 占쏙옙占싱깍옙  
			   dialog.show();
		}
    }
	
	
		// 占쏙옙 占쏙옙占쏙옙 占싱븝옙트
	
		public class BackPressCloseHandler extends Activity {	
		 	private long backKeyPressedTime = 0;
		 	private Toast toast;
		 	private Activity activity;
		 	public BackPressCloseHandler(login context) {
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
		        		sout.println("connection finish");
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
		
		// 占쏙옙 占쏙옙占쏙옙 占싱븝옙트
	


		class readworker extends Thread
		{
			private BufferedReader sin;
			private Socket sc;
			String output;
			
			
			public readworker(Socket s)
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


}
