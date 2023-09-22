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

import com.guet.demo_android.Adapters.FocusPhotoAdapter;
import com.guet.demo_android.Adapters.SharePhotoAdapter;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.ItemDecoration.GridSpacingItemDecoration;
import com.guet.demo_android.ItemDecoration.VerticalSpaceItemDecoration;
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

    private FocusPhotoAdapter focusAdapter;

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
    private int current_focus=2;
    private int current_find=2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        app = (AppContext) getActivity().getApplication();
        //绑定xml文件
        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space); // 间隔的像素值
        boolean includeEdge = false; // 是否包括边缘
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
        focusrecyclerView.setHasFixedSize(false);

//        StaggeredGridLayoutManager focusLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        focusLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//        focusrecyclerView.setLayoutManager(focusLayoutManager);
//        focusrecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, includeEdge));
//
        LinearLayoutManager focusLayoutManager = new LinearLayoutManager(getContext());
        focusrecyclerView.setLayoutManager(focusLayoutManager);
        int verticalSpaceHeight = getResources().getDimensionPixelSize(R.dimen.space1); // 设置你想要的上下边距的像素值
        focusrecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(verticalSpaceHeight));

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

        findAdapter = new SharePhotoAdapter(null, requireContext());
        focusAdapter = new FocusPhotoAdapter(null, requireContext());
        findrecyclerView.setAdapter(findAdapter);
        focusrecyclerView.setAdapter(focusAdapter);

        findrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = findAdapter.getItemCount();
                StaggeredGridLayoutManager layoutManager=(StaggeredGridLayoutManager) findrecyclerView.getLayoutManager();
                assert layoutManager != null;
                int [] lastItems = new int[layoutManager.getSpanCount()];
                layoutManager.findLastVisibleItemPositions(lastItems);
                int lastVisibleItemPosition = findMax(lastItems);

                // 检查是否已经滚动到底部
                if (lastVisibleItemPosition>=totalItemCount-1) {
                    // 在这里触发你的事件，例如加载更多数据
                    // 这里只是一个示例，你可以根据你的需求进行操作
                    findViewModel.fetchData(current_find);
                    current_find++;
                }
            }
        });

        focusrecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager=(LinearLayoutManager) focusrecyclerView.getLayoutManager();
                assert layoutManager != null;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                // 检查是否已经滚动到底部
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    // 在这里触发你的事件，例如加载更多数据
                    // 这里只是一个示例，你可以根据你的需求进行操作
                    focusViewModel.fetchData(current_focus);
                    current_focus++;
                }
            }
        });

        findViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<ShareDetail>>() {
            @Override
            public void onChanged(List<ShareDetail> records) {
                // 初始化适配器并分配给recyclerView
                findAdapter.setRecords(records);
                // 设置RecyclerView的item点击事件监听器
                findAdapter.setOnImageClickListener(position -> {
                    // 处理图片 ImageView 的点击事件，position 是被点击的 item 的位置
                    ShareDetail clickedItem = records.get(position);
                    // 在这里执行相应的操作，例如查看大图或者其他操作
                    Intent intent = new Intent(getContext(), PictureDetailActivity.class);
                    intent.putExtra("userId", clickedItem.getpUserId());
                    intent.putExtra("username",clickedItem.getUsername());
                    intent.putExtra("shareId", clickedItem.getId());
                    startActivity(intent);
                });
            }
        });

        focusViewModel.getRecords().observe(getViewLifecycleOwner(), new Observer<List<ShareDetail>>() {
            @Override
            public void onChanged(List<ShareDetail> records) {
                // 初始化适配器并分配给recyclerView

                focusAdapter.setRecords(records);
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
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        focusViewModel.getRecords().removeObservers(this);
        findViewModel.getRecords().removeObservers(this);
    }


    private int findMax(int[] positions) {
        int max = positions[0];
        for (int position : positions) {
            if (position > max) {
                max = position;
            }
        }
        return max;
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
}