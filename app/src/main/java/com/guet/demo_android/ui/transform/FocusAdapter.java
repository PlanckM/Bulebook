package com.guet.demo_android.ui.transform;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.guet.demo_android.R;
import com.guet.demo_android.Type.ShareDetail;

import java.util.ArrayList;
import java.util.List;

public class FocusAdapter extends RecyclerView.Adapter<FocusAdapter.myViewHodler> {
    private List<ShareDetail> records;
    private Context context;

        //创建构造函数
        public FocusAdapter(List<ShareDetail> records, Context context) {
            //将传递过来的数据，赋值给本地变量
            this.context = context;//上下文
            this.records = records;//实体类数据ArrayList
        }
        @Override
        public FocusAdapter.myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
            //创建自定义布局
            View itemView = View.inflate(context, R.layout.item_image, null);
            return new FocusAdapter.myViewHodler(itemView);
        }
        @Override
        public void onBindViewHolder(FocusAdapter.myViewHodler holder, int position) {
            //根据点击位置绑定数据
            ShareDetail data = records.get(position);
            holder.mItemUserName.setText(data.getUsername());
            holder.mItemTitle.setText(data.getTitle());
            Glide.with(context).load(data.getImageUrlList().get(0)).into(holder.imageView);
        }
        @Override
        public int getItemCount() {
            return records.size();
        }
        //自定义viewhodler
        class myViewHodler extends RecyclerView.ViewHolder {
            public ImageView imageView;

            private TextView mItemUserName;
            private TextView mItemTitle;
            public myViewHodler(View itemView) {
                super(itemView);
                imageView =itemView.findViewById(R.id.iv_image);
                mItemUserName =  itemView.findViewById(R.id.tv_username);
                mItemTitle =  itemView.findViewById(R.id.tv_title_myshare);
//                每个item设置点击事件
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //可以选择直接在本位置直接写业务处理

                        //此处回传点击监听事件
                    }
                });
            }
        }
    }