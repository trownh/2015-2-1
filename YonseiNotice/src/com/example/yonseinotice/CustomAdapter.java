package com.example.yonseinotice;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    
    // ���ڿ��� ���� �� ArrayList
    private ArrayList<String>   m_List;
    private ArrayList<String>   m_Listid;
     
    // ������
    public CustomAdapter() {
        m_List = new ArrayList<String>();
        m_Listid = new ArrayList<String>();
    }
 
    // ���� �������� ���� ����
    @Override
    public int getCount() {
        return m_List.size();
    }
 
    // ���� �������� ������Ʈ�� ����, Object�� ��Ȳ�� �°� �����ϰų� ���Ϲ��� ������Ʈ�� ĳ�����ؼ� ���
    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }
 
    // ������ position�� ID �� ����
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    // ��� �� ������ ����
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();
         
        // ����Ʈ�� ������鼭 ���� ȭ�鿡 ������ �ʴ� �������� converView�� null�� ���·� ��� ��
        if ( convertView == null ) {
            // view�� null�� ��� Ŀ���� ���̾ƿ��� ��� ��
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_row, parent, false);
        }
            
            TextView text1 = (TextView) convertView.findViewById(R.id.tv_unknown);
            text1.setTextColor(Color.WHITE);
            text1.setText(m_Listid.get(position).toString());
            
            // TextView�� ���� position�� ���ڿ� �߰�
            TextView text = (TextView) convertView.findViewById(R.id.tv_comment);
            text.setTextColor(Color.WHITE);
            text.setText(m_List.get(position).toString());             
         
        return convertView;
    }
     
    // �ܺο��� ������ �߰� ��û �� ���
    public void add(String id, String comment) {
    	m_Listid.add(id);
        m_List.add(comment);  
    }
     
    // �ܺο��� ������ ���� ��û �� ���
    public void remove(int _position) {
        m_List.remove(_position);
    }
}