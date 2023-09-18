package com.guet.demo_android.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.guet.demo_android.R;
import com.guet.demo_android.ui.SharedViewModel;

import java.util.List;

public class SharePhotoAdapter extends RecyclerView.Adapter<SharePhotoAdapter.ImageViewHolder> {
    private List<String> imageUrls;
    private Context context;
    private SharedViewModel sharedViewModel;
    public SharePhotoAdapter(List<String> imageUrls, Context context, SharedViewModel sharedViewModel) {
        this.imageUrls = imageUrls;
        this.context = context;
        this.sharedViewModel = sharedViewModel; // 传递 SharedViewModel
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //布局资源item_image文件加载为View对象
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        // 获取标题和内容，并设置到对应的 TextView 中
        String title = "上海citywalk | 如果你只有1天时间和一部相机";
        String username = "古烜坤的旅游本";
        holder.titleTextView.setText(title);
        holder.contentTextView.setText(username);
        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView contentTextView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            titleTextView = itemView.findViewById(R.id.tv_title_myshare);
            contentTextView = itemView.findViewById(R.id.tv_subtitle);
        }
    }
    public void setImageUrls(List<String> newImageUrls) {
        // 更新适配器的数据集
        this.imageUrls = newImageUrls;
        notifyDataSetChanged(); // 刷新 RecyclerView
    }

}
