package com.guet.demo_android.ui.transform;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.guet.demo_android.Adapters.SharePhotoAdapter;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.ItemDecoration.GridSpacingItemDecoration;
import com.guet.demo_android.MainActivity;
import com.guet.demo_android.R;

import com.guet.demo_android.Type.ShareDetail;

import com.guet.demo_android.ViewModelFactory.SharedViewModelFactory;
import com.guet.demo_android.databinding.FragmentTransformBinding;
import com.guet.demo_android.ui.PictureDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class TransformFragment extends Fragment {

    //通用适配器
    private SharePhotoAdapter findAdapter;

    private SharePhotoAdapter focusAdapter;

    private List<View> views;
    private RadioGroup radioGroup;
    private RadioButton labelFind, labelFocus;
    private ViewPager viewPager;
    //流式布局
    private RecyclerView findrecyclerView, focusrecyclerView;
    private FragmentTransformBinding binding;

    private final String focusURL = "http://47.107.52.7:88/member/photo/focus";
    private final String findURL = "http://47.107.52.7:88/member/photo/share";
    private AppContext app;

    private FindViewModel findViewModel;
    private FocusViewModel focusViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        app = (AppContext) getActivity().getApplication();
        //绑定xml文件
        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space); // 间隔的像素值
        boolean includeEdge = true; // 是否包括边缘
        //发现
        View findView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_find, null);
        findrecyclerView = findView.findViewById(R.id.homepage_find);
        findrecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager findLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        findrecyclerView.setLayoutManager(findLayoutManager);
        // 添加GridSpacingItemDecoration来设置间隔
        findrecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, includeEdge));

        //关注
        View focusView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_focus, null);
        focusrecyclerView = focusView.findViewById(R.id.homepage_focus);
        focusrecyclerView.setHasFixedSize(true);

//        StaggeredGridLayoutManager focusLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        focusLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//        focusrecyclerView.setLayoutManager(focusLayoutManager);
//        focusrecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, includeEdge));
//
        LinearLayoutManager focusLayoutManager = new LinearLayoutManager(getContext());
        focusrecyclerView.setLayoutManager(focusLayoutManager);

        views = new ArrayList<>();
        radioGroup = binding.homeRadioGroup;
        labelFind = binding.labelFind;
        labelFocus = binding.labelFocus;
        viewPager = binding.vpStyle;

        views.add(findView);
        views.add(focusView);

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.label_find) viewPager.setCurrentItem(0, false);
            else if (i == R.id.label_focus) viewPager.setCurrentItem(1, false);
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        labelFind.setChecked(true);
                        break;
                    case 1:
                        labelFocus.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(new MyStyeViewPageApapter());

        MainActivity a = (MainActivity) getActivity();
        a.setBottomVisible();
        return root;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app = (AppContext) getActivity().getApplication();

        // 初始化viewModel
//        findViewModel = new ViewModelProvider(this).get(FindViewModel.class);
//        focusViewModel = new ViewModelProvider(this).get(FocusViewModel.class);
        findViewModel = new ViewModelProvider(this, new SharedViewModelFactory(app, findURL)).get(FindViewModel.class);
        focusViewModel = new ViewModelProvider(this, new SharedViewModelFactory(app, focusURL)).get(FocusViewModel.class);

        findViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<ShareDetail>>() {
            @Override
            public void onChanged(List<ShareDetail> records) {
                // 初始化适配器并分配给recyclerView
                findAdapter = new SharePhotoAdapter(records, requireContext());

                // 设置RecyclerView的item点击事件监听器
                findAdapter.setOnImageClickListener(new SharePhotoAdapter.OnImageClickListener() {
                    @Override
                    public void onImageClick(int position) {
                        // 处理图片 ImageView 的点击事件，position 是被点击的 item 的位置
                        ShareDetail clickedItem = records.get(position);
                        // 在这里执行相应的操作，例如查看大图或者其他操作
                        Intent intent = new Intent(getContext(), PictureDetailActivity.class);
                        intent.putExtra("userId", clickedItem.getpUserId());
                        intent.putExtra("username",clickedItem.getUsername());
                        intent.putExtra("shareId", clickedItem.getId());
                        startActivity(intent);
                    }
                });

                findAdapter.setOnIsLikeClickListener(new SharePhotoAdapter.OnIsLikeClickListener() {
                    @Override
                    public void onIsLikeClick(int position) {
                        // 处理is_like ImageView 的点击事件，position 是被点击的 item 的位置
                        ShareDetail clickedItem = records.get(position);
                        // 在这里执行相应的操作，例如切换点赞状态等
                        boolean isLiked = clickedItem.getHasLike();// 获取当前点赞状态
                        // 根据点赞状态执行操作，例如发送点赞请求或者取消点赞请求
                        if (isLiked) {
                            // 已点赞，执行取消点赞操作

                        } else {
                            // 未点赞，执行点赞操作
                        }
                    }
                });

                findrecyclerView.setAdapter(findAdapter);
            }
        });

        focusViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<ShareDetail>>() {
            @Override
            public void onChanged(List<ShareDetail> records) {
                // 初始化适配器并分配给recyclerView
                focusAdapter = new SharePhotoAdapter(records, requireContext());

                // 设置RecyclerView的ImageItem点击事件监听器
                focusAdapter.setOnImageClickListener(position -> {
                    // 处理图片 ImageView 的点击事件，position 是被点击的 item 的位置
                    ShareDetail clickedItem = records.get(position);
                    // 在这里执行相应的操作，例如查看大图或者其他操作
                    Intent intent = new Intent(getContext(), PictureDetailActivity.class);
                    intent.putExtra("userId", clickedItem.getpUserId());
                    intent.putExtra("username",clickedItem.getUsername());
                    intent.putExtra("shareId", clickedItem.getId());
                    startActivity(intent);
                });

                focusAdapter.setOnIsLikeClickListener(new SharePhotoAdapter.OnIsLikeClickListener() {
                    @Override
                    public void onIsLikeClick(int position) {
                        // 处理is_like ImageView 的点击事件，position 是被点击的 item 的位置
                        ShareDetail clickedItem = records.get(position);
                        // 在这里执行相应的操作，例如切换点赞状态等
                    }
                });

                focusrecyclerView.setAdapter(focusAdapter);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        focusViewModel.getRecords().removeObservers(this);
        findViewModel.getRecords().removeObservers(this);
    }


    class MyStyeViewPageApapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views == null ? 0 : views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View itemView = views.get(position);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

//    //请求发现列表的数据
//    public void refreshFindData() {
//        String userId = app.user.getId();
//        Map<String, String> params = new HashMap<>();
//        params.put("userId", userId);
//        HttpUtils.get("http://47.107.52.7:88/member/photo/share/myself", params, (body, gson) -> {
//            Type jsonType = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
//            }.getType();
//            HttpUtils.ResponseBody<PicList> responseBody = gson.fromJson(body, jsonType);
//            picList = responseBody.getData();
//            List<ShareDetail> shareDetails = picList.getRecords();
//            for (int i = 0; i < shareDetails.size(); i++) {
//                ShareDetail shareDetail = new ShareDetail();
//                shareDetail.setUsername(shareDetails.get(i).getUsername());
//
//                shareDetail.setTitle(shareDetails.get(i).getTitle());
//                shareDetail.setImageUrlList(shareDetails.get(i).getImageUrlList());
//                ShareDetail.add(shareDetail);
//            }
//        });
//    }
//
//    //请求发现列表的数据请求关注列表的数据
////    public void refreshFocusData() {
////        String userId = app.user.getId();
////        Map<String, String> params = new HashMap<>();
////        params.put("userId", userId);
////        HttpUtils.get("http://47.107.52.7:88/member/photo/focus", params, (body, gson) -> {
////            Type jsonType = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
////            }.getType();
////            HttpUtils.ResponseBody<PicList> responseBody = gson.fromJson(body, jsonType);
////            picList = responseBody.getData();
////            List<ShareDetail> shareDetails = picList.getRecords();
////            Log.d(TAG, "refreshFocusData: " + shareDetails.toString());
////            for (int i = 0; i < shareDetails.size(); i++) {
////                ShareDetail shareDetail = new ShareDetail();
////                shareDetail.setUsername(shareDetails.get(i).getUsername());
////                shareDetail.setTitle(shareDetails.get(i).getTitle());
////                shareDetail.setImageUrlList(shareDetails.get(i).getImageUrlList());
////                FocusShareDetail.add(shareDetail);
////            }
////        });
////    }
}