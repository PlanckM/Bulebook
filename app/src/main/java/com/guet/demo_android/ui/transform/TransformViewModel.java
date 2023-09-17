package com.guet.demo_android.ui.transform;

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
    private MutableLiveData<List<String>> imageUrlsLiveData = new MutableLiveData<>();
    private String URL = "http://47.107.52.7:88/member/photo/share";
    private String userId = "1702291281314713600";
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
    public LiveData<List<String>> getImageUrls() {
        return imageUrlsLiveData;
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
                    List<ShareDetail> records = picList.getRecords();
                    List<String> imageUrls = new ArrayList<>();

                    for (ShareDetail record : records) {
                        List<String> imageUrlList = record.getImageUrlList();
                        imageUrls.addAll(imageUrlList);
                    }
                    // 更新 LiveData
                    imageUrlsLiveData.postValue(imageUrls);
                } else {
                    // 处理请求失败的情
                }
            }
        });
    }

}