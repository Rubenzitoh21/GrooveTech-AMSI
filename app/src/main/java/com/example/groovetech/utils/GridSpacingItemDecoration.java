package com.example.groovetech.utils;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int space;  // Space between items

    public GridSpacingItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // Apply space to the left, right, top, and bottom of each item
        outRect.left = space;
        outRect.right = space;

        // Add extra space for the first row if needed (optional)
        if (parent.getChildAdapterPosition(view) < 2) {
            outRect.top = space;  // Adds extra space to the top of the first row
        }
    }
}
