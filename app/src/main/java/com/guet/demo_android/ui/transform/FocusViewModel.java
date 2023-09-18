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

public class FocusViewModel extends ViewModel {
    private MutableLiveData<List<ShareDetail>> recordsLiveData = new MutableLiveData<>(new ArrayList<>()); // 初始化为一个空的ArrayList
    private AppContext app;

    public FocusViewModel() {
//        this.app = app;
        fetchData();
    }

    public LiveData<List<ShareDetail>> getRecords() {
        return recordsLiveData;
    }

    private void fetchData() {
        String userId = "1696496527540883456";
        Map<String, String> params = new HashMap<>();
        String current = "1";
        String size = "20";
        params.put("current", current);
        params.put("size", size);
        params.put("userId", userId);

        HttpUtils.get("http://47.107.52.7:88/member/photo/focus", params, new VolleyCallback() {
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
