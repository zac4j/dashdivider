package com.zac4j.decor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Dash divider for grid layout manager
 * Created by Zaccc on 2017/8/15.
 */

public class GridDashDivider extends RecyclerView.ItemDecoration {

  // Dash divider paint.
  private Paint mPaint;
  // Draw strategy.
  private boolean[] mDrawer;
  // Offset strategy.
  private int[] mOffset;
  // View bounds container.
  private final Rect mBounds = new Rect();

  /**
   * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
   * {@link LinearLayoutManager}.
   */
  public GridDashDivider(int dashGap, int dashLength, int dashThickness, int color,
      boolean[] drawer, int[] offset) {

    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(color);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(dashThickness);
    mPaint.setPathEffect(new DashPathEffect(new float[] { dashLength, dashGap }, 0));

    mDrawer = drawer;
    mOffset = offset;
  }

  @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    if (parent.getLayoutManager() == null) {
      return;
    }
    if (mDrawer[1] || mDrawer[3]) {
      drawVertical(c, parent);
    }
    if (mDrawer[0] || mDrawer[2]) {
      drawHorizontal(c, parent);
    }
  }

  /**
   * Draw divider for vertical display list view
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

      final int startX = mBounds.left + Math.round(child.getTranslationX()) + mOffset[0];
      final int stopX = mBounds.right + Math.round(child.getTranslationX()) - mOffset[2];
      // Draw bottom divider.
      if (mDrawer[3]) {
        final int bottomY = mBounds.bottom + Math.round(child.getTranslationY());
        canvas.drawLine(startX, bottomY, stopX, bottomY, mPaint);
        // Avoiding over draw, draw most top divider.
        if (mDrawer[1] && i < spanCount) {
          final int topY = mBounds.top + Math.round(child.getTranslationY());
          canvas.drawLine(startX, topY, stopX, topY, mPaint);
        }
        // Only Draw top divider.
      } else {
        final int topY = mBounds.top + Math.round(child.getTranslationY());
        canvas.drawLine(startX, topY, stopX, topY, mPaint);
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

      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

      final int startY = mBounds.top + Math.round(child.getTranslationY()) + mOffset[1];
      final int stopY = mBounds.bottom + Math.round(child.getTranslationY()) - mOffset[3];

      // Draw right divider.
      if (mDrawer[2]) {
        final int rightX = mBounds.right + Math.round(child.getTranslationX());
        canvas.drawLine(rightX, startY, rightX, stopY, mPaint);
        // Avoiding over draw, draw most left divider.
        if (mDrawer[0] && i % spanCount == 0) {
          final int leftX = mBounds.left + Math.round(child.getTranslationX());
          canvas.drawLine(leftX, startY, leftX, stopY, mPaint);
        }
        // Only draw left divider.
      } else {
        final int leftX = child.getLeft() - params.leftMargin + mOffset[0];
        canvas.drawLine(leftX, startY, leftX, stopY, mPaint);
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
    private int dashGap;
    private int dashLength;
    private int dashThickness;
    private int color;
    private boolean[] drawer;
    private int[] offset;

    public Builder(Context context) {
      this.context = context;
    }

    public Builder dashGap(int gap) {
      if (gap <= 0) {
        throw new IllegalArgumentException("Dash gap must greater than 0.");
      }
      this.dashGap = gap;
      return this;
    }

    public Builder dashLength(int length) {
      if (length <= 0) {
        throw new IllegalArgumentException("Dash length must greater than 0.");
      }
      this.dashLength = length;
      return this;
    }

    public Builder dashThickness(int thickness) {
      if (thickness <= 0) {
        throw new IllegalArgumentException("Dash thickness must greater than 0.");
      }
      this.dashThickness = thickness;
      return this;
    }

    public Builder color(@ColorInt int color) {
      this.color = color;
      return this;
    }

    public Builder drawer(boolean left, boolean top, boolean right, boolean bottom) {
      this.drawer = new boolean[] { left, top, right, bottom };
      return this;
    }

    public Builder offset(int left, int top, int right, int bottom) {
      this.offset = new int[] { left, top, right, bottom };
      return this;
    }

    public GridDashDivider build() {
      if (dashGap <= 0) {
        throw new IllegalArgumentException("Dash gap must greater than 0.");
      }
      if (dashLength <= 0) {
        throw new IllegalArgumentException("Dash length must greater than 0.");
      }
      if (dashThickness <= 0) {
        throw new IllegalArgumentException("Dash thickness must greater than 0.");
      }
      return new GridDashDivider(dashGap, dashLength, dashThickness, color, drawer, offset);
    }
  }
}
