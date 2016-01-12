package com.yushilong.supportfootviewrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        MBRecyclerView recyclerView = (MBRecyclerView) findViewById(R.id.recyclerView);
        adapter = new SimpleAdapter(this, new ArrayList());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnBottomListener(new MBRecyclerView.OnBottomListener() {
            @Override
            public void onBottom() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();
                    }
                }, 2500);
            }
        });
        ((SwipeRefreshLayout) findViewById(R.id.refreshLayout)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                        ((SwipeRefreshLayout) findViewById(R.id.refreshLayout)).setRefreshing(false);
                    }
                }, 2500);
            }
        });
        refresh();
    }

    private void refresh() {
        adapter.reset();
        ArrayList models = new ArrayList();
        for (int i = 1; i < 16; i++) {
            models.add("第" + i + "个item");
        }
        adapter.appendList(models, true, true);
    }

    private void loadMore() {
        ArrayList models = new ArrayList();
        for (int i = 1; i < 16; i++) {
            models.add("第" + (adapter.getModels().size() + i) + "个item");
        }
        adapter.appendList(models, true, false);
    }
}
