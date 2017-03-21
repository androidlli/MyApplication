package com.cango.mvpdemo.homes;

import com.cango.mvpdemo.BasePresenter;
import com.cango.mvpdemo.BaseView;
import com.cango.mvpdemo.model.GankItemData;

import java.util.List;

/**
 * Created by cango on 2017/3/15.
 */

public interface HomeContract {

    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        void showDatas(List<GankItemData> gankItemDataList);
        void showNoDatas();
        void showDatasError();
        void showDataDetailUi(String dataId);
        boolean isActive();
    }

    interface Presenter extends BasePresenter{
        void loadDatas(boolean showLoadingUi,int pageSize,int pageCount);
        void openDataDetails();
    }
}
