package com.guet.demo_android.Adapters;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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


    public SharePhotoAdapter(List<ShareDetail> records, Context context) {
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
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(records!=null)
        {
            if (records.get(position).getImageUrlList().size() == 0) {
                // 处理数据为空的情况，例如显示默认数据或者隐藏视图等操作
            } else {
                ShareDetail record = records.get(position);
                String imageUrl = record.getImageUrlList().get(0); // 获取第一个图片链接
                // 获取标题和内容，并设置到对应的 TextView 中
                String title = record.getTitle();
                String username = record.getUsername(); // 用户名字段是 username
                String likeNum = String.valueOf(record.getLikeNum());
                String headImage = record.getAvatar();
                final boolean[] islike = {record.getHasLike()};

                if (headImage == null) {
                    // 如果用户没有设置头像，则使用默认头像。
                    headImage = "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2023/09/20/db9adc6b-228b-44fa-9d00-b8ce16e8e40a.jpg";
                }
                holder.titleTextView.setText(title);
                holder.contentTextView.setText(username);
                holder.likeNumTextView.setText(likeNum);

                Glide.with(context)
                        .load(imageUrl)
                        .into(holder.imageView);
                Glide.with(context)
                        .load(headImage)
                        .into(holder.headImageView);

                //初始化图标的状态
                if (islike[0]) {
                    holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_20); // 已点赞状态
                } else {
                    holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_border_20); // 未点赞状态
                }

                // 点击事件监听器
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleLikeClick(holder, record, position);
                    }
                };

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
                holder.isLikeImageView.setOnClickListener(clickListener);
            }
        }
    }

    // 处理点赞点击事件
    private void handleLikeClick(ImageViewHolder holder, ShareDetail record, int position) {
        boolean isLiked = record.getHasLike();
        int currentLikeNum = record.getLikeNum();
        final boolean[] islike = {record.getHasLike()};
        // 创建一个缩放动画对象，这里示例使用2倍缩小和恢复原始大小
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 0.5f, // 开始和结束的X缩放比例
                1.0f, 0.5f, // 开始和结束的Y缩放比例
                Animation.RELATIVE_TO_SELF, 0.5f, // 缩放中心的X坐标（相对于自身）
                Animation.RELATIVE_TO_SELF, 0.5f  // 缩放中心的Y坐标（相对于自身）
        );

        // 创建一个放大动画对象，使用相同的插值器和持续时间
        ScaleAnimation expandAnimation = new ScaleAnimation(
                0.5f, 1.0f, // 开始和结束的X缩放比例
                0.5f, 1.0f, // 开始和结束的Y缩放比例
                Animation.RELATIVE_TO_SELF, 0.5f, // 缩放中心的X坐标（相对于自身）
                Animation.RELATIVE_TO_SELF, 0.5f  // 缩放中心的Y坐标（相对于自身）
        );

        ScaleAnimation expandAnimation2 = new ScaleAnimation(
                1f, 1.3f, // 开始和结束的X缩放比例
                1f, 1.3f, // 开始和结束的Y缩放比例
                Animation.RELATIVE_TO_SELF, 0.5f, // 缩放中心的X坐标（相对于自身）
                Animation.RELATIVE_TO_SELF, 0.5f  // 缩放中心的Y坐标（相对于自身）
        );
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(
                1.3f, 1.0f, // 开始和结束的X缩放比例
                1.3f, 1.0f, // 开始和结束的Y缩放比例
                Animation.RELATIVE_TO_SELF, 0.5f, // 缩放中心的X坐标（相对于自身）
                Animation.RELATIVE_TO_SELF, 0.5f  // 缩放中心的Y坐标（相对于自身）
        );

        // 设置动画持续时间（以毫秒为单位）
        scaleAnimation.setDuration(200); // 这里设置为300毫秒，你可以根据需要调整
        expandAnimation.setDuration(200); // 设置持续时间
        expandAnimation2.setDuration(100);
        scaleAnimation2.setDuration(100);

        // 使用插值器使动画更加平滑
        expandAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        expandAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());

        // 设置动画结束后保持最终状态
        scaleAnimation.setFillAfter(true);

        // 放小动画应用到ImageView
        holder.isLikeImageView.startAnimation(scaleAnimation);
        // 应用放大动画
        holder.isLikeImageView.startAnimation(expandAnimation);
        holder.isLikeImageView.startAnimation(expandAnimation2);
        holder.isLikeImageView.startAnimation(scaleAnimation2);

        if (isLiked) {
            // 取消点赞逻辑...
            if (currentLikeNum > 0) {
                // 如果当前点赞数量大于0，则减一
                currentLikeNum--;
                record.setLikeNum(currentLikeNum);
                // 更新点赞数量的显示
                holder.likeNumTextView.setText(String.valueOf(currentLikeNum));
                islike[0] = false;
                record.setHasLike(false);
            }
            holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_border_24); // 未点赞状态
            sendCancelLikeRequest(record.getLikeId(), "http://47.107.52.7:88/member/photo/like/cancel?");
        } else {
            // 点赞逻辑...
            currentLikeNum++;
            record.setLikeNum(currentLikeNum);
            // 更新点赞数量的显示
            holder.likeNumTextView.setText(String.valueOf(currentLikeNum));
            holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_20); // 已点赞状态
            islike[0] = true;
            record.setHasLike(true);
            sendLikeRequest(record.getId(), "http://47.107.52.7:88/member/photo/like?");
        }

//        if (onIsLikeClickListener != null) {
//            onIsLikeClickListener.onIsLikeClick(position);
//        }
    }

    //取消点赞
    private void sendCancelLikeRequest(String id, String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("likeId", id);
        HttpUtils.post(url, params, false, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
                }.getType();
                HttpUtils.ResponseBody<PicList> response = gson.fromJson(body, type);
                Log.d(TAG, "onSuccess: 1111" + response.getCode());
                if (response != null && response.getCode() == 200) {
//                    Toast.makeText(context.getApplicationContext(), "点击成功!", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });
    }

    //点赞
    private void sendLikeRequest(String id, String url) {
        Map<String, Object> params = new HashMap<>();
        params.put("shareId", id);
        AppContext app = (AppContext) context.getApplicationContext();
        params.put("userId", app.user.getId());
        HttpUtils.post(url, params, false, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
                }.getType();
                HttpUtils.ResponseBody<PicList> response = gson.fromJson(body, type);
                Log.d(TAG, "onSuccess: 00000" + response.getCode());
                if (response != null && response.getCode() == 200) {
//                    Toast.makeText(context.getApplicationContext(), "点击成功!", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(records==null) return 0;
        return records.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView contentTextView;
        TextView likeNumTextView;
        ImageView isLikeImageView;
        ImageView headImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_image);
            titleTextView = itemView.findViewById(R.id.tv_title_myshare);
            contentTextView = itemView.findViewById(R.id.tv_username);
            likeNumTextView = itemView.findViewById(R.id.like_num);
            isLikeImageView = itemView.findViewById(R.id.is_like);
            headImageView = itemView.findViewById(R.id.headImageView);
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
