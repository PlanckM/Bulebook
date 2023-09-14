package com.guet.demo_android.ui.transform;

import static android.service.controls.ControlsProviderService.TAG;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.MainActivity;
import com.guet.demo_android.R;
import com.guet.demo_android.Type.PicList;
import com.guet.demo_android.Type.User;
import com.guet.demo_android.VolleyCallback;
import com.guet.demo_android.databinding.FragmentTransformBinding;
import com.guet.demo_android.databinding.ItemTransformBinding;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TransformFragment extends Fragment {

    private FragmentTransformBinding binding;
    AppContext app;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //初始化ViewModel
        //这里使用ViewModelProvider来创建或获取TransformFragment的实例。ViewModel是用来管理UI相关数据和业务逻辑的类，
        //使用ViewModel可以将数据与UI组件（如Fragment）分离，避免配置变更等情况下数据丢失，并提供更好的代码组织和维护。
        TransformViewModel transformViewModel = new ViewModelProvider(this).get(TransformViewModel.class);

        //绑定xml文件
        binding = FragmentTransformBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //绑定控件
        RecyclerView recyclerView = binding.recyclerviewTransform;
        ListAdapter<String, TransformViewHolder> adapter = new TransformAdapter();
        GridLayoutManager glm=new GridLayoutManager(getContext(),2);

        MainActivity a= (MainActivity) getActivity();
        a.setBottomVisible();
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(adapter);
        app=(AppContext) getActivity().getApplication();
        //响应式
        //这样，控件上的数据将始终与viewModel上的值保持同步。
        transformViewModel.getTexts().observe(getViewLifecycleOwner(), adapter::submitList);
        refreshData();
        return root;
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
            imageView = binding.ivImage;
            textView = binding.tvTitle;

        }
    }
   //多线程数据请求
    private void refreshData() {
        String userId=app.user.getId();
        Map<String,String> params=new HashMap<>();
        params.put("userId",userId);
        HttpUtils.get("http://47.107.52.7:88/member/photo/share", params, (body, gson) -> {
            Type jsonType=new TypeToken<HttpUtils.ResponseBody<PicList>>(){}.getType();
            HttpUtils.ResponseBody<PicList> responseBody= gson.fromJson(body,jsonType);
            PicList picList=responseBody.getData();
        });
    }
    //结果回调
//    private okhttp3.Callback callback = new okhttp3.Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//            e.printStackTrace();
//            Log.d(TAG, "onFailure: "+e.toString());
//        }
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            if (response.isSuccessful()) {
//                final String body = response.body().string();
//                Log.d(TAG, "onResponse: 11111111111111111111111111"+body);
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        Gson gson = new Gson();
////                        Type jsonType = new TypeToken<BaseResponse<List<News>>>() {}.getType();
////                        BaseResponse<List<News>> newsListResponse = gson.fromJson(body, jsonType);
////                        for (News news:newsListResponse.getData()) {
////                            adapter.add(news);
////                        }
////                        adapter.notifyDataSetChanged();
////                    }
////                });
//            } else {
//            }
//        }
//    };
}