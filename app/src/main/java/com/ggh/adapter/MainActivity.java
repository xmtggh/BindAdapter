package com.ggh.adapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bindview_api.*;
import com.example.BindAdapter;
import com.example.BindView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView textView;
    @BindAdapter(
            itemId = R.layout.item_layout
            , recyclerViewId = R.id.test_recy
            , type = Persion.class)
    RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.inject(this);
        List<Persion> list = new ArrayList<>();
        Persion persion = new Persion("GGH");
        list.add(persion);
        AdapterInjector.created(this, BR.item, list);
        view.setLayoutManager(new LinearLayoutManager(this));

    }
}
