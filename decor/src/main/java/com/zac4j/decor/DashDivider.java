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
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Dash divider for linear layout manager
 * Created by Zaccc on 2017/8/15.
 */

public class DashDivider extends RecyclerView.ItemDecoration {

  public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
  public static final int VERTICAL = LinearLayout.VERTICAL;

  private Paint mPaint;

  /**
   * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
   */
  private int mOrientation;

  /**
   * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
   * {@link LinearLayoutManager}.
   *
   * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
   */
  private DashDivider(int dashGap, int dashLength, int dashThickness, int color, int orientation) {

    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(color);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(dashThickness);
    mPaint.setPathEffect(new DashPathEffect(new float[] { dashLength, dashGap }, 0));

    setOrientation(orientation);
  }

  /**
   * Sets the orientation for this divider. This should be called if
   * {@link RecyclerView.LayoutManager} changes orientation.
   *
   * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
   */
  public void setOrientation(int orientation) {
    if (orientation != HORIZONTAL && orientation != VERTICAL) {
      throw new IllegalArgumentException(
          "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
    }
    mOrientation = orientation;
  }

  @Override public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    if (parent.getLayoutManager() == null) {
      return;
    }
    if (mOrientation == VERTICAL) {
      drawVertical(c, parent);
    } else {
      drawHorizontal(c, parent);
    }
  }

  private void drawVertical(Canvas canvas, RecyclerView parent) {
    canvas.save();
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int startX = child.getLeft() - params.leftMargin;
      final int stopX = child.getRight() + params.rightMargin;
      final int y = child.getBottom() + params.bottomMargin;

      canvas.drawLine(startX, y, stopX, y, mPaint);
    }
    canvas.restore();
  }

  private void drawHorizontal(Canvas canvas, RecyclerView parent) {
    canvas.save();

    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);

      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int rightX = child.getRight() + params.rightMargin;
      final int startY = child.getTop() - params.topMargin;
      final int stopY = child.getBottom() + params.bottomMargin;

      canvas.drawLine(rightX, startY, rightX, stopY, mPaint);
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
    private int orientation = VERTICAL;

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

    public Builder orientation(int orientation) {
      if (orientation != HORIZONTAL && orientation != VERTICAL) {
        throw new IllegalArgumentException("Illegal dashed divider orientation!");
      }
      this.orientation = orientation;
      return this;
    }

    public DashDivider build() {
      if (dashGap <= 0) {
        throw new IllegalArgumentException("Dash gap must greater than 0.");
      }
      if (dashLength <= 0) {
        throw new IllegalArgumentException("Dash length must greater than 0.");
      }
      if (dashThickness <= 0) {
        throw new IllegalArgumentException("Dash thickness must greater than 0.");
      }
      if (orientation != HORIZONTAL && orientation != VERTICAL) {
        throw new IllegalArgumentException("Illegal dashed divider orientation!");
      }
      return new DashDivider(dashGap, dashLength, dashThickness, color, orientation);
    }
  }
}
