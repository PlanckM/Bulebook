package com.guet.demo_android.Adapters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.Activity.PictureDetailActivity;
import com.guet.demo_android.Utils.HttpUtils;
import com.guet.demo_android.R;
import com.guet.demo_android.Entity.Chat1;
import com.guet.demo_android.Entity.ChatList1;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter{

    private PictureDetailActivity mContext;
    private LayoutInflater mInflater;
    private List<Chat1>   mGroupStrings;
    public List<List<Chat1>> mData;
    private ExpandableListView expandableListView;
    private Handler handler;

    private class GroupViewHolder {
        TextView mGroupName;
        TextView mGroupCount;

    }

    private class ChildViewHolder {
        TextView mChildName;
        TextView mDetail;
    }

    public MyExpandableListViewAdapter(PictureDetailActivity context,List<Chat1>mGroupStrings,ExpandableListView expandableListView) {
        mContext = context;
        mData = new ArrayList<>();
        for(int i = 0;i<100; i++){
            mData.add(null);
        }
        this.expandableListView = expandableListView;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mGroupStrings = mGroupStrings;
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:{
                        Bundle data=msg.getData();
                        expandableListView.expandGroup(data.getInt("position"));
                        context.updateH();
                    }
                }
            }
        };
    }

    @Override
    public Chat1 getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.child_item_layout, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        holder.mChildName = convertView.findViewById(R.id.item_name);
        holder.mChildName.setText(getChild(groupPosition, childPosition)
                .getUserName());
        holder.mDetail = convertView.findViewById(R.id.item_detail);
        holder.mDetail.setText(getChild(groupPosition, childPosition)
                .getContent());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(mData.get(groupPosition)==null) return 0;
        return mData.get(groupPosition).size();
    }

    @Override
    public List<Chat1> getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        if(mGroupStrings==null) return 0;
        return mGroupStrings.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public void setmGroupStrings(List<Chat1> mGroupStrings) {
        this.mGroupStrings = mGroupStrings;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {    //convertView is the old view to reuse
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group_item_layout, null);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.mGroupName = convertView
                .findViewById(R.id.group_name);
        holder.mGroupName.setText(mGroupStrings.get(groupPosition).getUserName());
        holder.mGroupCount = convertView
                .findViewById(R.id.group_detail);
        holder.mGroupCount.setText(mGroupStrings.get(groupPosition).getContent());
        ImageView image= convertView.findViewById(R.id.expand);
        image.setOnClickListener(view -> {
            if(!expandableListView.isGroupExpanded(groupPosition)) {

                String url="http://47.107.52.7:88/member/photo/comment/second";
                HashMap<String,String> params= new HashMap<>();
                params.put("commentId",mGroupStrings.get(groupPosition).getId());
                params.put("shareId",mGroupStrings.get(groupPosition).getShareId());
                HttpUtils.get(url,params, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<ChatList1>>(){}.getType();
                    HttpUtils.ResponseBody<ChatList1> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getData()!=null){
                        List<Chat1> groupStrings = responseBody.getData().getRecords();
                        mData.set(groupPosition,groupStrings);
                        Message message=new Message();
                        message.what=1;
                        Bundle data=new Bundle();
                        data.putInt("position",groupPosition);
                        message.setData(data);
                        handler.sendMessage(message);
                        image.setImageResource(R.drawable.baseline_expand_less_24);
                    }
                    else{
                        Looper.prepare();
                        Toast.makeText(mContext, "此评论无回复！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });

            }
            else{
                expandableListView.collapseGroup(groupPosition);
                image.setImageResource(R.drawable.baseline_expand_more_24);
                mContext.updateH();
            }
        });
        convertView.setOnClickListener(view -> {

        });

        //回复
        convertView.findViewById(R.id.reply).setOnClickListener(view -> {
            mContext.editText.requestFocus();
            mContext.parentCommentId=mGroupStrings.get(groupPosition).getId();
            mContext.parentCommentUserId=mGroupStrings.get(groupPosition).getpUserId();
            mContext.editText.setHint("回复：@"+mGroupStrings.get(groupPosition).getUserName());
            mContext.imm.showSoftInput(mContext.editText, InputMethodManager.SHOW_IMPLICIT);
            mContext.ischat1=false;
            mContext.position=groupPosition;
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
class Item{
    private String name;
    private String detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}