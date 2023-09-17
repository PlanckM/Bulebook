package com.guet.demo_android.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.guet.demo_android.ItemDecoration.GridSpacingItemDecoration;
import com.guet.demo_android.R;
import com.guet.demo_android.databinding.FragmentSharedBinding;

import java.util.ArrayList;
import java.util.List;

public class SharedFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private SharePhotoAdapter adapter;

    private FragmentSharedBinding binding; // 使用ViewBinding声明绑定对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 初始化ViewModel
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // 使用ViewBinding来绑定布局
        binding = FragmentSharedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerviewShared;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 初始化适配器并分配给recyclerView
        adapter = new SharePhotoAdapter(new ArrayList<>(), requireContext(), sharedViewModel);
        // 使用GridLayoutManager来设置网格布局
        int spanCount = 2; // 列数
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);

        // 添加GridSpacingItemDecoration来设置间隔
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space); // 间隔的像素值
        boolean includeEdge = true; // 是否包括边缘
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, includeEdge));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消观察LiveData
        sharedViewModel.getImageUrls().removeObservers(this);
        // 释放ViewModel资源
        sharedViewModel = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 观察LiveData以获取图片URL列表
        sharedViewModel.getImageUrls().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> imageUrls) {
                // 更新适配器的数据集
                adapter.setImageUrls(imageUrls);
            }
        });
    }
}
