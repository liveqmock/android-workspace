package com.mobileclient.util;

import java.util.List;  
import java.util.Map;

import com.mobileclient.service.SpecialFieldInfoService;
import com.mobileclient.activity.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater; 
import android.view.View;
import android.view.ViewGroup;  
import android.widget.ImageView; 
import android.widget.SimpleAdapter; 
import android.widget.TextView; 

public class ClassInfoSimpleAdapter extends SimpleAdapter { 
	/*��Ҫ�󶨵Ŀؼ���Դid*/
    private int[] mTo;
    /*map���Ϲؼ�������*/
    private String[] mFrom;
/*��Ҫ�󶨵�����*/
    private List<? extends Map<String, ?>> mData; 

    private LayoutInflater mInflater;
    Context context = null;

    public ClassInfoSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) { 
        super(context, data, resource, from, to); 
        mTo = to; 
        mFrom = from; 
        mData = data;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context= context;
    } 

  public View getView(int position, View convertView, ViewGroup parent) { 
    ViewHolder holder = null; 
    /*��һ��װ�����viewʱ=null,���½�һ������inflate����һ��view*/
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.classinfo_list_item, null);
			holder = new ViewHolder();
			try { 
				/*�󶨸�view�����ؼ�*/
				holder.tv_classNumber = (TextView)convertView.findViewById(R.id.tv_classNumber);
				holder.tv_className = (TextView)convertView.findViewById(R.id.tv_className);
				holder.tv_classSpecialFieldNumber = (TextView)convertView.findViewById(R.id.tv_classSpecialFieldNumber);
			} catch(Exception ex) {}
			/*������view*/
			convertView.setTag(holder);
		}else{
			/*ֱ��ȡ����ǵ�view*/
			holder = (ViewHolder)convertView.getTag();
		}
		/*���ø����ؼ���չʾ����*/
		holder.tv_classNumber.setText("�༶��ţ�" + mData.get(position).get("classNumber").toString());
		holder.tv_className.setText("�༶���ƣ�" + mData.get(position).get("className").toString());
		holder.tv_classSpecialFieldNumber.setText("����רҵ��" + (new SpecialFieldInfoService()).GetSpecialFieldInfo(mData.get(position).get("classSpecialFieldNumber").toString()).getSpecialFieldName());
		/*�����޸ĺõ�view*/
		return convertView; 
    } 

    static class ViewHolder{ 
    	TextView tv_classNumber;
    	TextView tv_className;
    	TextView tv_classSpecialFieldNumber;
    }
} 