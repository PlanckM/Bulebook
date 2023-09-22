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

public class FindViewModel extends ViewModel {
    private final MutableLiveData<String> title = new MutableLiveData<>();
    private final MutableLiveData<String> content = new MutableLiveData<>();
    private MutableLiveData<List<ShareDetail>> recordsLiveData = new MutableLiveData<>(new ArrayList<>()); // 初始化为一个空的ArrayList

    public MutableLiveData<List<String>> getAvatarListLiveData() {
        return AvatarListLiveData;
    }

    public void setAvatarListLiveData(MutableLiveData<List<String>> avatarListLiveData) {
        AvatarListLiveData = avatarListLiveData;
    }

    //    private String URL = "http://47.107.52.7:88/member/photo/share";
    private MutableLiveData<List<String>> AvatarListLiveData = new MutableLiveData<>(new ArrayList<>());
    private String url;
    private AppContext appContext;
    private List<ShareDetail> records;
    public FindViewModel() {
        // 初始化 ViewModel 时获取数据
        fetchData(1);
    }

    public FindViewModel(AppContext appContext, String url) {
        this.appContext = appContext;
        this.url = url;
        this.AvatarListLiveData.setValue(new ArrayList<>());
        fetchData(1);
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
    private List<String> AvatarList;
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


    public void fetchData(int current) {
        Map<String, String> params = new HashMap<>();
        String userId = appContext.user.getId();

        params.put("current", current+"");
        params.put("size", "10");
        params.put("userId", userId);

        HttpUtils.get(url, params, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
                }.getType();
                HttpUtils.ResponseBody<PicList> response = gson.fromJson(body, type);

                if (response != null && response.getCode() == 200) {
                    PicList picList = response.getData();
                    Log.d("", "onSuccess: " + picList);
                    records = picList.getRecords();
                    // 避免了渲染空图片的情况
                    int size = records.size();
                    for (int i = 0; i < size; ) {
                        if (records.get(i).getImageUrlList().size() == 0) {
                            records.remove(i);
                            size--;
                        } else {
                            i++;
                        }
                    }
                    for (int i = 0; i < records.size(); i++) {
                        getUserInfo(records, i);
                        // 更新 LiveData
                    }
                    List<ShareDetail> list = (List<ShareDetail>)recordsLiveData.getValue();
                    list.addAll(records); // 修改为更新recordsLiveData
                    recordsLiveData.postValue(list);
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