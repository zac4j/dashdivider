/*
 * Copyright (c) 2017 Zaccc (http://github.com/zac4j).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zac4j.decor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Dashed divider for grid layout manager bitmap implementation
 * Created by Zaccc on 2017/8/15.
 */

public class GridDashedDivider extends RecyclerView.ItemDecoration {

  /**
   * Draw divider strategy.
   * Draw a grid item's left|top|right|bottom aspect divider
   */
  private boolean[] mDrawer;
  /**
   * Hide divider strategy:
   * Hide left-most|top-most|right-most|bottom-most divider
   */
  private boolean[] mHider;
  /**
   * Set divider offset strategy.
   * Set grid item's left|top|right|bottom aspect divider line offset
   */
  private float[] mOffset;
  // View bounds container.
  private final Rect mBounds = new Rect();

  private Bitmap mVerticalDivider;
  private Bitmap mHorizontalDivider;

  /**
   * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
   * {@link LinearLayoutManager}.
   */
  private GridDashedDivider(Context context, boolean[] drawer, boolean[] hider, float[] offset) {
    mDrawer = drawer;
    mHider = hider;
    mOffset = offset;

    // Fetch bitmap from resource
    fetchBitmap(context);
  }

  @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    if (parent.getLayoutManager() == null) {
      return;
    }
    if (mDrawer[1] || mDrawer[3]) {
      drawHorizontal(c, parent);
    }
    if (mDrawer[0] || mDrawer[2]) {
      drawVertical(c, parent);
    }
  }

  private void fetchBitmap(Context context) {
    // fetch horizontal bitmap
    BitmapDrawable horizontalDrawable =
        (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.dash_divider_horizontal);
    mHorizontalDivider = horizontalDrawable.getBitmap();

    // fetch vertical bitmap
    BitmapDrawable verticalDrawable =
        (BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.dash_divider_vertical);
    mVerticalDivider = verticalDrawable.getBitmap();
  }

  /**
   * Draw divider for vertical display list view
   *
   * @param canvas canvas for draw divider
   * @param parent list view for display divider
   */
  private void drawHorizontal(Canvas canvas, RecyclerView parent) {
    canvas.save();

    int spanCount = 0;
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
    }

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);

      final float startX = mBounds.left + child.getTranslationX() + mOffset[0];
      final float stopX = mBounds.right - child.getTranslationX() - mOffset[2];
      // Draw bottom divider.
      if (mDrawer[3]) {
        // If should hide bottom-most divider
        if (mHider[3]) {
          int lastCount = childCount % spanCount;
          if (lastCount == 0) {
            if (i < childCount - spanCount) {
              final float bottomY = mBounds.bottom - child.getTranslationY();
              canvas.drawBitmap(mHorizontalDivider, new Rect(0, 0, mBounds.right - mBounds.left, 2),
                  new RectF(startX, bottomY - 2, stopX, bottomY), null);
            }
          } else {
            if (i < childCount - lastCount) {
              final float bottomY = mBounds.bottom - child.getTranslationY();
              canvas.drawBitmap(mHorizontalDivider, new Rect(0, 0, mBounds.right - mBounds.left, 2),
                  new RectF(startX, bottomY - 2, stopX, bottomY), null);
            }
          }
        }

        // Avoiding over draw, draw top-most divider.
        if (mDrawer[1] && i < spanCount && !mHider[1]) {
          final float topY = mBounds.top + child.getTranslationY();
          canvas.drawBitmap(mHorizontalDivider, new Rect(0, 0, mBounds.right - mBounds.left, 2),
              new RectF(startX, topY, stopX, topY + 2), null);
        }
        // Only Draw top divider.
      } else {
        final float topY = mBounds.top + child.getTranslationY();
        canvas.drawBitmap(mHorizontalDivider, new Rect(0, 0, mBounds.right - mBounds.left, 2),
            new RectF(startX, topY, stopX, topY + 2), null);
      }
    }
    canvas.restore();
  }

  /**
   * Draw divider for horizontal display list view
   *
   * @param canvas canvas for draw divider
   * @param parent list view for display divider
   */
  private void drawVertical(Canvas canvas, RecyclerView parent) {
    canvas.save();

    int spanCount = 0;
    RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
    if (layoutManager instanceof GridLayoutManager) {
      spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
    }

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);

      final float startY = mBounds.top + child.getTranslationY() + mOffset[1];
      final float stopY = mBounds.bottom + child.getTranslationY() - mOffset[3];

      // Draw right divider.
      if (mDrawer[2]) {
        // If need hide right-most divider
        if (mHider[2] && (i + 1) % spanCount == 0) {
          continue;
        } else {
          final float rightX = mBounds.right - child.getTranslationX();
          canvas.drawBitmap(mVerticalDivider, new Rect(0, 0, 2, (int) (stopY - startY)),
              new RectF(rightX - 2, startY, rightX, stopY), null);
        }
        // Avoiding over draw, draw left-most divider.
        if (mDrawer[0] && i % spanCount == 0 && !mHider[0]) {
          final float leftX = mBounds.left + child.getTranslationX();
          canvas.drawBitmap(mVerticalDivider, new Rect(0, 0, 2, (int) (stopY - startY)),
              new RectF(leftX, startY, leftX + 2, stopY), null);
        }
        // Only draw left divider.
      } else {
        final float leftX = mBounds.left + child.getTranslationX();
        canvas.drawBitmap(mVerticalDivider, new Rect(0, 0, 2, (int) (stopY - startY)),
            new RectF(leftX, startY, leftX + 2, stopY), null);
      }
    }
    canvas.restore();
  }

  public static Builder with(@NonNull Context context) {
    if (context == null) {
      throw new IllegalArgumentException("context == null");
    }
    return new Builder(context);
  }

  public static class Builder {
    private Context context;
    private boolean[] drawer;
    private boolean[] hider;
    private float[] offset;

    public Builder(Context context) {
      this.context = context;
    }

    public Builder drawer(boolean left, boolean top, boolean right, boolean bottom) {
      this.drawer = new boolean[] { left, top, right, bottom };
      return this;
    }

    public Builder hider(boolean left, boolean top, boolean right, boolean bottom) {
      this.hider = new boolean[] { left, top, right, bottom };
      return this;
    }

    public Builder offset(float left, float top, float right, float bottom) {
      this.offset = new float[] { left, top, right, bottom };
      return this;
    }

    public GridDashedDivider build() {
      return new GridDashedDivider(context, drawer, hider, offset);
    }
  }
}
