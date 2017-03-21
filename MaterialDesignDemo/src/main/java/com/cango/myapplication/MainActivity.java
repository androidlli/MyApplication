package com.cango.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cango.myapplication.adapter.FruitAdapter;
import com.cango.myapplication.bean.Fruit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private NavigationView mNav;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private FruitAdapter mFruitAdapter;
    private List<Fruit> fruitList=new ArrayList<>();
    private Fruit[] fruits={new Fruit(R.drawable.apple,"apple"),new Fruit(R.drawable.coconut,"coconut"),
                            new Fruit(R.drawable.kiwifruit,"kiwifruit"),new Fruit(R.drawable.orange,"orange"),
                            new Fruit(R.drawable.pear,"pear"),new Fruit(R.drawable.watermelon,"watermelon")
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initOnClick();
    }

    private void initView() {
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        mNav= (NavigationView) findViewById(R.id.design_navigation_view);
        mDrawerLayout= (DrawerLayout) findViewById(R.id.drawerlayout);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
//        if (supportActionBar!=null){
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//            supportActionBar.setHomeAsUpIndicator(R.drawable.menu);
//        }

        //toolbar右边的三条线的动画
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        //设置左上角显示三道横线
        toggle.syncState();

        initFruits();
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        mFruitAdapter=new FruitAdapter(fruitList);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mFruitAdapter);
    }

    private void initFruits() {
        fruitList.clear();
        for (int i=0;i<50;i++){
            Random random=new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }

    private void initOnClick() {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.backup:
                break;
            case R.id.delete:
                break;
            case R.id.settings:
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}
