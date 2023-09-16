package com.guet.demo_android.ui.transform;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.guet.demo_android.MainActivity;
import com.guet.demo_android.R;
import com.guet.demo_android.databinding.FragmentTransformBinding;
import com.guet.demo_android.databinding.ItemTransformBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment that demonstrates a responsive layout pattern where the format of the content
 * transforms depending on the size of the screen. Specifically this Fragment shows items in
 * the [RecyclerView] using LinearLayoutManager in a small screen
 * and shows items using GridLayoutManager in a large screen.
 */
public class TransformFragment extends Fragment {

    private List<View> views;
    private RadioGroup radioGroup;
    private RadioButton labelFind,labelFocus;
    private ViewPager viewPager;
    private RecyclerView findrecyclerView,focusrecyclerView;
    private FragmentTransformBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //初始化ViewModel
        //这里使用ViewModelProvider来创建或获取TransformFragment的实例。ViewModel是用来管理UI相关数据和业务逻辑的类，
        //使用ViewModel可以将数据与UI组件（如Fragment）分离，避免配置变更等情况下数据丢失，并提供更好的代码组织和维护。
        TransformViewModel transformViewModel =
                new ViewModelProvider(this).get(TransformViewModel.class);

        //绑定xml文件
        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //绑定控件

        MainActivity a= (MainActivity) getActivity();
        a.setBottomVisible();

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

        StaggeredGridLayoutManager findLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        findLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        findrecyclerView.setLayoutManager(findLayoutManager);

        View focusView = LayoutInflater.from(getActivity()).inflate(R.layout.item_home_focus,null);
        focusrecyclerView=focusView.findViewById(R.id.homepage_focus);
        focusrecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager focusLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        focusLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        focusrecyclerView.setLayoutManager(focusLayoutManager);

        views.add(findView);
        views.add(focusView);
        TransformAdapter adapter=new TransformAdapter();
        List<String> texts = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            texts.add("This is item # " + i);
        }
        adapter.submitList(texts);
        findrecyclerView.setAdapter(adapter);
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

    //TransformAdapter是TransformFragment类的一个内部类，它继承自ListAdapter，
    //是用来适配数据并在RecyclerView中显示列表项的适配器。它负责管理列表项的数据和视图，并在需要时更新显示。
    private static class TransformAdapter extends ListAdapter<String, TransformViewHolder> {

        private final List<Integer> drawables = Arrays.asList(
                R.drawable.avatar_1,
                R.drawable.avatar_2,
                R.drawable.avatar_3,
                R.drawable.avatar_4,
                R.drawable.avatar_5,
                R.drawable.avatar_6,
                R.drawable.avatar_7,
                R.drawable.avatar_8,
                R.drawable.avatar_9,
                R.drawable.avatar_10,
                R.drawable.avatar_11,
                R.drawable.avatar_12,
                R.drawable.avatar_13,
                R.drawable.avatar_14,
                R.drawable.avatar_15,
                R.drawable.avatar_16);

        //在TransformAdapter的构造函数中，我们传递了一个DiffUtil.ItemCallback对象给super()方法，、
        //它用于比较两个列表项是否相等，从而帮助ListAdapter计算差异。
        protected TransformAdapter() {
            //这个对象的两个方法areItemsTheSame()和areContentsTheSame()用于比较两个列表项是否相等。
            super(new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }


        //onCreateViewHolder()方法用于创建新的TransformViewHolder对象，即用于表示单个列表项的视图。
        @NonNull
        @Override
        public TransformViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTransformBinding binding = ItemTransformBinding.inflate(LayoutInflater.from(parent.getContext()));
            return new TransformViewHolder(binding);
        }

        //onBindViewHolder()方法用于将数据绑定到ViewHolder的视图上。
        @Override
        public void onBindViewHolder(@NonNull TransformViewHolder holder, int position) {
            holder.textView.setText(getItem(position));
            holder.imageView.setImageDrawable(
                    ResourcesCompat.getDrawable(holder.imageView.getResources(),
                            drawables.get(position),
                            null));
        }
    }

    private static class TransformViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textView;

        public TransformViewHolder(ItemTransformBinding binding) {
            super(binding.getRoot());
            imageView = binding.imageViewItemTransform;
            textView = binding.textViewItemTransform;
        }
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
}