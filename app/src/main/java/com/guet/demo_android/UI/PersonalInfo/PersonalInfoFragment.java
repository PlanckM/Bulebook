package com.guet.demo_android.UI.PersonalInfo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.Utils.HttpUtils;
import com.guet.demo_android.Activity.MainActivity;
import com.guet.demo_android.Utils.VolleyCallback;
import com.guet.demo_android.databinding.FragmentSlideshowBinding;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PersonalInfoFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    PersonalInfoViewModel personalInfoViewModel;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private String path;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        personalInfoViewModel = new ViewModelProvider(getActivity()).get(PersonalInfoViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity a= (MainActivity) getActivity();
        a.setBottomVisible();
        personalInfoViewModel.getAvatar().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(String s){
                Glide.with(getActivity())
                        .load(personalInfoViewModel.getAvatar().getValue())
                        .into(binding.head);
            }
        });
        personalInfoViewModel.getUsername().observe(getViewLifecycleOwner(),binding.username::setText);
        personalInfoViewModel.getId().observe(getViewLifecycleOwner(),binding.userid::setText);
        personalInfoViewModel.getIntroduction().observe(getViewLifecycleOwner(),binding.introduction::setText);
        personalInfoViewModel.getSex().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(Objects.equals(personalInfoViewModel.getSex().getValue(), "1")) binding.sex.setText("男");
                else if(Objects.equals(personalInfoViewModel.getSex().getValue(), "-1")) binding.sex.setText("女");
                else binding.sex.setText("未设置");
            }
        });
        binding.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(getContext())
                        .openSystemGallery(SelectMimeType.ofImage())
                        .setSelectMaxFileSize(1)
                        .setSelectMinFileSize(1)
                        .forSystemResultActivity(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {
                                for (LocalMedia media : result) {
                                    System.out.println(media.getRealPath());
                                    try {
                                        FileInputStream stream = new FileInputStream(media.getRealPath());
                                        binding.head.setImageBitmap(BitmapFactory.decodeStream(stream));
                                        path=media.getRealPath();
                                        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                                        File file = new File(path);
                                        RequestBody bodyy = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                        builder.addFormDataPart("fileList",file.getName(),bodyy);
                                        RequestBody requestBody = builder.build();
                                        HttpUtils.post("http://47.107.52.7:88/member/photo/image/upload", requestBody, (body, gson) -> {
                                            Type jsonType=new TypeToken<HttpUtils.ResponseBody<JsonObject>>(){}.getType();
                                            HttpUtils.ResponseBody<JsonObject> responseBody= gson.fromJson(body,jsonType);
                                            if(responseBody.getCode()!=200){
                                                Looper.prepare();
                                                Toast.makeText(getActivity(), "上传头像失败", Toast.LENGTH_SHORT).show();
                                                Looper.loop();
                                                return;
                                            }
                                            String url=responseBody.getData().get("imageUrlList").getAsJsonArray().get(0).toString();
                                            url=url.substring(1,url.length()-1);
                                            personalInfoViewModel.getAvatar().postValue(url);
                                            Map<String,Object> params=new HashMap<String,Object>();
                                            params.put("avatar",url);
                                            params.put("id", personalInfoViewModel.getId().getValue());
                                            String Url="http://47.107.52.7:88/member/photo/user/update";
                                            HttpUtils.post(Url,params,true,new VolleyCallback() {
                                                @Override
                                                public void onSuccess(String body, Gson gson) {
                                                    Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                                                    HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                                                    if(responseBody.getCode()==200){
                                                        Looper.prepare();
                                                        Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                                                        Looper.loop();
                                                    }
                                                    else{
                                                        Looper.prepare();
                                                        Toast.makeText(getActivity(), "上传头像失败！", Toast.LENGTH_SHORT).show();
                                                        Looper.loop();
                                                    }
                                                }
                                            });

                                        });
                                    } catch (FileNotFoundException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            }
                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

        binding.budgetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main=(MainActivity)getActivity();
                main.navigateF(MainActivity.FragmentEditInfo);
            }
        });
        binding.settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main=(MainActivity)getActivity();
                main.navigateF(MainActivity.FragmentSetting);
            }
        });
        binding.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main=(MainActivity)getActivity();
                main.navigateF(MainActivity.FragmentLiked);
            }
        });
        binding.saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main=(MainActivity)getActivity();
                main.navigateF(MainActivity.FragmentSaved);
            }
        });
        binding.shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main=(MainActivity)getActivity();
                main.navigateF(MainActivity.FragmentShared);
            }
        });
        binding.collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity main=(MainActivity)getActivity();
                main.navigateF(MainActivity.FragmentCollected);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}