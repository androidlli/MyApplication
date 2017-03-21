package com.cango.mvpdemo.homes;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cango.mvpdemo.R;
import com.cango.mvpdemo.baseAdapter.BaseAdapter;
import com.cango.mvpdemo.baseAdapter.BaseHolder;
import com.cango.mvpdemo.model.GankItemData;
import com.cango.mvpdemo.util.CommUtil;

import java.util.List;

/**
 * Created by cango on 2017/3/20.
 */

public class HomeAdapter extends BaseAdapter<GankItemData> {


    public HomeAdapter(Context context, List<GankItemData> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_home;
    }

    @Override
    protected void convert(BaseHolder holder, GankItemData data) {
        ImageView iconIv = holder.getView(R.id.item_home_icon);
        TextView descTv = holder.getView(R.id.item_home_desc);
        TextView whoTv = holder.getView(R.id.item_home_who);
        List<String> urlHost = data.getImages();
        if (CommUtil.checkIsNotNull(urlHost)) {

        } else {
            Glide.with(mContext)
                    .load(data.getImages().get(0) + "?imageView2/0/w/100")
                    .into(iconIv);
        }
        String desc = CommUtil.checkIsNotNull(data.getDesc()) ? "" : data.getDesc();
        descTv.setText(desc);
        String who = CommUtil.checkIsNotNull(data.getWho()) ? "" : data.getWho();
        whoTv.setText(who);
    }

}
