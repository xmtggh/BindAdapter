package com.bindview_api.finder;

import android.content.Context;
import android.view.View;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午3:51
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface Finder {
    Context getContext(Object source);

    View findView(Object source, int id);

}

