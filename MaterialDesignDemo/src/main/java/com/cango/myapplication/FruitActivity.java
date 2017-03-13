package com.cango.myapplication;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FruitActivity extends AppCompatActivity {

    public static final String FRUIT_ID="fruit_id";
    public static final String FRUIT_NAME="fruit_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        Intent intent = getIntent();
        int fruitImageId = intent.getIntExtra(FRUIT_ID, 0);
        String fruitName = intent.getStringExtra(FRUIT_NAME);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        ImageView fruitIv= (ImageView) findViewById(R.id.fruit_image_view);
        TextView fruitTv = (TextView) findViewById(R.id.fruit_content);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle(fruitName);
        Glide.with(this).load(fruitImageId).into(fruitIv);
        String fruitContent = getFruitContent(fruitName);
        fruitTv.setText(fruitContent);
    }

    public String getFruitContent(String fruitName) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<500;i++){
            stringBuffer.append(fruitName);
        }
        return stringBuffer.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
