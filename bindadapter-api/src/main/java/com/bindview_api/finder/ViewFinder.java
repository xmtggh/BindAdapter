package com.bindview_api.finder;

import android.content.Context;
import android.view.View;

public class ViewFinder implements Finder {
    public Context getContext(Object source) {
        return ((View) source).getContext();
    }

    @Override
    public View findView(Object source, int id) {
        return ((View) source).findViewById(id);
    }
}
