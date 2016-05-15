package com.fightzhao.baseadapterdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.fightzhao.baseadapterdemo.base.BaseRecyclerAdapter;
import com.fightzhao.baseadapterdemo.base.BaseRecyclerViewHolder;
import com.fightzhao.baseadapterdemo.base.BaseSpacesItemDecoration;
import com.fightzhao.baseadapterdemo.callback.OnItemClickListener;
import com.fightzhao.baseadapterdemo.data.NewsSummary;
import com.fightzhao.baseadapterdemo.utils.MeasureUtil;
import com.fightzhao.baseadapterdemo.widget.AutoLoadMoreRecyclerView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private AutoLoadMoreRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseRecyclerAdapter<NewsSummary> mAdapter;
    private ArrayList<NewsSummary> mDatas;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
        if(savedInstanceState!=null){
            savedInstanceState.getBundle("key");
        }
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(MainActivity.this, "单击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(MainActivity.this, "长安", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTextViewClick(View view, int position) {
                Toast.makeText(MainActivity.this, "标题", Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setOnLoadMoreListener(new AutoLoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                // 状态停止，并且滑动到最后一位
                // TODO: 16/5/10 加载更多
                new GetBoomData().execute();
                // 显示尾部加载
                mAdapter.showFooter();
                mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
            }
        });

    }

    private void initData() {
        mDatas = new ArrayList<>();
        mDatas.add(new NewsSummary("IS宣称对叙利亚爆炸案负责", "极端组织“伊斯兰国”31日在社交媒体上宣称，该组织制造了当天在叙利亚首都大马士革发生的连环爆炸案。"));
        mDatas.add(new NewsSummary("苹果因漏电召回部分电源插头", "昨天记者了解到，苹果公司在其官网发出交流电源插头转换器更换计划，召回部分可能存在漏电风险的电源插头。"));
        mDatas.add(new NewsSummary("航拍“摩托大军”返乡高峰 如蚂蚁搬家（组图)", "月30日，距离春节还有不到十天，“摩托大军”返乡高峰到来。航拍广西梧州市东出口服务站附近的骑行返乡人员，如同蚂蚁搬家一般。"));
        mDatas.add(new NewsSummary("航拍“摩托大军”返乡高峰 如蚂蚁搬家（组图)", "月30日，距离春节还有不到十天，“摩托大军”返乡高峰到来。航拍广西梧州市东出口服务站附近的骑行返乡人员，如同蚂蚁搬家一般。"));
        mDatas.add(new NewsSummary("航拍“摩托大军”返乡高峰 如蚂蚁搬家（组图)", "月30日，距离春节还有不到十天，“摩托大军”返乡高峰到来。航拍广西梧州市东出口服务站附近的骑行返乡人员，如同蚂蚁搬家一般。"));

    }

    private void initView() {
        mRecyclerView = (AutoLoadMoreRecyclerView) findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        mAdapter = new BaseRecyclerAdapter<NewsSummary>(MainActivity.this, mDatas) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_home;
            }

            @Override
            public void bindData(BaseRecyclerViewHolder holder, int position, NewsSummary item) {
                holder.getTextView(R.id.tv_title).setText(item.title);
                holder.getTextView(R.id.tv_content).setText(item.content);
            }
        };

        /**
         * 设置样式,分割线,动画
         */

        // 垂直布局
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);

        //列数为两列
        int spanCount = 2;

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                spanCount,
                StaggeredGridLayoutManager.VERTICAL);





        mRecyclerView.setAutoLayoutManager(staggeredGridLayoutManager).setAutoHasFixedSize(true)
                .addAutoItemDecoration(
                        new BaseSpacesItemDecoration(MeasureUtil.dip2px(this, 4)))
                .setAutoItemAnimator(new DefaultItemAnimator()).setAutoAdapter(mAdapter);

    }

    @Override
    public void onRefresh() {
        Toast.makeText(MainActivity.this, "正在刷新哦", Toast.LENGTH_SHORT).show();
        new GetTopData().execute();

    }

    private class GetTopData extends AsyncTask<Void, Void, ArrayList<NewsSummary>> {

        @Override
        protected ArrayList<NewsSummary> doInBackground(Void... params) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsSummary> newsSummaries) {
            mDatas.add(0, new NewsSummary("骑士获得NBA总冠军", "詹姆斯,欧文,乐福狂砍100分获得史上最大分数差!!!"));
            mDatas.add(1, new NewsSummary("骑士获得NBA总冠军", "詹姆斯,欧文,乐福狂砍100分获得史上最大分数差!!!"));
            mAdapter.setData(mDatas);
            mSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(newsSummaries);
        }
    }

    private class GetBoomData extends AsyncTask<Void, Void, ArrayList<NewsSummary>> {

        @Override
        protected ArrayList<NewsSummary> doInBackground(Void... params) {
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return mDatas;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsSummary> newsSummaries) {
            /**
             * 1.数据加载
             * 2.隐藏底部
             * 3.通知RecyclerView状态
             */
            mAdapter.add(mDatas.size(), new NewsSummary("底部数据加载1", "詹姆斯,欧文,乐福狂砍100分获得史上最大分数差!!!"));
            mAdapter.add(mDatas.size(), new NewsSummary("底部数据加载2", "詹姆斯,欧文,乐福狂砍100分获得史上最大分数差!!!"));
            mAdapter.hideFooter();
            mRecyclerView.notifyMoreLoaded();

            super.onPostExecute(newsSummaries);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
