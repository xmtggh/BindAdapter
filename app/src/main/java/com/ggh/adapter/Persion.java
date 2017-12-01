package com.ggh.adapter;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by ZQZN on 2017/4/17.
 */

public class Persion extends BaseObservable {
    private String name;

    public Persion(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
