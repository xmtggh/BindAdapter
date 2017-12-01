package com.bindview_api;

import android.content.Context;

import com.bindview_api.finder.Finder;

import java.util.List;

/**
 * Created by ZQZN on 2017/4/17.
 */

public interface AdapterInject<T, D> {
    void inject(T host, Object source, Finder finder, List<D> data,int databindingId);
}
