package com.ggh.adapter;

import android.app.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.example.BindAdapter;

/**
 * Created by Administrator on 2017/12/2 0002.
 */

public class TestActivity extends Activity {
    @BindAdapter(itemId = R.layout.item_test_layout,type = Test.class)
    RecyclerView test;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
