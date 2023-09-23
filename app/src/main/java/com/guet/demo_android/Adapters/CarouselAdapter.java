package com.guet.demo_android.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guet.demo_android.R;

import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {
    private List<String> carouselImages;

    public CarouselAdapter(List<String> carouselImages) {
        this.carouselImages = carouselImages;
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
