package com.guet.demo_android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.VolleyCallback;
import com.guet.demo_android.databinding.FragmentEditInfoBinding;
import com.guet.demo_android.ui.slideshow.SlideshowViewModel;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditInfoFragment extends Fragment {
    private FragmentEditInfoBinding binding;

    private AlertDialog alertDialog1;
    private SlideshowViewModel slideshowViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentEditInfoBinding.inflate(inflater,container,false);
        View root=binding.getRoot();
        slideshowViewModel = new ViewModelProvider(getActivity()).get(SlideshowViewModel.class);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String[] items = {"男", "女"};
        if(Objects.equals(slideshowViewModel.getSex().getValue(), "1")) binding.sexEdit.setText("男");
        else if(Objects.equals(slideshowViewModel.getSex().getValue(), "-1")) binding.sexEdit.setText("女");
        else binding.sexEdit.setText("未设置");
        binding.introductionEdit.setText(slideshowViewModel.getIntroduction().getValue());
        binding.usernameEdit.setText(slideshowViewModel.getUsername().getValue());
        binding.sexEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                alertBuilder.setTitle("选择性别");
                alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        binding.sexEdit.setText(items[i]);
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = alertBuilder.create();
                alertDialog1.show();
            }
        });
        binding.editSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> params=new HashMap<String,Object>();
                params.put("introduce",binding.introductionEdit.getText().toString());
                Integer sex=null;
                if(binding.sexEdit.getText().toString().equals("男")) sex=1;
                else if(binding.sexEdit.getText().toString().equals("女")) sex=-1;
                Log.d("", sex+"onClick: ");
                params.put("sex",sex);
                Log.d("", sex+"EditInfoFragment: ");
                params.put("username",binding.usernameEdit.getText().toString());
                params.put("id",slideshowViewModel.getId().getValue());
                String Url="http://47.107.52.7:88/member/photo/user/update";
                HttpUtils.post(Url,params,true,new VolleyCallback() {
                    @Override
                    public void onSuccess(String body, Gson gson) {
                        Type jsonType=new TypeToken<HttpUtils.ResponseBody<Object>>(){}.getType();
                        HttpUtils.ResponseBody<Object> responseBody= gson.fromJson(body,jsonType);
                        if(responseBody.getCode()==200){
                            slideshowViewModel.getUsername().postValue(params.get("username").toString());
                            slideshowViewModel.getSex().postValue(params.get("sex")+"");
                            slideshowViewModel.getIntroduction().postValue(params.get("introduce").toString());
                            Looper.prepare();
                            Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        else{
                            Looper.prepare();
                            Toast.makeText(getActivity(), "修改失败，用户名已存在！", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    }
                });
            }
        });
    }
}
