package com.fightzhao.baseadapterdemo.callback;

import android.view.View;

/**
 * Created by fightzhao on 16/5/10.
 */
public interface OnItemClickListener {
    /**
     * 单击事件
     *
     * @param view
     * @param position
     */
    void onItemClick(View view, int position);

    /**
     * 双击事件
     *
     * @param view
     * @param position
     */
    void onItemLongClick(View view, int position);

    /**
     * 布局中TextView的点击事件
     * @param view
     * @param position
     */
    void onTextViewClick(View view, int position);
}
