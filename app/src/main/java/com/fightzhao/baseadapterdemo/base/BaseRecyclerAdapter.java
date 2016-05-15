package com.fightzhao.baseadapterdemo.base;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.fightzhao.baseadapterdemo.R;
import com.fightzhao.baseadapterdemo.callback.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fightzhao on 16/5/10.
 * RecyclerView通用适配器
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    /**
     * RecyclerView的加载样式
     * 当滑动到底部时候的加载
     */
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 2;
    public static final int TYPE_FOOTER = 3;

    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;
    protected OnItemClickListener mClickListener;
    private RecyclerView.LayoutManager mLayoutManager;

    protected boolean mUseAnimation;
    protected boolean mShowFooter;

    private int mLastPosition = -1;

    /**
     * 瀑布流
     *
     * @param savedInstanceState
     */
    private List<Integer> mHeights;

    public BaseRecyclerAdapter(Context context, List<T> data) {
        this(context, data, true);
    }

    public BaseRecyclerAdapter(Context context, List<T> data, boolean useAnimation) {
        this(context, data, useAnimation, null);
    }

    public BaseRecyclerAdapter(Context context, List<T> data, boolean useAnimation, RecyclerView.LayoutManager layoutManager) {
        mContext = context;
        mUseAnimation = useAnimation;
        mLayoutManager = layoutManager;
        mData = data == null ? new ArrayList<T>() : data;
        mInflater = LayoutInflater.from(context);

        mHeights = new ArrayList<Integer>();
        for (int i = 0; i < mData.size(); i++) {
            mHeights.add((int) (100 + Math.random() * 300));
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            // 底部加载更多的样式
            return new BaseRecyclerViewHolder(mContext, mInflater.inflate(R.layout.item_load_more, parent, false));
        } else {
            // 正常情况下的布局样式和点击事件(以接口的形式向外提供)
            final BaseRecyclerViewHolder holder = new BaseRecyclerViewHolder(mContext,
                    mInflater.inflate(getItemLayoutId(viewType), parent, false));
            if (mClickListener != null) {

                /**
                 * 点击事件
                 */
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onItemClick(v, holder.getLayoutPosition());
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mClickListener.onItemLongClick(v, holder.getLayoutPosition());
                        return false;
                    }
                });

                holder.getTextView(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickListener.onTextViewClick(v, holder.getLayoutPosition());
                    }
                });
            }
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_FOOTER) {
            if (mLayoutManager != null) {
                if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                    if (((StaggeredGridLayoutManager) mLayoutManager).getSpanCount() != 1) {
                        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView
                                .getLayoutParams();
                        params.setFullSpan(true);

                    } else if (mLayoutManager instanceof GridLayoutManager) {
                        if (((GridLayoutManager) mLayoutManager)
                                .getSpanCount() != 1 && ((GridLayoutManager) mLayoutManager)
                                .getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                            throw new RuntimeException("网格布局列数大于1时应该继承SpanSizeLookup时处理底部加载时布局占满一行。");
                        }
                    }
                }
            }

            // 底部加载的处理
            holder.getImageView(R.id.loading_icon).setVisibility(View.VISIBLE);
            holder.getAnimationDrawable(R.id.loading_icon).start();
        } else {
            // 布局样式处理完毕后,处理数据
            bindData(holder, position, mData.get(position));
            if (mUseAnimation) {
                setAnimation(holder.itemView, position);
            }

        }


    }

    @Override
    public int getItemCount() {
        int i = mShowFooter ? 1 : 0;
        return mData != null ? mData.size() + i : 0;
    }

    /**
     * Item的样式布局Id
     *
     * @param viewType
     * @return
     */
    public abstract int getItemLayoutId(int viewType);

    @Override
    public int getItemViewType(int position) {
        if (mShowFooter && getItemCount() - 1 == position) {
            return TYPE_FOOTER;
        }
        return bindViewType(position);
    }

    protected int bindViewType(int position) {
        return 0;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     * @param item
     */
    public abstract void bindData(BaseRecyclerViewHolder holder, int position, T item);

    /**
     * 动画的处理
     *
     * @param viewToAnimate
     * @param position
     */
    protected void setAnimation(View viewToAnimate, int position) {
        if (position > mLastPosition) {
            Animation animation = AnimationUtils
                    .loadAnimation(viewToAnimate.getContext(), R.anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            mLastPosition = position;
        }
    }

    /**
     * Adapter显示尾部
     */
    public void showFooter() {
        notifyItemInserted(getItemCount());
        mShowFooter = true;
    }

    /**
     * Adapter隐藏尾部
     */
    public void hideFooter() {
        notifyItemRemoved(getItemCount() - 1);
        mShowFooter = false;
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void add(int pos, T item) {
        mData.add(pos, item);
        notifyItemInserted(pos);
    }

    public void delete(int pos) {
        mData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addMoreData(List<T> data) {
        int startPos = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(startPos, data.size());
    }

    public List<T> getData() {
        return mData;
    }
}
