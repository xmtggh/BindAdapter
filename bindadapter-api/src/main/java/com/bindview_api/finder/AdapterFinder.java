package com.bindview_api.finder;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public class AdapterFinder implements Finder {

    @Override
    public Context getContext(Object source) {
        return (Activity) source;
    }

    @Override
    public View findView(Object source, int id) {
        return ((Activity) source).findViewById(id);
    }
}
