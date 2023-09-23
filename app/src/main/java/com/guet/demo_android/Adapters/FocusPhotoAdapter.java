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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.guet.demo_android.HttpUtils;
import com.guet.demo_android.R;
import com.guet.demo_android.Type.PicList;
import com.guet.demo_android.Type.ShareDetail;
import com.guet.demo_android.VolleyCallback;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FocusPhotoAdapter extends RecyclerView.Adapter<FocusPhotoAdapter.FocusImageViewHolder>{
    private List<ShareDetail> records;
    private Context context;

    public FocusPhotoAdapter(List<ShareDetail> records, Context context) {
        this.records = records;
        this.context = context;
    }

    @NonNull
    @Override
    public FocusImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_focus, parent, false);
        return new FocusPhotoAdapter.FocusImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FocusImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
                final  boolean[] iscollected={record.getHasCollect()};
                if (headImage == null) {
                    // 如果用户没有设置头像，则使用默认头像。
                    headImage = "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2023/09/20/db9adc6b-228b-44fa-9d00-b8ce16e8e40a.jpg";
                }
                holder.titleTextView.setText(title);
                holder.contentTextView.setText(username);
                holder.likeNumTextView.setText(likeNum);


                Glide.with(context)
                        .load(headImage)
                        .into(holder.headImageView);

                //
                CarouselAdapter carouselAdapter = new CarouselAdapter(record.getImageUrlList());
                holder.viewPager.setAdapter(carouselAdapter); //
                //初始化图标的状态
                if (islike[0]) {
                    holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_32); // 已点赞状态
                } else {
                    holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_border_32); // 未点赞状态
                }
                if (iscollected[0]){
                    holder.collectedImg.setImageResource(R.drawable.baseline_star_24);
                }else {
                    holder.collectedImg.setImageResource(R.drawable.baseline_star_outline_36);

                }
                // 点击事件监听器
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handleLikeClick(holder, record, position);
                    }
                };

                View.OnClickListener clickCollectListener=new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handlecollectedClick(holder,record);
                    }
                };

                //每一个图标绑定点击事件
                holder.isLikeImageView.setOnClickListener(clickListener);
                holder.collectedImg.setOnClickListener(clickCollectListener);
                holder.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {

                        updateDotIndicator(holder, position);
                    }
                });
            }
        }
    }

    private void handlecollectedClick(FocusImageViewHolder holder, ShareDetail record) {
        final boolean[] isCollect = {record.getHasCollect()};
        int currentCollectNum=record.getCollectNum();
        // åå»ºä¸ä¸ªç¼©æ¾å¨ç»å¯¹è±¡ï¼è¿éç¤ºä¾ä½¿ç¨2åç¼©å°åæ¢å¤åå§å¤§å°
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1.0f, 0.5f, // å¼å§åç»æçXç¼©æ¾æ¯ä¾
                1.0f, 0.5f, // å¼å§åç»æçYç¼©æ¾æ¯ä¾
                Animation.RELATIVE_TO_SELF, 0.5f, // ç¼©æ¾ä¸­å¿çXåæ ï¼ç¸å¯¹äºèªèº«ï¼
                Animation.RELATIVE_TO_SELF, 0.5f  // ç¼©æ¾ä¸­å¿çYåæ ï¼ç¸å¯¹äºèªèº«ï¼
        );

        // åå»ºä¸ä¸ªæ¾å¤§å¨ç»å¯¹è±¡ï¼ä½¿ç¨ç¸åçæå¼å¨åæç»­æ¶é´
        ScaleAnimation expandAnimation = new ScaleAnimation(
                0.5f, 1.0f, // å¼å§åç»æçXç¼©æ¾æ¯ä¾
                0.5f, 1.0f, // å¼å§åç»æçYç¼©æ¾æ¯ä¾
                Animation.RELATIVE_TO_SELF, 0.5f, // ç¼©æ¾ä¸­å¿çXåæ ï¼ç¸å¯¹äºèªèº«ï¼
                Animation.RELATIVE_TO_SELF, 0.5f  // ç¼©æ¾ä¸­å¿çYåæ ï¼ç¸å¯¹äºèªèº«ï¼
        );

        ScaleAnimation expandAnimation2 = new ScaleAnimation(
                1f, 1.3f, // å¼å§åç»æçXç¼©æ¾æ¯ä¾
                1f, 1.3f, // å¼å§åç»æçYç¼©æ¾æ¯ä¾
                Animation.RELATIVE_TO_SELF, 0.5f, // ç¼©æ¾ä¸­å¿çXåæ ï¼ç¸å¯¹äºèªèº«ï¼
                Animation.RELATIVE_TO_SELF, 0.5f  // ç¼©æ¾ä¸­å¿çYåæ ï¼ç¸å¯¹äºèªèº«ï¼
        );
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(
                1.3f, 1.0f, // å¼å§åç»æçXç¼©æ¾æ¯ä¾
                1.3f, 1.0f, // å¼å§åç»æçYç¼©æ¾æ¯ä¾
                Animation.RELATIVE_TO_SELF, 0.5f, // ç¼©æ¾ä¸­å¿çXåæ ï¼ç¸å¯¹äºèªèº«ï¼
                Animation.RELATIVE_TO_SELF, 0.5f  // ç¼©æ¾ä¸­å¿çYåæ ï¼ç¸å¯¹äºèªèº«ï¼
        );

        // è®¾ç½®å¨ç»æç»­æ¶é´ï¼ä»¥æ¯«ç§ä¸ºåä½ï¼
        scaleAnimation.setDuration(200); // è¿éè®¾ç½®ä¸º300æ¯«ç§ï¼ä½ å¯ä»¥æ ¹æ®éè¦è°æ´
        expandAnimation.setDuration(200); // è®¾ç½®æç»­æ¶é´
        expandAnimation2.setDuration(100);
        scaleAnimation2.setDuration(100);

        // ä½¿ç¨æå¼å¨ä½¿å¨ç»æ´å å¹³æ»
        expandAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        expandAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleAnimation2.setInterpolator(new AccelerateDecelerateInterpolator());

        // è®¾ç½®å¨ç»ç»æåä¿ææç»ç¶æ
        scaleAnimation.setFillAfter(true);

        // æ¾å°å¨ç»åºç¨å°ImageView
        holder.collectedImg.startAnimation(scaleAnimation);
        // åºç¨æ¾å¤§å¨ç»
        holder.collectedImg.startAnimation(expandAnimation);
        holder.collectedImg.startAnimation(expandAnimation2);
        holder.collectedImg.startAnimation(scaleAnimation2);

        if (isCollect[0]) {
            isCollect[0] = false;
            if (record.getCollectNum()>1){
                currentCollectNum--;
            }
            record.setHasCollect(false);
            record.setCollectNum(currentCollectNum);
            holder.collectedImg.setImageResource(R.drawable.baseline_star_outline_36); // æªæ¶èç¶æ
            sendCancelcollectRequest(record.getCollectId(), "http://47.107.52.7:88/member/photo/collect/cancel?");
        } else {
            // æ´æ°ç¹èµæ°éçæ¾ç¤º
            currentCollectNum++;
            holder.collectedImg.setImageResource(R.drawable.baseline_star_36); // å·²æ¶èç¶æ
            isCollect[0] = true;
            record.setHasCollect(true);
            record.setCollectNum(currentCollectNum);
            sendCollectedRequest(record.getId(), "http://47.107.52.7:88/member/photo/collect?");
        }
    }
    // 处理点赞点击事件
    private void handleLikeClick(FocusImageViewHolder holder, ShareDetail record, int position) {
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
            holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_border_32); // 未点赞状态
            sendCancelLikeRequest(record.getLikeId(), "http://47.107.52.7:88/member/photo/like/cancel?");
        } else {
            // 点赞逻辑...
            currentLikeNum++;
            record.setLikeNum(currentLikeNum);
            // 更新点赞数量的显示
            holder.likeNumTextView.setText(String.valueOf(currentLikeNum));
            holder.isLikeImageView.setImageResource(R.drawable.baseline_favorite_32); // 已点赞状态
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

    //ç¹èµ
    private void sendLikeRequest(String id, String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shareId", id);
        params.put("userId", "1696496527540883456");
        HttpUtils.post(url, params, false, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
                }.getType();
                HttpUtils.ResponseBody<PicList> response = gson.fromJson(body, type);
                Log.d(TAG, "onSuccess: 00000" + response.getCode());
                if (response != null && response.getCode() == 200) {
//                    Toast.makeText(context.getApplicationContext(), "ç¹å»æå!", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }

    //åæ¶æ¶è
    private void sendCancelcollectRequest(String id, String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("collectId", id);
        HttpUtils.post(url, params, false, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
                }.getType();
                HttpUtils.ResponseBody<PicList> response = gson.fromJson(body, type);
                Log.d(TAG, "onSuccess: 1111" + response.getCode());
                if (response != null && response.getCode() == 200) {
//                    Toast.makeText(context.getApplicationContext(), "ç¹å»æå!", Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });
    }
    //æ¶è
    private void sendCollectedRequest(String id, String url) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shareId", id);
        params.put("userId", "1696496527540883456");
        HttpUtils.post(url, params, false, new VolleyCallback() {
            @Override
            public void onSuccess(String body, Gson gson) {
                Type type = new TypeToken<HttpUtils.ResponseBody<PicList>>() {
                }.getType();
                HttpUtils.ResponseBody<PicList> response = gson.fromJson(body, type);
                Log.d(TAG, "onSuccess: 00000" + response.getCode());
                if (response != null && response.getCode() == 200) {
//                    Toast.makeText(context.getApplicationContext(), "ç¹å»æå!", Toast.LENGTH_SHORT).show();
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

    private void updateDotIndicator(FocusImageViewHolder holder, int position) {
        // è·åå°åç¹æç¤ºå¨çç¶å¸å±
        LinearLayout dotIndicator = holder.dotIndicator;

        // è·åå°åç¹çæ°éï¼è¿åºè¯¥ä¸è½®æ­å¾çæ°æ®é¡¹æ°éä¸è´
        int itemCount = records.get(position).getImageUrlList().size();

        // æ¸é¤ä¹åçææå°åç¹
        dotIndicator.removeAllViews();

        // æ·»å æ°çå°åç¹
        for (int i = 0; i < itemCount; i++) {
            View dot = new View(context);
            dot.setLayoutParams(new LinearLayout.LayoutParams(
                    15, 15 // è®¾ç½®å°åç¹çå®½åº¦åé«åº¦
            ));
            dot.setBackgroundResource(R.drawable.baseline_circle_unselected_24); // æªéä¸­ç¶æçèæ¯
            dotIndicator.addView(dot); // æ·»å å°å°åç¹æç¤ºå¨çç¶å¸å±ä¸­
        }

        // æ´æ°å½åé¡µé¢å¯¹åºçå°åç¹ä¸ºéä¸­ç¶æ
        if (itemCount > 0) {
            dotIndicator.getChildAt(position % itemCount).setBackgroundResource(R.drawable.baseline_circle_24); // éä¸­ç¶æçèæ¯
        }
    }


    public static class FocusImageViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView contentTextView;
        TextView likeNumTextView;
        ImageView isLikeImageView;
        ImageView headImageView;
        ImageView collectedImg;
        LinearLayout dotIndicator; // æ·»å è¿ä¸ªæååé
        ViewPager2 viewPager;
        public FocusImageViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_title_myshare);
            contentTextView = itemView.findViewById(R.id.tv_username);
            likeNumTextView = itemView.findViewById(R.id.like_num);
            isLikeImageView = itemView.findViewById(R.id.is_like);
            headImageView = itemView.findViewById(R.id.headImageView);
            collectedImg=itemView.findViewById(R.id.collect_img);
            dotIndicator = itemView.findViewById(R.id.dotIndicator); // åå§åå°åç¹æç¤ºå¨
            viewPager = itemView.findViewById(R.id.viewPager); // ç¡®ä¿IDå¹é
            float letterSpacing = 0.05f; // è®¾ç½®å­é´è·ï¼å¯ä»¥æ ¹æ®éè¦è°æ´è¿ä¸ªå¼
            titleTextView.setLetterSpacing(letterSpacing);
        }
    }

    public void setRecords(List<ShareDetail> newRecords) {
        // 更新适配器的数据集
        this.records = newRecords;
        notifyDataSetChanged(); // 刷新 RecyclerView
    }
    private SharePhotoAdapter.OnIsLikeClickListener onIsLikeClickListener;
    private SharePhotoAdapter.OnImageClickListener onImageClickListener;

    public interface OnIsLikeClickListener {
        void onIsLikeClick(int position);
    }

    public void setOnIsLikeClickListener(SharePhotoAdapter.OnIsLikeClickListener listener) {
        this.onIsLikeClickListener = listener;
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }

    public void setOnImageClickListener(SharePhotoAdapter.OnImageClickListener listener) {
        this.onImageClickListener = listener;
    }
}
