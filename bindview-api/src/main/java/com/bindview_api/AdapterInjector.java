package com.bindview_api;

import android.app.Activity;
import android.content.Context;

import com.bindview_api.finder.ActivityFinder;
import com.bindview_api.finder.Finder;
import com.bindview_api.finder.ViewFinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZQZN on 2017/4/17.
 */

public class AdapterInjector {
    private static final ActivityFinder ACTIVITY_FINDER = new ActivityFinder();
    private static final ViewFinder VIEW_FINDER = new ViewFinder();
    private static final Map<String, AdapterInject> ADAPTER_MAP = new HashMap<>();

    public static <T> void created(Activity activity, List<T> data) {
        inject(activity, activity, ACTIVITY_FINDER, data);
    }

    public static <T> void inject(Object host, Object source, Finder finder, List<T> data) {
        String className = host.getClass().getName();
        try {
            AdapterInject injector = ADAPTER_MAP.get(className);
            if (injector == null) {
                Class<?> finderClass = Class.forName(className + "$$AdapterInject");
                injector = (AdapterInject) finderClass.newInstance();
                ADAPTER_MAP.put(className, injector);
            }
            injector.inject(host, source, finder, data);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }
}
