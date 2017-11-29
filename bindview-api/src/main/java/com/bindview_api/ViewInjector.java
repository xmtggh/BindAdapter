package com.bindview_api;

import android.app.Activity;
import android.view.View;


import com.bindview_api.finder.ActivityFinder;
import com.bindview_api.finder.Finder;
import com.bindview_api.finder.ViewFinder;

import java.util.HashMap;
import java.util.Map;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午4:25
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class ViewInjector {

    private static final ActivityFinder ACTIVITY_FINDER = new ActivityFinder();
    private static final ViewFinder VIEW_FINDER = new ViewFinder();
    private static final Map<String, Injector> FINDER_MAP = new HashMap<>();

    public static void inject(Activity activity) {
        inject(activity, activity, ACTIVITY_FINDER);
    }

    public static void inject(View view) {
        inject(view, view);
    }

    public static void inject(Object host, View view) {
        // for fragment
        inject(host, view, VIEW_FINDER);
    }

    public static void inject(Object host, Object source, Finder finder) {
        String className = host.getClass().getName();
        try {
            Injector injector = FINDER_MAP.get(className);
            if (injector == null) {
                Class<?> finderClass = Class.forName(className + "$$Injector");
                injector = (Injector) finderClass.newInstance();
                FINDER_MAP.put(className, injector);
            }
            injector.inject(host, source, finder);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }

}
