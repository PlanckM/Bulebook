package com.guet.demo_android.Adapters;


import static android.app.PendingIntent.getActivity;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.AppContext;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.R;
import com.guet.demo_android.Type.PicList;
import com.guet.demo_android.Type.ShareDetail;
import com.guet.demo_android.VolleyCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SharePhotoAdapter extends RecyclerView.Adapter<SharePhotoAdapter.ImageViewHolder> {
    private List<ShareDetail> records;
    private Context context;
    private String userId;
    public SharePhotoAdapter(List<ShareDetail> records, Context context) {
        this.context = context;
        this.records = records;
    }

    public SharePhotoAdapter(List<ShareDetail> records, Context context, String userId) {
        this.context = context;
        this.records = records;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 布局资源item_image文件加载为View对象
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (records.get(position).getImageUrlList().size() == 0) {
            // 处理数据为空的情况，例如显示默认数据或者隐藏视图等操作
        } else {
            ShareDetail record = records.get(position);
            String imageUrl = record.getImageUrlList().get(0); // 获取第一个图片链接
            // 获取标题和内容，并设置到对应的 TextView 中
            String title = record.getTitle();
            String username = record.getUsername(); // 用户名字段是 username
            String likeNum = String.valueOf(record.getLikeNum());
            final boolean[] islike = {record.getHasLike()};

            holder.titleTextView.setText(title);
            holder.contentTextView.setText(username);
            holder.likeNumTextView.setText(likeNum);

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.imageView);
            //初始化图标的状态
            if(islike[0]){
                holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_20); // 已点赞状态
            }else{
                holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_border_24); // 未点赞状态
            }
            //点击图片跳转页面
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onImageClickListener != null) {
                        onImageClickListener.onImageClick(position);
                    }
                }
            });
            //每一个图标绑定点击事件
            holder.isLikeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentLikeNum = record.getLikeNum();
                    if(islike[0]){
                        ShareDetail record = records.get(position);
                        if (currentLikeNum > 0) {
                            // 如果当前点赞数量大于0，则减一
                            currentLikeNum--;
                            record.setLikeNum(currentLikeNum);
                            // 更新点赞数量的显示
                            holder.likeNumTextView.setText(String.valueOf(currentLikeNum));
                            islike[0] =false;
                            record.setHasLike(false);
                        }
                        holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_border_24); // 未点赞状态
                        sendCancelLikeRequest(record.getLikeId(),"http://47.107.52.7:88/member/photo/like/cancel?");
                    }else{
                        currentLikeNum++;
                        record.setLikeNum(currentLikeNum);
                        // 更新点赞数量的显示
                        holder.likeNumTextView.setText(String.valueOf(currentLikeNum));
                        holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_20); // 已点赞状态
                        islike[0] =true;
                        record.setHasLike(true);
                        sendLikeRequest(record.getId(),"http://47.107.52.7:88/member/photo/like?");

                    }
                    if (onIsLikeClickListener != null) {
                        onIsLikeClickListener.onIsLikeClick(position);
                    }
                }
            });
        }
    }
   //取消点赞
    private void sendCancelLikeRequest(String id,String url) {
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("likeId",id);
        HttpUtils.post(url,  params,false, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {

            }
        });
    }
    //点赞
    private void sendLikeRequest(String shareId,String url){
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("shareId",shareId);
        params.put("userId",userId);
        HttpUtils.post(url,  params,false, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView contentTextView;
        TextView likeNumTextView;
        ImageView isLikeImageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            titleTextView = itemView.findViewById(R.id.tv_title_myshare);
            contentTextView = itemView.findViewById(R.id.tv_username);
            likeNumTextView = itemView.findViewById(R.id.like_num);
            isLikeImageView = itemView.findViewById(R.id.is_like);
        }
    }

    public void setRecords(List<ShareDetail> newRecords) {
        // 更新适配器的数据集
        this.records = newRecords;
        notifyDataSetChanged(); // 刷新 RecyclerView
    }

    private OnIsLikeClickListener onIsLikeClickListener;
    private OnImageClickListener onImageClickListener;

    public interface OnIsLikeClickListener {
        void onIsLikeClick(int position);
    }

    public void setOnIsLikeClickListener(OnIsLikeClickListener listener) {
        this.onIsLikeClickListener = listener;
    }
    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.onImageClickListener = listener;
    }

}
