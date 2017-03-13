package com.cango.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cango.myapplication.FruitActivity;
import com.cango.myapplication.R;
import com.cango.myapplication.bean.Fruit;

import java.util.List;

/**
 * Created by cango on 2017/3/13.
 */

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitHolder> {
    private Context mContext;
    private List<Fruit> fruitList;
    public FruitAdapter(List<Fruit> fruitList){
        this.fruitList=fruitList;
    }
    @Override
    public FruitAdapter.FruitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null)
            mContext=parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item, parent, false);
        final FruitHolder fruitHolder=new FruitHolder(view);
        fruitHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = fruitHolder.getAdapterPosition();
                Fruit fruit = fruitList.get(adapterPosition);
                Intent intent=new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_ID,fruit.getId());
                intent.putExtra(FruitActivity.FRUIT_NAME,fruit.getName());
                mContext.startActivity(intent);
            }
        });
        return fruitHolder;
    }

    @Override
    public void onBindViewHolder(FruitAdapter.FruitHolder holder, int position) {
        Fruit fruit = fruitList.get(position);
        holder.fruitName.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getId()).into(holder.fruitImage);
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }
    static class FruitHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView fruitImage;
        TextView fruitName;
        public FruitHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView;
            fruitImage= (ImageView) itemView.findViewById(R.id.fruit_image);
            fruitName= (TextView) itemView.findViewById(R.id.fruit_name);
        }
    }
}
