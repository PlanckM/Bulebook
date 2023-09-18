package com.guet.demo_android.ui.transform;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.MainActivity;
import com.guet.demo_android.R;
import com.guet.demo_android.Type.PicList;

import com.guet.demo_android.Type.ShareDetail;
import com.guet.demo_android.databinding.FragmentTransformBinding;
import com.guet.demo_android.databinding.ItemTransformBinding;

import java.util.ArrayList;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransformFragment extends Fragment {

    private List<View> views;
    private RadioGroup radioGroup;
    private RadioButton labelFind,labelFocus;
    private ViewPager viewPager;
    private RecyclerView findrecyclerView,focusrecyclerView;
    private FragmentTransformBinding binding;
    private AppContext app;
    public List<ShareDetail> ShareDetail=new ArrayList<>();
    PicList picList;
    TransformViewModel transformViewModel;
    RecyclerView.Adapter<FindAdapter.myViewHodler> findAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        app=(AppContext) getActivity().getApplication();

//        initData();
        //初始化ViewModel
        //这里使用ViewModelProvider来创建或获取TransformFragment的实例。ViewModel是用来管理UI相关数据和业务逻辑的类，
        //使用ViewModel可以将数据与UI组件（如Fragment）分离，避免配置变更等情况下数据丢失，并提供更好的代码组织和维护。
        transformViewModel = new ViewModelProvider(this).get(TransformViewModel.class);
        //绑定xml文件
        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        MainActivity a= (MainActivity) getActivity();
        a.setBottomVisible();
        findAdapter=new FindAdapter(getContext(),ShareDetail);
        refreshData();
        app=(AppContext) getActivity().getApplication();
        //响应式
        //这样，控件上的数据将始终与viewModel上的值保持同步。
//        transformViewModel.getTexts().observe(getViewLifecycleOwner(), adapter::submitList);
        return root;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        views=new ArrayList<>();
        radioGroup = view.findViewById(R.id.home_radio_group);
        labelFind = view.findViewById(R.id.label_find);
        labelFocus = view.findViewById(R.id.label_focus);
        viewPager = view.findViewById(R.id.vp_style);

        View findView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_find,null);
        findrecyclerView=findView.findViewById(R.id.homepage_find);
        findrecyclerView.setHasFixedSize(true);

        GridLayoutManager findLayoutManager=new GridLayoutManager(getContext(),2);
        findrecyclerView.setLayoutManager(findLayoutManager);

        View focusView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_focus,null);
        focusrecyclerView=focusView.findViewById(R.id.homepage_focus);
        focusrecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager focusLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        focusLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        focusrecyclerView.setLayoutManager(focusLayoutManager);

        views.add(findView);
        views.add(focusView);
        findrecyclerView.setAdapter(findAdapter);
//        findrecyclerView.setAdapter(adapter);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                if(i==R.id.label_find)  viewPager.setCurrentItem(0,false);
                else if(i==R.id.label_focus) viewPager.setCurrentItem(1,false);
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
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
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    class MyStyeViewPageApapter extends PagerAdapter {
        @Override
        public int getCount() {
            return views ==null ? 0 : views.size();
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

   //多线程数据请求
    public void refreshData() {
        String userId=app.user.getId();
        Map<String,String> params=new HashMap<>();
        params.put("userId",userId);
        HttpUtils.get("http://47.107.52.7:88/member/photo/share/myself", params, (body, gson) -> {
            Type jsonType=new TypeToken<HttpUtils.ResponseBody<PicList>>(){}.getType();
            HttpUtils.ResponseBody<PicList> responseBody= gson.fromJson(body,jsonType);
            picList=responseBody.getData();
            List<ShareDetail> shareDetails=picList.getRecords();
            for (int i=0;i<shareDetails.size();i++){
                ShareDetail shareDetail=new ShareDetail();
                shareDetail.setUsername(shareDetails.get(i).getUsername());
                shareDetail.setTitle(shareDetails.get(i).getTitle());
                shareDetail.setImageUrlList(shareDetails.get(i).getImageUrlList());
                ShareDetail.add(shareDetail);
            }
        });
    }
}