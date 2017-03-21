package com.cango.mvpdemo;

/**
 * Created by cango on 2017/3/15.
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
