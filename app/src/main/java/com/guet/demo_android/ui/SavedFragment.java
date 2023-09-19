package com.guet.demo_android.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.guet.demo_android.Adapters.SharePhotoAdapter;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.ItemDecoration.GridSpacingItemDecoration;
import com.guet.demo_android.R;
import com.guet.demo_android.SharedViewModelFactory;
import com.guet.demo_android.Type.ShareDetail;
import com.guet.demo_android.databinding.FragmentLikedBinding;
import com.guet.demo_android.databinding.FragmentSavedBinding;

import java.util.List;

public class SavedFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private SharePhotoAdapter adapter;
    private AppContext app;
    private FragmentSavedBinding binding; // 使用ViewBinding声明绑定对象

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 使用ViewBinding来绑定布局
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerviewSave;

        // 使用StaggeredGridLayoutManager来设置交错网格布局
        int spanCount = 2; // 列数
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        // 添加GridSpacingItemDecoration来设置间隔
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space); // 间隔的像素值
        boolean includeEdge = true; // 是否包括边缘


        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingInPixels, includeEdge));

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化ViewModel
        app = (AppContext) getActivity().getApplication();
        sharedViewModel = new ViewModelProvider(this, new SharedViewModelFactory(app)).get(SharedViewModel.class);

        Log.d("onCreateView: ", app.user.getId());

        // 观察LiveData以获取图片URL列表
        sharedViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<ShareDetail>>() {
            @Override
            public void onChanged(List<ShareDetail> records) {
                // 初始化适配器并分配给recyclerView
                adapter = new SharePhotoAdapter(records, requireContext(), sharedViewModel);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 取消观察LiveData
        sharedViewModel.getRecords().removeObservers(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 释放ViewModel资源
        sharedViewModel = null;
    }
}