package com.guet.demo_android.ui.transform;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.Type.PicList;
import com.guet.demo_android.Type.ShareDetail;
import com.guet.demo_android.Type.User;
import com.guet.demo_android.VolleyCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FocusViewModel extends ViewModel {
    private MutableLiveData<List<ShareDetail>> recordsLiveData = new MutableLiveData<>(new ArrayList<>()); // 初始化为一个空的ArrayList

    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> content = new MutableLiveData<>();
    private AppContext appContext;
    private String url;
    private String userId;

    public FocusViewModel() {
        fetchData();
    }

    public FocusViewModel(AppContext appContext, String url) {
        this.appContext = appContext;
        this.url = url;
        fetchData();
    }

    // 添加公开方法以设置标题和内容
    public void setTitle(String newTitle) {
        title.setValue(newTitle);
    }

    public void setContent(String newContent) {
        content.setValue(newContent);
    }

    // 公开 LiveData 以供视图观察
    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getContent() {
        return content;
    }

    public LiveData<List<ShareDetail>> getRecords() {
        return recordsLiveData;
    }
    private void getUserInfo(List<ShareDetail> records, int i){
        Map<String, String> params = new HashMap<>();
        params.put("username", records.get(i).getUsername());
        Log.d("TAG", "getUserInfo: "+records.get(i).getUsername());
        HttpUtils.get("http://47.107.52.7:88/member/photo/user/getUserByName", params, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<User>>() {}.getType();
                HttpUtils.ResponseBody<User> response = gson.fromJson(body, type);
                Log.d("TAG", "onSuccess: " + response);
                String Avatar = null;
                if(response.getData() != null){
                    Avatar = response.getData().getAvatar();
                }
                if(Avatar == null){
                    Avatar = "content://com.android.providers.media.documents/document/image%3A1000000101";
                } else {
                    records.get(i).setAvatar(Avatar);
                }

            }
        });
    }
    private void fetchData() {
        userId = appContext.user.getId();
        Map<String, String> params = new HashMap<>();
        String current = "1";
        String size = "20";
        params.put("current", current);
        params.put("size", size);
        params.put("userId", userId);

        HttpUtils.get("http://47.107.52.7:88/member/photo/focus", params, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
                }.getType();
                HttpUtils.ResponseBody<PicList> response = gson.fromJson(body, type);

                if (response != null && response.getCode() == 200) {
                    PicList picList = response.getData();
                    List<ShareDetail> records=null;
                    if(picList!=null)
                        records = picList.getRecords();
                    for (int i = 0; i < records.size(); i++) {
                        getUserInfo(records, i);
                        // 更新 LiveData
                        Log.d("TAG", "onSuccess: "+records);
                        Log.d("TAG", "onSuccess: "+records.get(i).getAvatar());
                    }
                    // 更新 LiveData
                    recordsLiveData.postValue(records); // 修改为更新recordsLiveData
                } else {
                    // 处理请求失败的情况
                    // 可以发送错误消息或采取其他适当的操作
                    String msg = "ddsadf";
                    Log.d("2", msg);
                }
            }
        });
    }
}
