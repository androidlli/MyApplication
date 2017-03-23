package com.cango.mvpdemo.homes;

import com.cango.mvpdemo.api.HomeService;
import com.cango.mvpdemo.model.GankItemData;
import com.cango.mvpdemo.model.HttpResult;
import com.cango.mvpdemo.net.NetManager;
import com.cango.mvpdemo.net.RxSubscriber;
import com.cango.mvpdemo.util.CommUtil;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cango on 2017/3/15.
 */

public class HomePresenter implements HomeContract.Presenter {

    private HomeService mHomeService;
    private HomeContract.View mHomeView;

    public HomePresenter(HomeContract.View homeView) {
        CommUtil.checkNotNull(homeView, "homeView cannot be null!");
        mHomeView = homeView;
        mHomeView.setPresenter(this);
        mHomeService = NetManager.getInstance().create(HomeService.class);
    }

    @Override
    public void start() {
        loadDatas(true,10,1);
    }

    @Override
    public void loadDatas(boolean showLoadingUi,int pageSize,int pageCount) {
        Logger.d("pageCount = "+pageCount);
        loadDatas(true, showLoadingUi,pageSize,pageCount);
    }

    /**
     * @param froceUpdate
     * @param showLoadingUi 只是是否显示下拉刷新动画,我觉得还是在fragment触发动作的时候处理，不应再presenter进行操作view
     * @param pageSize
     * @param pageCount
     */
    private void loadDatas(boolean froceUpdate, final boolean showLoadingUi, int pageSize, int pageCount) {
        mHomeService.gankDataList("Android",pageSize,pageCount)
                .subscribeOn(Schedulers.io())
                .map(new Func1<HttpResult, List<GankItemData>>() {
                    @Override
                    public List<GankItemData> call(HttpResult httpResult) {
                        return httpResult.getResults();
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        if (showLoadingUi) mHomeView.setLoadingIndicator(true);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<List<GankItemData>>() {
                    @Override
                    protected void _onNext(List<GankItemData> gankItemDatas) {
//                        if (showLoadingUi) mHomeView.setLoadingIndicator(false);
                        if (gankItemDatas!=null){
                            if (gankItemDatas.size()<10)mHomeView.showNoDatas();
                            else mHomeView.showDatas(gankItemDatas);
                        }
                    }

                    @Override
                    protected void _onError() {
//                        if (showLoadingUi)mHomeView.setLoadingIndicator(false);
                        mHomeView.showDatasError();
                    }
                });


    }

    @Override
    public void openDataDetails() {

    }
}
