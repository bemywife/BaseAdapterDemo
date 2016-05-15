package com.fightzhao.baseadapterdemo.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by fightzhao on 16/5/10.
 */
public abstract class BaseListViewAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mData;

    public BaseListViewAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= mData.size()) {
            return null;
        }
        return mData.get(position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 该方法需要子类实现，需要返回item布局的resource id
     *
     * @return
     */
    public abstract int getItemResource();

    /**
     * 使用该getItemView方法替换原来的getView方法，需要子类实现
     *
     * @param position
     * @param convertView
     * @param holder
     * @return
     */
    public abstract View getItemView(int position, View convertView, ViewHolder holder);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = View.inflate(mContext, getItemResource(), null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return getItemView(position, convertView, holder);
    }

    public class ViewHolder {
        private SparseArray<View> views = new SparseArray<View>();
        private View convertView;

        public ViewHolder(View convertView) {
            this.convertView = convertView;
        }

        /**
         * 通过控件的Id获取对应的控件，如果没有则加入views
         *
         * @param resId
         * @param <T>
         * @return
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = convertView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }
    }

    public void add(T t) {
        mData.add(t);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        mData.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        mData.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem) {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    public BaseListViewAdapter<T> setItems(Collection<? extends T> collection) {
        if (collection != null) {
            Collection<T> copy = new ArrayList<T>(collection);
            mData.clear();
            addAll((List<T>) copy);
        } else {
            mData.clear();
        }
        notifyDataSetChanged();
        return this;
    }
}
