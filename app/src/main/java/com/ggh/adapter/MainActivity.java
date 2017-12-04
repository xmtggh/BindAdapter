package com.ggh.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.BindAdapter;
import com.ggh.adapter.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @BindAdapter(itemId = R.layout.item_layout, type = Persion.class)
    RecyclerView nameList;

    @BindAdapter(itemId = R.layout.item_layout, type = Persion.class)
    RecyclerView testList;

    ActivityMainBinding mainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main);
        List<Persion> list = new ArrayList<>();
        Persion persion = new Persion("GGH");
        list.add(persion);
        NameListAdapter mNameListAdapter = new NameListAdapter(MainActivity.this,list,BR.item);
        mainBinding.recy.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mainBinding.recy.setAdapter(mNameListAdapter);
    }
}
