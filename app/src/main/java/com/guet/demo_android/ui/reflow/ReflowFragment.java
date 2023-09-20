package com.guet.demo_android.ui.reflow;

import android.app.Application;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.LoginActivity;
import com.guet.demo_android.MainActivity;
import com.guet.demo_android.Type.User;
import com.guet.demo_android.VolleyCallback;
import com.guet.demo_android.databinding.FragmentReflowBinding;
import com.guet.demo_android.ui.PictureDetailActivity;
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
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ReflowFragment extends Fragment {

    private FragmentReflowBinding binding;
    private int localImage = 1;
    private RelativeLayout rl1,rl2,rl3;
    private ImageView image1,image2,image3;
    private List<String> paths;
    private AppContext app;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity a= (MainActivity) getActivity();
        a.setBottomVisible();
        binding = FragmentReflowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        image1 = binding.add1;
        image2 = binding.add2;
        image3 = binding.add3;
        rl1 = binding.addRl1;
        rl2 = binding.addRl2;
        rl3 = binding.addRl3;
        app=(AppContext) getActivity().getApplication();
        paths = new ArrayList<>();
        binding.btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (localImage > 3){
//                    Toast.makeText(getActivity(),"最多允许上传三张",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                PictureSelector.create(getContext())
//                        .openSystemGallery(SelectMimeType.ofImage())
//                        .setSelectMaxFileSize(3)
//                        .setSelectMinFileSize(1)
//                        .forSystemResultActivity(new OnResultCallbackListener<LocalMedia>() {
//                            @Override
//                            public void onResult(ArrayList<LocalMedia> result) {
//                                for (LocalMedia media : result) {
//                                    System.out.println(media.getRealPath());
//                                    try {
//                                        FileInputStream stream = new FileInputStream(String.valueOf(media.getRealPath()));
//                                        if (localImage == 1){
//                                            image1.setImageBitmap(BitmapFactory.decodeStream(stream));
//                                            rl1.setVisibility(View.VISIBLE);
//                                        }else if (localImage == 2){
//                                            image2.setImageBitmap(BitmapFactory.decodeStream(stream));
//                                            rl2.setVisibility(View.VISIBLE);
//                                        }else if (localImage == 3){
//                                            image3.setImageBitmap(BitmapFactory.decodeStream(stream));
//                                            rl3.setVisibility(View.VISIBLE);
//                                        }
//                                        localImage ++;
//                                        paths.add(media.getRealPath());
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                            @Override
//                            public void onCancel() {
//
//                            }
//                        });
                Intent intent=new Intent(getActivity(),PictureDetailActivity.class);
                intent.putExtra("username","gxk");
                intent.putExtra("sharedId","5378");
                intent.putExtra("userId","1696496527540883456");
                startActivity(intent);
            }
        });

        binding.imgUpload.setOnClickListener(view1 -> {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < paths.size(); i++) {
                File file = new File(paths.get(i));

                RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart("fileList",file.getName(),body);
            }
            RequestBody requestBody = builder.build();
            HttpUtils.post("http://47.107.52.7:88/member/photo/image/upload", requestBody, (body, gson) -> {
                Type jsonType=new TypeToken<HttpUtils.ResponseBody<JsonObject>>(){}.getType();
                HttpUtils.ResponseBody<JsonObject> responseBody= gson.fromJson(body,jsonType);


                String codes2 =  responseBody.getData().get("imageCode").toString();
                String title=binding.tTitle.getText().toString();
                String content=binding.context.getText().toString();
                String userId=app.user.getId();
                String code=codes2.substring(1,codes2.length()-1);

                Map<String,Object> bodyMap=new HashMap<String,Object>();
                bodyMap.put("content",content );
                bodyMap.put("imageCode", code);
                bodyMap.put("pUserId", userId);
                bodyMap.put("title", title);
                HttpUtils.post("http://47.107.52.7:88/member/photo/share/add", bodyMap, true, (responseBody1, gson1) -> {
                    Type type=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody2= gson1.fromJson(responseBody1,type);
                    if(responseBody2.getCode()==200){
                        Looper.prepare();
                        Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    else{
                        Looper.prepare();
                        Toast.makeText(getActivity(), "上传失败！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });

            });
        });

        binding.imgSaved.setOnClickListener(view12 -> {
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < paths.size(); i++) {
                File file = new File(paths.get(i));

                RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart("fileList",file.getName(),body);
            }
            RequestBody requestBody = builder.build();
            HttpUtils.post("http://47.107.52.7:88/member/photo/image/upload", requestBody, (body, gson) -> {
                Type jsonType=new TypeToken<HttpUtils.ResponseBody<JsonObject>>(){}.getType();
                HttpUtils.ResponseBody<JsonObject> responseBody= gson.fromJson(body,jsonType);


                String codes2 =  responseBody.getData().get("imageCode").toString();
                String title=binding.tTitle.getText().toString();
                String content=binding.context.getText().toString();
                String userId=app.user.getId();
                String code=codes2.substring(1,codes2.length()-1);

                Map<String,Object> bodyMap=new HashMap<String,Object>();
                bodyMap.put("content",content );
                bodyMap.put("imageCode", code);
                bodyMap.put("pUserId", userId);
                bodyMap.put("title", title);
                HttpUtils.post("http://47.107.52.7:88/member/photo/share/save", bodyMap, true, (responseBody1, gson1) -> {
                    Type type=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                    HttpUtils.ResponseBody<Object> responseBody2= gson1.fromJson(responseBody1,type);
                    if(responseBody2.getCode()==200){
                        Looper.prepare();
                        Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    else{
                        Looper.prepare();
                        Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                });

            });
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}