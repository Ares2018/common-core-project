package com.zjrb.core.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义GridView解决与ScrollView的冲突
 *
 * @author a_liYa
 * @date 2016/4/29 20:18.
 *
 */
public class GridViewForScrollView extends GridView {

	public GridViewForScrollView(Context context) {
		super(context);
	}

	public GridViewForScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public GridViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 重写该方法，达到使GridView适应ScrollView的效果
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
