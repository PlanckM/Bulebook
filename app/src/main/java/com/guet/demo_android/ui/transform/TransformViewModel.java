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
import com.guet.demo_android.VolleyCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformViewModel extends ViewModel {
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> content = new MutableLiveData<>();
    private MutableLiveData<List<ShareDetail>> recordsLiveData = new MutableLiveData<>(new ArrayList<>()); // 初始化为一个空的ArrayList
    private String URL = "http://47.107.52.7:88/member/photo/share";
    private String userId = "1696496527540883456";
    private Integer current;
    private Integer size;
    private AppContext app;

    public TransformViewModel() {
        // 初始化 ViewModel 时获取数据
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
    public LiveData<List<ShareDetail>> getRecords() { // 修改为LiveData<List<ShareDetail>>
        return recordsLiveData;
    }

    private void fetchData() {
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);
        HttpUtils.get(URL, params, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<PicList>>(){}.getType();
                HttpUtils.ResponseBody<PicList> response = gson.fromJson(body, type);

                if (response != null && response.getCode() == 200) {
                    PicList picList = response.getData();
                    Log.d("","onSuccess: "+ picList);
                    List<ShareDetail> records = picList.getRecords();
                    Log.d("", "onSuccess: " + records);
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