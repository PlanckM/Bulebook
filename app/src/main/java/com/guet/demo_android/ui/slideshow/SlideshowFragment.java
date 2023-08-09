package com.guet.demo_android.ui.slideshow;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.MainActivity;
import com.guet.demo_android.VolleyCallback;
import com.guet.demo_android.databinding.FragmentSlideshowBinding;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    SlideshowViewModel slideshowViewModel;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = new ViewModelProvider(getActivity()).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        slideshowViewModel.getAvatar().observe(getViewLifecycleOwner(),new Observer<String>(){
            @Override
            public void onChanged(String s){
                    if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES)
                            != PackageManager.PERMISSION_GRANTED){
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_MEDIA_IMAGES)) {

                        }else{
                            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                                    REQUEST_CODE_STORAGE_PERMISSION);
                        }
                    } else {
                        // 权限已经授予，加载图片
                        if(slideshowViewModel.getAvatar().getValue()!=null&&slideshowViewModel.getAvatar().getValue()!="")
                        {
                            Boolean weHaveDurablePermission=false;
                            List<UriPermission> permissions = getActivity().getContentResolver().getPersistedUriPermissions();
                            for (UriPermission perm : permissions) {
                                if (perm.getUri().equals(Uri.parse(slideshowViewModel.getAvatar().getValue()))) {
                                    weHaveDurablePermission=true;
                                }
                            }
                            if(weHaveDurablePermission)binding.head.setImageURI(Uri.parse(slideshowViewModel.getAvatar().getValue()));
                        }
                    }
            }
        });
        slideshowViewModel.getUsername().observe(getViewLifecycleOwner(),binding.username::setText);
        slideshowViewModel.getId().observe(getViewLifecycleOwner(),binding.userid::setText);
        slideshowViewModel.getIntroduction().observe(getViewLifecycleOwner(),binding.introduction::setText);
        slideshowViewModel.getSex().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(Objects.equals(slideshowViewModel.getSex().getValue(), "1")) binding.sex.setText("男");
                else if(Objects.equals(slideshowViewModel.getSex().getValue(), "-1")) binding.sex.setText("女");
                else binding.sex.setText("未设置");
            }
        });
        binding.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,555);
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
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                Uri uri = data.getData();
                Map<String,Object> params=new HashMap<String,Object>();
                params.put("avatar",uri.toString());
                params.put("id",slideshowViewModel.getId().getValue());
                String Url="http://47.107.52.7:88/member/photo/user/update";
                HttpUtils.post(Url,params,true,new VolleyCallback() {
                    @Override
                    public void onSuccess(String body, Gson gson) {
                    }
                });
                getActivity().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                slideshowViewModel.getAvatar().setValue(uri.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d("666", "失败");
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户授权了权限，在这里处理逻辑，如加载图片
            } else {
                // 用户拒绝了权限，可以在这里做一些提示或处理
                Toast.makeText(getActivity(), "头像无法正常显示!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}