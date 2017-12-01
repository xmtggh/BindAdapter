package com.bindview_api;

import android.app.Activity;

import com.bindview_api.finder.AdapterFinder;
import com.bindview_api.finder.Finder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZQZN on 2017/4/17.
 */

public class BindingAdapterUtil {
    private static final AdapterFinder Adapter_FINDER = new AdapterFinder();
    private static final Map<String, AdapterInject> ADAPTER_MAP = new HashMap<>();

    public static <T> void created(Activity activity, int id, List<T> data) {
        inject(activity, activity, Adapter_FINDER, data, id);
    }

    public static <T> void inject(Object host, Object source, Finder finder, List<T> data, int dbId) {
        String className = host.getClass().getName();
        try {
            AdapterInject injector = ADAPTER_MAP.get(className);
            if (injector == null) {
                Class<?> finderClass = Class.forName(className + "$$AdapterInject");
                injector = (AdapterInject) finderClass.newInstance();
                ADAPTER_MAP.put(className, injector);
            }
            injector.inject(host, source, finder, data, dbId);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }
}
