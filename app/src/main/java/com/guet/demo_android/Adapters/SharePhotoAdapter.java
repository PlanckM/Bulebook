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
import com.guet.demo_android.Type.ShareDetail;
import com.guet.demo_android.ui.SharedViewModel;
import java.util.List;

public class SharePhotoAdapter extends RecyclerView.Adapter<SharePhotoAdapter.ImageViewHolder> {
    private List<ShareDetail> records;
    private Context context;

    public SharePhotoAdapter(List<ShareDetail> records, Context context, SharedViewModel sharedViewModel) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 布局资源item_image文件加载为View对象
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (records.get(position).getImageUrlList().size() == 0) {
            // 处理数据为空的情况，例如显示默认数据或者隐藏视图等操作
        } else {
            ShareDetail record = records.get(position);
            String imageUrl = record.getImageUrlList().get(0); // 获取第一个图片链接
            // 获取标题和内容，并设置到对应的 TextView 中
            String title = record.getTitle();
            String username = record.getUsername(); // 用户名字段是 username
            username = "guxuankun";
            holder.titleTextView.setText(title);
            holder.contentTextView.setText(username);
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.imageView);

        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView contentTextView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            titleTextView = itemView.findViewById(R.id.tv_title_myshare);
            contentTextView = itemView.findViewById(R.id.tv_username);
        }
    }

    public void setRecords(List<ShareDetail> newRecords) {
        // 更新适配器的数据集
        this.records = newRecords;
        notifyDataSetChanged(); // 刷新 RecyclerView
    }
}
