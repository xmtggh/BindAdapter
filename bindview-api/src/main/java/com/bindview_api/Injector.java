package com.bindview_api;


import com.bindview_api.finder.Finder;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/9/5-下午4:21
 * 描    述：
 * 修订历史：
 * ================================================
 */
public interface Injector<T> {

    void inject(T host, Object source, Finder finder);

}
