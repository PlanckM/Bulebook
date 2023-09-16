package com.guet.demo_android.ItemDecoration;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spanCount; // 列数
    private int spacing; // 间隔大小
    private boolean includeEdge; // 是否包括边缘

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // 获取item的位置
        int column = position % spanCount; // 获取item所在列

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // 左间隔
            outRect.right = (column + 1) * spacing / spanCount; // 右间隔

            if (position < spanCount) {
                outRect.top = spacing; // 顶部间隔
            }
            outRect.bottom = spacing; // 底部间隔
        } else {
            outRect.left = column * spacing / spanCount; // 左间隔
            outRect.right = spacing - (column + 1) * spacing / spanCount; // 右间隔

            if (position >= spanCount) {
                outRect.top = spacing; // 顶部间隔
            }
        }
    }
}
