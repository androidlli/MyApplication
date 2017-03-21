package com.cango.mvpdemo.net;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by cango on 2017/3/15.
 */

public class NetManager {

    private static final int DEFAULT_TIMEOUT = 10;
    private static NetManager mNetManager;

    private NetManager(){};

    public static NetManager getInstance(){
        if (mNetManager==null){
            mNetManager=new NetManager();
        }
        return mNetManager;
    }
    public <T> T create(Class<T> service){
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(getBaseUrl(service))
                .build();
        return retrofit.create(service);
    }

    public <T> String getBaseUrl(Class<T> service){
        try {
            Field baseUrlField = service.getField("BASE_URL");
            return (String) baseUrlField.get(service);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    private OkHttpClient getOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        //配置log打印拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        return builder.build();
    }
}
