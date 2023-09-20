package com.guet.demo_android.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.R;
import com.guet.demo_android.Type.Chat1;
import com.guet.demo_android.Type.ChatList1;
import com.guet.demo_android.Type.PictureDetail;
import com.guet.demo_android.databinding.ActivityPictureDetailBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//详情，点赞，收藏，关注
public class PictureDetailActivity extends AppCompatActivity {

    private String userId;
    private String shareId;
    private String username;
    private ActivityPictureDetailBinding binding;
    private TextView usernameView;
    private ImageView focusButton;
    private Boolean isfocus=false;
    private ImageView likesButton;
    private Boolean islikes=false;
    private ImageButton collectButton;
    private Boolean iscollect=false;
    private ImageButton chatButton;
    private TextView pictureTitle;
    private PictureDetail pictureDetail;
    private TextView likeNum;
    private TextView collectNum;
    private Handler mHandler;
    private List<String> imageUrls;
    private TextView content;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    private MyExpandableListViewAdapter myExpandableListViewAdapter;
    private ExpandableListView expandableListView;
    private TextView a;
    public EditText editText;
    List<Chat1> groupStrings;
    public InputMethodManager imm;
    public String parentCommentId;
    public String parentCommentUserId;
    public Boolean ischat1;
    public int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityPictureDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ischat1=true;
        usernameView=binding.username;
        focusButton=binding.focus;
        likesButton=binding.likes;
        collectButton=binding.collect;
        chatButton=binding.chat;
        pictureTitle=binding.pictureTitle;
        likeNum=binding.likeNum;
        collectNum=binding.collectNum;
        content=binding.content;
        editText=binding.edit;
        viewPager=binding.loopviewpager;
        expandableListView=binding.wListview;
        groupStrings=new ArrayList<>();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        myExpandableListViewAdapter=new MyExpandableListViewAdapter(this,groupStrings,expandableListView);
        myExpandableListViewAdapter.notifyDataSetChanged();
        expandableListView.setAdapter(myExpandableListViewAdapter);

        a=binding.a;
        Intent intent = getIntent();
        AppContext app=(AppContext)getApplication();
        userId = app.user.getId();
        shareId = intent.getStringExtra("shareId");
        username=intent.getStringExtra("username");

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                    {
                        if(pictureDetail.getHasLike()){
                            likesButton.setImageResource(R.drawable.baseline_favorite_24);
                            islikes=true;
                        }
                        if(pictureDetail.getHasCollect()){
                            collectButton.setImageResource(R.drawable.baseline_star_24);
                            iscollect=true;
                        }
                        if(pictureDetail.getHasFocus()){
                            focusButton.setImageResource(R.drawable.baseline_remove_24);
                            isfocus=true;
                        }
                        if(pictureDetail.getLikeNum()!=null) likeNum.setText(pictureDetail.getLikeNum()+"");
                        if(pictureDetail.getCollectNum()!=null) collectNum.setText(pictureDetail.getCollectNum()+"");
                        else collectNum.setText("0");
                    }
                    break;
                    case 2:
                    case 4:
                        iniData();
                    break;
                    case 3:{
                        likesButton.setImageResource(R.drawable.baseline_favorite_border_24);
                        islikes=false;
                        iniData();
                    }
                    break;
                    case 5:{
                        collectButton.setImageResource(R.drawable.baseline_star_border_24);
                        iscollect=false;
                        iniData();
                    }
                    break;
                    case 6:{
                        focusButton.setImageResource(R.drawable.baseline_remove_24);
                        isfocus=true;
                    }
                    break;
                    case 7:{
                        focusButton.setImageResource(R.drawable.baseline_add_24);
                        isfocus=false;
                    }
                    break;
                    case 8:
                    {
                        pagerAdapter=new PictureDetailAdapter(imageUrls);
                        viewPager.setAdapter(pagerAdapter);
                        binding.b.setText(pagerAdapter.getCount()+"");
                        pictureTitle.setText(pictureDetail.getTitle());
                        if(pictureDetail.getHasLike()){
                            likesButton.setImageResource(R.drawable.baseline_favorite_24);
                            islikes=true;
                        }
                        if(pictureDetail.getHasCollect()){
                            collectButton.setImageResource(R.drawable.baseline_star_24);
                            iscollect=true;
                        }
                        if(pictureDetail.getHasFocus()){
                            focusButton.setImageResource(R.drawable.baseline_remove_24);
                            isfocus=true;
                        }
                        if(pictureDetail.getLikeNum()!=null) likeNum.setText(pictureDetail.getLikeNum()+"");
                        if(pictureDetail.getCollectNum()!=null) collectNum.setText(pictureDetail.getCollectNum()+"");
                        else collectNum.setText("0");
                        content.setText(pictureDetail.getContent());
                    }
                    break;
                    case 9:{
                        binding.chatnum.setText("共"+groupStrings.size()+"条");
                        myExpandableListViewAdapter.setmGroupStrings(groupStrings);
                        myExpandableListViewAdapter.notifyDataSetChanged();
                        updateH();
                    }
                    break;
                    case 10:{
                        editText.setText("");
                        iniChat();
                    }
                    break;
                    case 11:{
                        if(expandableListView.isGroupExpanded(position)){
                            Chat1 chat=new Chat1();
                            chat.setContent(editText.getText().toString());
                            chat.setUserName(username);
                            myExpandableListViewAdapter.mData.get(position).add(chat);
                            myExpandableListViewAdapter.notifyDataSetChanged();
                        }
                        editText.setText("");
                    }
                    default:
                        break;
                }
            }
        };
        //轮播图适配器
        iniAllData();
        iniChat();
        usernameView.setText(username);
        focusButton.setOnClickListener(view -> {
            if(!isfocus){
                String url="http://47.107.52.7:88/member/photo/focus?";
                HashMap<String,Object> params= new HashMap<>();
                params.put("focusUserId",pictureDetail.getpUserId());
                params.put("userId",userId);
                HttpUtils.post(url, params, false, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getCode()==200){
                        Message msg = new Message();
                        msg.what = 6;
                        mHandler.sendMessage(msg);
                    }
                });
            }
            else{
                String url="http://47.107.52.7:88/member/photo/focus/cancel?";
                HashMap<String,Object> params= new HashMap<>();
                params.put("focusUserId",pictureDetail.getpUserId());
                params.put("userId",userId);
                HttpUtils.post(url, params, false, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getCode()==200){
                        Message msg = new Message();
                        msg.what = 7;
                        mHandler.sendMessage(msg);
                    }
                });
            }
        });
        likesButton.setOnClickListener(view -> {
            if(!islikes){
                String url="http://47.107.52.7:88/member/photo/like?";
                HashMap<String,Object> params= new HashMap<>();
                params.put("shareId",shareId);
                params.put("userId",userId);
                HttpUtils.post(url, params, false, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getCode()==200){
                        Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }
                });
            }
            else{
                String url="http://47.107.52.7:88/member/photo/like/cancel?";
                HashMap<String,Object> params= new HashMap<>();
                params.put("likeId",pictureDetail.getLikeId());
                HttpUtils.post(url, params, false, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getCode()==200){
                        Message msg = new Message();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    }
                });
            }
        });
        collectButton.setOnClickListener(view -> {
            if(!iscollect){
                String url="http://47.107.52.7:88/member/photo/collect?";
                HashMap<String,Object> params= new HashMap<>();
                params.put("shareId",shareId);
                params.put("userId",userId);
                HttpUtils.post(url, params, false, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getCode()==200){
                        Message msg = new Message();
                        msg.what = 4;
                        mHandler.sendMessage(msg);
                    }
                });
            }
            else{
                String url="http://47.107.52.7:88/member/photo/collect/cancel?";
                HashMap<String,Object> params= new HashMap<>();
                params.put("collectId",pictureDetail.getCollectId());
                HttpUtils.post(url, params, false, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getCode()==200){
                        Message msg = new Message();
                        msg.what = 5;
                        mHandler.sendMessage(msg);
                    }
                });
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                a.setText(position+1+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        chatButton.setOnClickListener(view->{
            editText.requestFocus();
            ischat1=true;
            editText.setHint("请友善评论");
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        });
        binding.sendBtn.setOnClickListener(view -> {
            if(editText.getText()==null||editText.getText().equals("")){
                Toast.makeText(this, "请输入内容!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(ischat1){
                String url="http://47.107.52.7:88/member/photo/comment/first";
                HashMap<String,Object> params= new HashMap<>();
                params.put("content",editText.getText().toString());
                params.put("shareId",shareId);
                params.put("userId",userId);
                params.put("userName",username);
                HttpUtils.post(url, params, true, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getCode()==200) {
                        Message msg = new Message();
                        msg.what = 10;
                        mHandler.sendMessage(msg);
                        Looper.prepare();
                        Toast.makeText(this, "评论成功!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    else{
                        Looper.prepare();
                        Toast.makeText(this, "评论失败，未知错误!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
            }
            else {
                String url="http://47.107.52.7:88/member/photo/comment/second";
                HashMap<String,Object> params= new HashMap<>();
                params.put("content",editText.getText().toString());
                Log.d("", "onCreate: "+parentCommentId);
                params.put("parentCommentId",parentCommentId);
                params.put("parentCommentUserId",parentCommentUserId);
                params.put("replyCommentId",parentCommentId);
                params.put("replyCommentUserId",parentCommentUserId);
                params.put("shareId",shareId);
                params.put("userId",userId);
                params.put("userName",username);
                HttpUtils.post(url, params, true, (body, gson) -> {
                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                    if(responseBody.getCode()==200) {
                        Message msg = new Message();
                        msg.what = 11;
                        mHandler.sendMessage(msg);
                        Looper.prepare();
                        Toast.makeText(this, "评论成功!", Toast.LENGTH_SHORT).show();
                        Looper.loop();

                    }
                    else {
                        Looper.prepare();
                        Toast.makeText(this, "评论失败!"+responseBody.getMsg(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });
            }
        });
    }

    private void iniData(){

        String url="http://47.107.52.7:88/member/photo/share/detail";
        HashMap<String,String> params= new HashMap<String,String>();
        params.put("shareId",shareId);
        params.put("userId",userId);
        HttpUtils.get(url,params, (body, gson) -> {
            Type jsonType=new TypeToken<HttpUtils.ResponseBody<PictureDetail>>(){}.getType();
            HttpUtils.ResponseBody<PictureDetail> responseBody= gson.fromJson(body,jsonType);
            pictureDetail=responseBody.getData();
            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessage(msg);
        });
    }
    private void iniAllData(){
        String url="http://47.107.52.7:88/member/photo/share/detail";
        HashMap<String,String> params= new HashMap<>();
        params.put("shareId",shareId);
        params.put("userId",userId);
        HttpUtils.get(url,params, (body, gson) -> {
            Type jsonType=new TypeToken<HttpUtils.ResponseBody<PictureDetail>>(){}.getType();
            HttpUtils.ResponseBody<PictureDetail> responseBody= gson.fromJson(body,jsonType);
            pictureDetail=responseBody.getData();
            imageUrls=pictureDetail.getImageUrlList();
            Message msg = new Message();
            msg.what = 8;
            mHandler.sendMessage(msg);
        });
    }
    private void iniChat(){
        String url="http://47.107.52.7:88/member/photo/comment/first";
        HashMap<String,String> params= new HashMap<>();
        params.put("shareId",shareId);
        params.put("size","50");
        HttpUtils.get(url,params, (body, gson) -> {
            Type jsonType=new TypeToken<HttpUtils.ResponseBody<ChatList1>>(){}.getType();
            HttpUtils.ResponseBody<ChatList1> responseBody= gson.fromJson(body,jsonType);
            groupStrings=responseBody.getData().getRecords();
            Message msg = new Message();
            msg.what = 9;
            mHandler.sendMessage(msg);
        });
    }
    public void updateH(){
        int totalHeight = 0;
        for (int i = 0; i < myExpandableListViewAdapter.getGroupCount(); i++) {
            View groupView = myExpandableListViewAdapter.getGroupView(i, false, null, expandableListView);
            groupView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += groupView.getMeasuredHeight();

            for (int j = 0; j < myExpandableListViewAdapter.getChildrenCount(i); j++) {
                if(expandableListView.isGroupExpanded(i)){
                View childView = myExpandableListViewAdapter.getChildView(i, j, false, null, expandableListView);
                childView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalHeight += childView.getMeasuredHeight();
                }
            }
        }

        ViewGroup.LayoutParams layoutParams = expandableListView.getLayoutParams();
        layoutParams.height = totalHeight+240 + (expandableListView.getDividerHeight() * (myExpandableListViewAdapter.getGroupCount() - 1));
        expandableListView.setLayoutParams(layoutParams);
    }
}