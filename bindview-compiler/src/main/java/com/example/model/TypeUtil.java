package com.example.model;

import com.squareup.javapoet.ClassName;

/**
 * Created by ZQZN on 2017/4/14.
 */

public class TypeUtil {
    public static final ClassName FINDER = ClassName.get("com.bindview_api.finder", "Finder");
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");
    public static final ClassName ANDROID_VIEW_ONCLICKLISTENER = ClassName.get("android.view", "View","OnClickListener");
    public static final ClassName INJECTOR = ClassName.get("com.bindview_api", "Injector");
    public static final ClassName ADAPTERINJECTOR = ClassName.get("com.bindview_api", "AdapterInject");
    public static final ClassName CONTEXT = ClassName.get("android.content", "Context");
    public static final ClassName LIST = ClassName.get("java.util", "List");
    public static final ClassName BASE_ADAPTER = ClassName.get("com.bindview_api", "AbRecyclerViewDBAdapter");
    public static final ClassName ONCREATERHOLDVIEW = ClassName.get("com.bindview_api", "BindingViewHolder");
    public static final ClassName VIEW_GROUP = ClassName.get("android.view", "ViewGroup");
    public static final ClassName BR = ClassName.get("com.bindview_api", "BR");




}
