package com.cango.mvpdemo.api;

import com.cango.mvpdemo.constant.Api;
import com.cango.mvpdemo.model.HttpResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cango on 2017/3/15.
 */

public interface HomeService {
    String BASE_URL= Api.URL_GANK_DOMAN;

    @GET("data/{typeName}/{pageSize}/{pageCount}")
//    Call<List<GankItemData>> gankDataList(@Path("typeName") String typeName, @Path("pageSize") int pageSize, @Path("pageCount") int pageCount);
    Observable<HttpResult> gankDataList(@Path("typeName") String typeName, @Path("pageSize") int pageSize, @Path("pageCount") int pageCount);

}
