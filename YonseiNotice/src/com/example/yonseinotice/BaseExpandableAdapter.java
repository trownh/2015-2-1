package com.example.yonseinotice;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BaseExpandableAdapter extends BaseExpandableListAdapter{
	
	private ArrayList<String> groupList = null;
	private ArrayList<ArrayList<String>> childList = null;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	
	public BaseExpandableAdapter(Context c, ArrayList<String> groupList, 
			ArrayList<ArrayList<String>> childList){
		super();
		this.inflater = LayoutInflater.from(c);
		this.groupList = groupList;
		this.childList = childList;
	}
	
	// 그룹 포지션을 반환한다.
	@Override
	public String getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	// 그룹 사이즈를 반환한다.
	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	// 그룹 ID를 반환한다.
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 그룹뷰 각각의 ROW 
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		View v = convertView;

		final int tmp = groupPosition;
		
		if(v == null){
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_row, parent, false);
			viewHolder.tv_groupName = (TextView) v.findViewById(R.id.tv_group);
			viewHolder.iv_image = (ImageView) v.findViewById(R.id.iv_image);
			
			
			viewHolder.btn_comment = (Button) v.findViewById(R.id.btn_comment);
			viewHolder.btn_comment.setFocusable(false);
			viewHolder.btn_comment.setOnClickListener(new Button.OnClickListener() {
			    public void onClick(View v) {
			    	
			    		Intent intent = new Intent(v.getContext(),comment.class) .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			    		intent.putExtra("group", tmp);
			    		v.getContext().startActivity(intent);
			    }
			});
			
			
			viewHolder.btn_bookmark = (Button) v.findViewById(R.id.btn_bookmark);
			viewHolder.btn_bookmark.setVisibility(View.GONE);
			
			
			v.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		
		// 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
		if(isExpanded){
			viewHolder.iv_image.setImageResource(R.drawable.up);
			//viewHolder.iv_image.setBackgroundColor(Color.GREEN);
		}else{
			viewHolder.iv_image.setImageResource(R.drawable.down);
			//viewHolder.iv_image.setBackgroundColor(Color.WHITE);
		}
		
		viewHolder.tv_groupName.setText(getGroup(groupPosition));
		viewHolder.tv_groupName.setTextColor(Color.WHITE);
		
		return v;
	}
	
	// 차일드뷰를 반환한다.
	@Override
	public String getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}
	
	// 차일드뷰 사이즈를 반환한다.
	@Override
	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
	}

	// 차일드뷰 ID를 반환한다.
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 차일드뷰 각각의 ROW
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
			View v = convertView;
		
    		if(v == null){
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_row, null);
			viewHolder.tv_childName = (TextView) v.findViewById(R.id.tv_child);
			
			viewHolder.btn_comment = (Button) v.findViewById(R.id.btn_comment);
			viewHolder.btn_comment.setVisibility(View.GONE);
			
			viewHolder.btn_bookmark = (Button) v.findViewById(R.id.btn_bookmark);
			viewHolder.btn_bookmark.setVisibility(View.GONE);
			/*
			viewHolder.btn_bookmark = (Button) v.findViewById(R.id.btn_bookmark);
			viewHolder.btn_bookmark.setFocusable(false);
			viewHolder.btn_bookmark.setTag(groupPosition*100+childPosition);
			viewHolder.btn_bookmark.setOnClickListener(new Button.OnClickListener() {
			    public void onClick(View v) {
			    	
			    	Toast.makeText(v.getContext(),v.getTag().toString(), 1).show();;
			
			    	String mSequence[] = {"","","","","","","","","",""} ;
				    String mUrl[] = {"","","","","","","","","",""} ;
				    int mSeq = 0;
				    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
				    boolean yonsei_home = pref.getBoolean("YONSEI_HOME", false);
					boolean yonsei_cs = pref.getBoolean("YONSEI_CS", false);
					boolean yonsei_library = pref.getBoolean("YONSEI_LIBRARY", false);
					boolean yonsei_engineering = pref.getBoolean("YONSEI_ENGINEERING", false);
					boolean yonsei_admission = pref.getBoolean("YONSEI_ADMISSION", false);
					boolean yonsei_scsc = pref.getBoolean("YONSEI_SCSC", false);
					boolean yonsei_wonju = pref.getBoolean("YONSEI_WONJU", false);
								
					if(yonsei_home){
						mUrl[mSeq] = "http://yonsei.ac.kr/sc/support/notice.jsp";
				        mSequence[mSeq++] = "notice_yonsei";
					}
					if(yonsei_cs){
						mUrl[mSeq] = "http://cs.yonsei.ac.kr/cs/";
				        mSequence[mSeq++] = "notice_cs";
					}
					if(yonsei_library){
						mUrl[mSeq] = "http://library.yonsei.ac.kr/";
				        mSequence[mSeq++] = "notice_lib";
					}
					if(yonsei_engineering){
						mUrl[mSeq] = "http://engineering.yonsei.ac.kr/new/";
						mSequence[mSeq++] = "notice_eng";
					}
			        if(yonsei_admission){
			        	mUrl[mSeq] = "";
				        mSequence[mSeq++] = "notice_admission";
			        }
			        if(yonsei_scsc){
			        	mUrl[mSeq] = "";
				        mSequence[mSeq++] = "notice_scsc";
			        }
			        if(yonsei_wonju){
			        	mUrl[mSeq] = "http://www.yonsei.ac.kr/wj/support/notice.jsp";
				        mSequence[mSeq++] = "notice_wonju";
			        }
			        
			        
			    	
			    }
			});
			*/
			
			
			v.setTag(viewHolder);
			
		}else{
			viewHolder = (ViewHolder)v.getTag();
		}
		
		viewHolder.tv_childName.setText(getChild(groupPosition, childPosition));
		viewHolder.tv_childName.setTextColor(Color.WHITE);
		
		return v;
	}

	@Override
	public boolean hasStableIds() {	return true; }

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }
	
	class ViewHolder {
		public ImageView iv_image;
		public TextView tv_groupName;
		public TextView tv_childName;
		
		public Button btn_comment;
		public Button btn_bookmark;
	}

}

