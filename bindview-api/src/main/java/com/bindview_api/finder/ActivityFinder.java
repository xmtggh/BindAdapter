package com.bindview_api.finder;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午4:17
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ActivityFinder implements Finder {

    @Override
    public Context getContext(Object source) {
        return (Activity) source;
    }

    @Override
    public View findView(Object source, int id) {
        return ((Activity) source).findViewById(id);
    }
}
