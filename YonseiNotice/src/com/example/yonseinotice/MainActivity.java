package com.example.yonseinotice;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	protected void onResume() {
		super.onResume();
		additem();

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
			// 환占썸설占쏙옙창占쏙옙占쏙옙

			startActivity(new Intent(this, setting.class));

			return true;
		} else if (id == R.id.action_refresh) {
			refresh();
		}
		return super.onOptionsItemSelected(item);
	}

	private void refresh() {
		// TODO Auto-generated method stub
		JsoupAsyncTask Task = new JsoupAsyncTask();
		Task.execute();
	}

	public void init() {
		// ActionBar 창占쏙옙 占싫븝옙占싱댐옙 占쏙옙荑� 占쏙옙占싱듸옙占쏙옙 占쏙옙占쏙옙
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
		}

		// 占쏙옙 占쏙옙占쏙옙 占싱븝옙트 占쏙옙占�
		backPressCloseHandler = new BackPressCloseHandler(this);

		additem();

		this.startService(new Intent("yonseinotice_service"));

	}

	private ArrayList<String> mGroupList = null;
	private ArrayList<ArrayList<String>> mChildList = null;

	private ArrayList<String> mChildListContent_yonsei = null;
	private ArrayList<String> mChildListContent_cs = null;
	private ArrayList<String> mChildListContent_lib = null;
	private ArrayList<String> mChildListContent_eng = null;
	private ArrayList<String> mChildListContent_adm = null;
	private ArrayList<String> mChildListContent_scsc = null;
	private ArrayList<String> mChildListContent_won = null;

	// private ArrayList<String> mSequence = null;
	private String mSequence[] = { "", "", "", "", "", "", "", "", "", "" };
	private String mUrl[] = { "", "", "", "", "", "", "", "", "", "" };
	private int mSeq = 0;

	private ExpandableListView mListView;

	private void additem() {
		// TODO Auto-generated method stub
		mListView = (ExpandableListView) findViewById(R.id.main_explist);

		mChildList = new ArrayList<ArrayList<String>>();
		mGroupList = new ArrayList<String>();

		for (int i = 0; i < mSequence.length; i++) {
			mSequence[i] = "";
			mUrl[i] = "";
			mSeq = 0;
		}

		mChildListContent_yonsei = new ArrayList<String>();
		mChildListContent_cs = new ArrayList<String>();
		mChildListContent_lib = new ArrayList<String>();
		mChildListContent_eng = new ArrayList<String>();
		mChildListContent_adm = new ArrayList<String>();
		mChildListContent_scsc = new ArrayList<String>();
		mChildListContent_won = new ArrayList<String>();

		final SharedPreferences notice = getSharedPreferences("notice", 1);
		int max_notice = notice.getInt("max_notice", 5);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		boolean yonsei_home = pref.getBoolean("YONSEI_HOME", true);
		boolean yonsei_cs = pref.getBoolean("YONSEI_CS", true);
		boolean yonsei_library = pref.getBoolean("YONSEI_LIBRARY", true);
		boolean yonsei_engineering = pref.getBoolean("YONSEI_ENGINEERING", true);
		boolean yonsei_admission = pref.getBoolean("YONSEI_ADMISSION", true);
		boolean yonsei_scsc = pref.getBoolean("YONSEI_SCSC", true);
		boolean yonsei_wonju = pref.getBoolean("YONSEI_WONJU", true);

		//////////////////////////////////// --------------------------------------
		// 캐占시울옙 占쏙옙占쏙옙占� 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占�
		////////////////////////////////////

		SharedPreferences notice_get;
		if (yonsei_home) {
			mGroupList.add("연세대학교");
			for (int i = 0; i < max_notice; i++) {
				notice_get = getSharedPreferences("notice_yonsei", 1);
				String tmp = notice_get.getString("notice_yonsei" + (i + 1), "yonsei공지" + (i + 1)); // 캐占시곤옙
																										// |
																										// 占쏙옙占쏙옙트占쏙옙
				mChildListContent_yonsei.add(tmp);
			}
			mChildList.add(mChildListContent_yonsei);

			mUrl[mSeq] = "http://yonsei.ac.kr/sc/support/notice.jsp";
			mSequence[mSeq++] = "notice_yonsei";
		}
		if (yonsei_cs) {
			mGroupList.add("컴퓨터과학과");
			for (int i = 0; i < max_notice; i++) {
				notice_get = getSharedPreferences("notice_cs", 1);
				String tmp = notice_get.getString("notice_cs" + (i + 1), "cs공지" + (i + 1)); // 캐占시곤옙
																								// |
																								// 占쏙옙占쏙옙트占쏙옙
				mChildListContent_cs.add(tmp);
			}
			mChildList.add(mChildListContent_cs);

			mUrl[mSeq] = "http://cs.yonsei.ac.kr/cs/";
			mSequence[mSeq++] = "notice_cs";
		}
		if (yonsei_library) {
			mGroupList.add("학술정보원");
			for (int i = 0; i < max_notice; i++) {
				notice_get = getSharedPreferences("notice_lib", 1);
				String tmp = notice_get.getString("notice_lib" + (i + 1), "lib공지" + (i + 1)); // 캐占시곤옙
																								// |
																								// 占쏙옙占쏙옙트占쏙옙
				mChildListContent_lib.add(tmp);
			}
			mChildList.add(mChildListContent_lib);

			mUrl[mSeq] = "http://library.yonsei.ac.kr/";
			mSequence[mSeq++] = "notice_lib";
		}
		if (yonsei_engineering) {
			mGroupList.add("공과대학");
			for (int i = 0; i < max_notice; i++) {
				notice_get = getSharedPreferences("notice_eng", 1);
				String tmp = notice_get.getString("notice_eng" + (i + 1), "eng공지" + (i + 1)); // 캐占시곤옙
																								// |
																								// 占쏙옙占쏙옙트占쏙옙
				mChildListContent_eng.add(tmp);
			}
			mChildList.add(mChildListContent_eng);

			mUrl[mSeq] = "http://engineering.yonsei.ac.kr/new/";
			mSequence[mSeq++] = "notice_eng";
		}
		if (yonsei_admission) {
			mGroupList.add("입학처");
			for (int i = 0; i < max_notice; i++) {
				notice_get = getSharedPreferences("notice_admission", 1);
				String tmp = notice_get.getString("notice_admission" + (i + 1), "입학처공지" + (i + 1)); // 캐占시곤옙
																										// |
																										// 占쏙옙占쏙옙트占쏙옙
				mChildListContent_adm.add(tmp);
			}
			mChildList.add(mChildListContent_adm);

			mUrl[mSeq] = "";
			mSequence[mSeq++] = "notice_admission";
		}
		if (yonsei_scsc) {
			mGroupList.add("SCSC");
			for (int i = 0; i < max_notice; i++) {
				notice_get = getSharedPreferences("notice_scsc", 1);
				String tmp = notice_get.getString("notice_scsc" + (i + 1), "sc공지" + (i + 1)); // 캐占시곤옙
																								// |
																								// 占쏙옙占쏙옙트占쏙옙
				mChildListContent_scsc.add(tmp);
			}
			mChildList.add(mChildListContent_scsc);

			mUrl[mSeq] = "";
			mSequence[mSeq++] = "notice_scsc";
		}
		if (yonsei_wonju) {
			mGroupList.add("원주캠퍼스");
			for (int i = 0; i < max_notice; i++) {
				notice_get = getSharedPreferences("notice_wonju", 1);
				String tmp = notice_get.getString("notice_wonju" + (i + 1), "bus공지" + (i + 1)); // 캐占시곤옙
																									// |
																									// 占쏙옙占쏙옙트占쏙옙
				mChildListContent_won.add(tmp);
			}
			mChildList.add(mChildListContent_won);

			mUrl[mSeq] = "http://www.yonsei.ac.kr/wj/support/notice.jsp";
			mSequence[mSeq++] = "notice_wonju";
		}

		mListView.setAdapter(new BaseExpandableAdapter(this, mGroupList, mChildList));

		////////////////////////////////////
		// 캐占시울옙 占쏙옙占쏙옙占� 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占�
		//////////////////////////////////// ---------------------------------------------------

		// 占쌓뤄옙 클占쏙옙 占쏙옙占쏙옙 占쏙옙占� 占싱븝옙트
		mListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(), "g click = " +
				// groupPosition, Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// 占쏙옙占싹듸옙 클占쏙옙 占쏙옙占쏙옙 占쏙옙占� 占싱븝옙트
		mListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
					long id) {

				// Toast.makeText(getApplicationContext(), "c click = " +
				// mSequence[groupPosition], Toast.LENGTH_SHORT).show();

				String tmp_string = mSequence[groupPosition];
				SharedPreferences tmp_preference = getSharedPreferences(tmp_string, 1);
				String tmp_url = mUrl[groupPosition]
						+ tmp_preference.getString(mSequence[groupPosition] + (childPosition + 1) + "_url", "");

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmp_url));
				startActivity(intent);
				return false;
			}
		});

		// 占쌓뤄옙占쏙옙 占쏙옙占쏙옙 占쏙옙占� 占싱븝옙트
		mListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {
				// Toast.makeText(getApplicationContext(), "g Collapse = " +
				// groupPosition, Toast.LENGTH_SHORT).show();
			}
		});

		// 占쌓뤄옙占쏙옙 占쏙옙占쏙옙 占쏙옙占� 占싱븝옙트
		mListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				// Toast.makeText(getApplicationContext(), "g Expand = " +
				// groupPosition, Toast.LENGTH_SHORT).show();
			}
		});

	}

	// 占쏙옙 占쏙옙占쏙옙 占싱븝옙트
	public class BackPressCloseHandler extends Activity {
		private long backKeyPressedTime = 0;
		private Toast toast;
		private Activity activity;

		public BackPressCloseHandler(MainActivity context) {
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
				login.sout.println("connection finish");
				toast.cancel();
			}
		}

		private void showGuide() {
			toast = Toast.makeText(activity, "뒤로 버튼을 한번 더 터치하시면 종료됩니다.", Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private BackPressCloseHandler backPressCloseHandler;

	public void onBackPressed() {
		backPressCloseHandler.onBackPressed();
	}
	// 占쏙옙 占쏙옙占쏙옙 占싱븝옙트

	public static void add_LikeList(int Group, int Child) {
		Log.v("ssw", Group + "" + Child + "");
	}

	// 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 AsyncTask
	private ProgressDialog asyncDialog;

	private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {

			asyncDialog = new ProgressDialog(MainActivity.this);
			asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			asyncDialog.setMessage("로딩중입니다..");
			asyncDialog.show();

			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			saveitem();

			return null;
		}

		private void saveitem() {
			// TODO Auto-generated method stub
			////////////////////////////////////////////////////////////////////////////////
			// 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙
			////////////////////////////////////////////////////////////////////////////////

			SharedPreferences.Editor editor;
			try {
				Document doc;
				Elements Ele;

				// 占쏙옙占쏙옙占쏙옙占싻깍옙
				doc = Jsoup.connect("http://yonsei.ac.kr/sc/support/notice.jsp").get();
				Ele = doc.select("li a[href*=?mode]");
				SharedPreferences notice_yonsei = getSharedPreferences("notice_yonsei", 1); //
				editor = notice_yonsei.edit();

				for (int i = 1; i <= 10; i++) {
					editor.putString("notice_yonsei" + i, Ele.get(i - 1).text());
					editor.putString("notice_yonsei" + i + "_url", Ele.get(i - 1).attr("href"));
				}
				editor.commit();

				// 占식곤옙
				doc = Jsoup.connect("http://cs.yonsei.ac.kr/cs/sub05_1.php?nSeq=1").get();
				Ele = doc.select("td.subject a");
				SharedPreferences notice_cs = getSharedPreferences("notice_cs", 1); //
				editor = notice_cs.edit();

				for (int i = 1; i <= 10; i++) {
					editor.putString("notice_cs" + i, Ele.get(i - 1).text());
					editor.putString("notice_cs" + i + "_url", Ele.get(i - 1).attr("href"));
				}
				editor.commit();

				// 占싻쇽옙占쏙옙占쏙옙占쏙옙
				doc = Jsoup.connect("http://library.yonsei.ac.kr/bbs/list/1").get();
				Ele = doc.select("td.title a");
				SharedPreferences notice_lib = getSharedPreferences("notice_lib", 1); //
				editor = notice_lib.edit();

				for (int i = 1; i <= 10; i++) {
					editor.putString("notice_lib" + i, Ele.get(i - 1).text());
					editor.putString("notice_lib" + i + "_url", Ele.get(i - 1).attr("href"));
				}
				editor.commit();

				// 占쏙옙占쏙옙占쏙옙占쏙옙
				doc = Jsoup.connect("http://engineering.yonsei.ac.kr/new/list.asp?mid=m00_01").get();
				Ele = doc.select("td a.home");
				SharedPreferences notice_eng = getSharedPreferences("notice_eng", 1); //
				editor = notice_eng.edit();

				for (int i = 1; i <= 10; i++) {
					editor.putString("notice_eng" + i, Ele.get(i - 1).text());
					editor.putString("notice_eng" + i + "_url", Ele.get(i - 1).attr("href"));
				}
				editor.commit();

				// 占쏙옙占쏙옙처
				doc = Jsoup.connect("http://admission.yonsei.ac.kr/seoul/admission/html/counsel/notice.asp").get();
				Ele = doc.select("td.subject a");

				SharedPreferences notice_admission = getSharedPreferences("notice_admission", 1); //
				editor = notice_admission.edit();

				for (int i = 1; i <= 10; i++) {
					editor.putString("notice_admission" + i, Ele.get(i - 1).getElementsByClass("tit").text());
					editor.putString("notice_admission" + i + "_url", Ele.get(i - 1).attr("href"));

				}
				editor.commit();

				// SCSC
				doc = Jsoup.connect("http://cs.yonsei.ac.kr/scsc/?forum=%ed%8f%ac%eb%9f%bc").get();
				Ele = doc.select("li a.bbp-topic-permalink");
				SharedPreferences notice_scsc = getSharedPreferences("notice_scsc", 1); //
				editor = notice_scsc.edit();

				for (int i = 1; i <= 10; i++) {
					editor.putString("notice_scsc" + i, Ele.get(i - 1).text());
					editor.putString("notice_scsc" + i + "_url", Ele.get(i - 1).attr("href"));

				}
				editor.commit();

				// 占쏙옙占쏙옙캠
				doc = doc = Jsoup.connect("http://www.yonsei.ac.kr/wj/support/notice.jsp").get();
				Ele = Ele = doc.select("li a[href*=?mode]");
				SharedPreferences notice_wonju = getSharedPreferences("notice_wonju", 1); //
				editor = notice_wonju.edit();

				for (int i = 1; i <= 10; i++) {
					editor.putString("notice_wonju" + i, Ele.get(i - 1).getElementsByTag("Strong").text());
					editor.putString("notice_wonju" + i + "_url", Ele.get(i - 1).attr("href"));
				}
				editor.commit();

			} catch (Exception ex) {

			}

			////////////////////////////////////////////////////////////////////////////////
			// 占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙
			////////////////////////////////////////////////////////////////////////////////
		}

		@Override
		protected void onPostExecute(Void result) {
			asyncDialog.dismiss();
			additem();
			super.onPostExecute(result);
		}

	}
	// 占쏙옙占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 Async

}
