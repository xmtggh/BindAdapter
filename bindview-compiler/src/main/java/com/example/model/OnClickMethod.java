package com.example.model;

import com.example.OnClick;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

/**
 * 处理方法类注解
 * Created by ZQZN on 2017/4/14.
 */

public class OnClickMethod {
    private Name mMethodName;
    private int[] mResId;

    public Name getmMethodName() {
        return mMethodName;
    }

    public int[] getmResId() {
        return mResId;
    }

    public OnClickMethod(Element element) throws IllegalArgumentException {
        //如果不是成员变量的方法
        if (element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException(String.format("只有方法才可以注解 @%s", OnClick.class.getSimpleName()));
        }
        //强制转换为方法元素
        ExecutableElement executableElement = (ExecutableElement) element;
        //获取元素名称
        mMethodName = executableElement.getSimpleName();
        //获取注解中的值（id）
        mResId = executableElement.getAnnotation(OnClick.class).value();
        //如果id不合法则抛出异常
        for (int id : mResId) {
            if (id < 0) {
                throw new IllegalArgumentException(String.format("您设置的id不合法应为 @%s", OnClick.class.getSimpleName()));
            }
        }
        List<? extends VariableElement> param = executableElement.getParameters();
        if (param.size() > 0) {
            String.format("这个注解 @%s 不应该设置参数", OnClick.class.getSimpleName());
        }
    }
}
