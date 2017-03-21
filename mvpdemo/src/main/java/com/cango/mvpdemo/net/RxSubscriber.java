package com.cango.mvpdemo.net;

import android.content.Context;
import android.widget.Toast;

import com.cango.mvpdemo.MtApplication;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by cango on 2017/3/17.
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {
    private Context mContext;
    public RxSubscriber() {
        mContext= MtApplication.getContext();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        //统一处理请求异常的情况
        if (e instanceof IOException) {
            Toast.makeText(mContext, "网络链接异常", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        _onError();
    }

    @Override
    public void onNext(T o) {
        _onNext(o);
    }
    protected abstract void _onNext(T o);
    protected abstract void _onError();
}
