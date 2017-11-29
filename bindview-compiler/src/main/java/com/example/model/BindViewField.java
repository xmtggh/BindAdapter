package com.example.model;

import com.example.BindView;

import javax.crypto.IllegalBlockSizeException;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * 处理成员变量bindview元素的注解
 * Created by ZQZN on 2017/4/14.
 */

public class BindViewField {
    private VariableElement bindedView;
    private int mResId;

    public BindViewField(Element element) throws IllegalArgumentException {
        //判断该元素是否为成员变量 不是则抛出异常
        if (element.getKind()!= ElementKind.FIELD){
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s",BindView.class.getSimpleName()));
        }
        //将元素变成成员变量的类型
        bindedView = (VariableElement)element;
        //从元素中获取注解
        BindView mBindView = bindedView.getAnnotation(BindView.class);
        mResId = mBindView.value();
        if (mResId < 0) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", BindView.class.getSimpleName(),
                            bindedView.getSimpleName()));
        }

    }

    public Name getElementName() {
        return bindedView.getSimpleName();
    }

    public int getResId() {
        return mResId;
    }

    public TypeMirror getTypeMirror() {
        return bindedView.asType();
    }
}
