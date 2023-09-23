package com.guet.demo_android.Adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guet.demo_android.R;
import com.guet.demo_android.Type.ShareDetail;
import com.guet.demo_android.ui.PictureDetailActivity;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private List<String> carouselImages;
    FocusPhotoAdapter adapter;

    private int position;

    public void setAdapter(FocusPhotoAdapter adapter) {
        this.adapter = adapter;
    }

    public CarouselAdapter(List<String> carouselImages,int position) {
        this.carouselImages = carouselImages;
        this.position=position;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_item_layout, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        String imageUrl = carouselImages.get(position); // 从数据源获取图片链接
        // 使用Glide加载图片到ImageView
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(view -> {
            ShareDetail clickedItem = adapter.getRecords().get(this.position);
            // 在这里执行相应的操作，例如查看大图或者其他操作
            Intent intent = new Intent(adapter.getContext(), PictureDetailActivity.class);
            intent.putExtra("userId", clickedItem.getpUserId());
            intent.putExtra("username",clickedItem.getUsername());
            intent.putExtra("shareId", clickedItem.getId());
            adapter.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carouselImages.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carouselImageView);
        }
    }
}
