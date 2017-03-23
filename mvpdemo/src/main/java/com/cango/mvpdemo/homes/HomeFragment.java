package com.cango.mvpdemo.homes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cango.mvpdemo.R;
import com.cango.mvpdemo.baseAdapter.BaseHolder;
import com.cango.mvpdemo.baseAdapter.OnBaseItemClickListener;
import com.cango.mvpdemo.baseAdapter.OnLoadMoreListener;
import com.cango.mvpdemo.model.GankItemData;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.LinearLayoutManager.*;

/**
 * 主页fragment
 */
public class HomeFragment extends Fragment implements HomeContract.View ,SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.home_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.home_recyclerView)
    RecyclerView mRecyclerView;
    private int mPageCount=1;
    static int PAGE_SIZE=10;
    private int mTempPageCount=2;
    /**
     * 是否是加载更多
     */
    private boolean isLoadMore;
    private HomeContract.Presenter mPresenter;
    HomeAdapter mHomeAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorAccent,R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mHomeAdapter=new HomeAdapter(getActivity(),new ArrayList<GankItemData>(),true);
        mHomeAdapter.setLoadingView(R.layout.load_loading_layout);
        mHomeAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                if (mPageCount == mTempPageCount && !isReload) {
                    return;
                }
                isLoadMore = true;
                mPageCount = mTempPageCount;
                mPresenter.loadDatas(true,PAGE_SIZE,mPageCount);
            }
        });
        mHomeAdapter.setOnItemClickListener(new OnBaseItemClickListener<GankItemData>() {
            @Override
            public void onItemClick(BaseHolder viewHolder, GankItemData data, int position) {
                Intent intent = new Intent(getActivity(), null);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        viewHolder.getView(R.id.item_home_icon), "shareView");
                ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
            }
        });
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mHomeAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setLoadingIndicator(true);
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        if (presenter != null)
            mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showDatas(List<GankItemData> gankItemDataList) {
        if (isLoadMore){
            if (gankItemDataList.size()==0){
                mHomeAdapter.setLoadEndView(R.layout.load_end_layout);
            }else {
                mHomeAdapter.setLoadMoreData(gankItemDataList);
                mTempPageCount++;
            }
        }else {
            mHomeAdapter.setNewData(gankItemDataList);
            setLoadingIndicator(false);
        }
    }

    @Override
    public void showNoDatas() {
        if (isLoadMore){
        }else {
            setLoadingIndicator(false);
        }
        mHomeAdapter.setLoadEndView(R.layout.load_end_layout);
    }

    @Override
    public void showDatasError() {
        if (isLoadMore){
            mHomeAdapter.setLoadFailedView(R.layout.load_failed_layout);
        }else {
            setLoadingIndicator(false);
        }
    }

    @Override
    public void showDataDetailUi(String dataId) {

    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void onRefresh() {
        isLoadMore=false;
        mPageCount=1;
        mTempPageCount=2;
        mPresenter.loadDatas(true,PAGE_SIZE,mPageCount);
    }
}
