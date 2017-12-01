package com.example.model;

import com.example.BindAdapter;
import com.google.auto.common.MoreElements;

import java.io.InputStream;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class BindAdapterHelp {
    private VariableElement classElement;
    private int mResId;
    private int layout;
    private String aClass;
    private AnnotationValue clazzValue;

    public Name getElementName() {
        return classElement.getSimpleName();
    }

    public VariableElement getClassElement() {
        return classElement;
    }

    public String getaClass() {
        return clazzValue.getValue().toString();
    }

    public int getLayout() {
        return layout;
    }

    public int getmResId() {
        return mResId;
    }

    public TypeMirror getTypeMirror() {
        return classElement.asType();
    }

    public BindAdapterHelp(Element element, Elements elementUtils) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s", BindAdapter.class.getSimpleName()));
        }
        //将元素变成成员变量的类型
        classElement = (VariableElement) element;
        //从元素中获取注解
        BindAdapter mBindView = classElement.getAnnotation(BindAdapter.class);
        mResId = mBindView.id();
        layout = mBindView.layout();
        AnnotationMirror svcAnnoMirror = MoreElements.getAnnotationMirror(element, BindAdapter.class).get();

        //无法通过映射获得class，只能通过AnnotationMirror获得
        for (Element getClassElement : elementUtils.getElementValuesWithDefaults(svcAnnoMirror).keySet()) {
            if (getClassElement.asType().toString().equals("()java.lang.Class<?>")) {
                clazzValue = elementUtils.getElementValuesWithDefaults(svcAnnoMirror).get(getClassElement);
            }

        }
//        aClass = mBindView.clazzName().getCanonicalName();
//        aClass = clazz.getCanonicalName();
        if (mResId < 0) {
            throw new IllegalArgumentException(
                    String.format("设置的id无效 !", BindAdapter.class.getSimpleName(),
                            classElement.getSimpleName()));
        }
        if (layout < 0) {
            throw new IllegalArgumentException(
                    String.format("设置的id无效 !", BindAdapter.class.getSimpleName(),
                            classElement.getSimpleName()));
        }

    }
}
