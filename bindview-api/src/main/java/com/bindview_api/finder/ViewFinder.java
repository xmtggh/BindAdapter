package com.bindview_api.finder;

import android.content.Context;
import android.view.View;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午4:19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ViewFinder implements Finder {
    public Context getContext(Object source) {
        return ((View) source).getContext();
    }

    @Override
    public View findView(Object source, int id) {
        return ((View) source).findViewById(id);
    }
}
