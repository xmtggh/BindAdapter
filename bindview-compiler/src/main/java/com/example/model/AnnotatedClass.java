package com.example.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by ZQZN on 2017/4/14.
 */

public class AnnotatedClass {
    private TypeElement mClassElement;//类的元素
    private List<BindViewField> mBindViewFieldList;
    private List<OnClickMethod> methodList;
    public Elements mElementUtils;

    public AnnotatedClass(TypeElement mClassElement, Elements mElementUtils) {
        this.mClassElement = mClassElement;
        this.mBindViewFieldList = new ArrayList<>();
        this.methodList = new ArrayList<>();
        this.mElementUtils = mElementUtils;
    }

    public void addField(BindViewField field) {
        mBindViewFieldList.add(field);
    }

    public void addMethod(OnClickMethod method) {
        methodList.add(method);

    }

    public JavaFile generateFinder() {
        MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("inject");
        injectMethod.addAnnotation(Override.class);
        injectMethod.addModifiers(Modifier.PUBLIC);
        injectMethod.addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL);
        injectMethod.addParameter(TypeName.OBJECT, "source");
        injectMethod.addParameter(TypeUtil.FINDER, "finder");

        for (BindViewField viewField : mBindViewFieldList) {
//            injectMethod.addStatement("host.$A= ($B)finder.findView(source,$C)", viewField.getElementName(), ClassName.get(viewField.getTypeMirror()), viewField.getResId());
            injectMethod.addStatement("host.$N= ($T)(finder.findView(source,$L))", viewField.getElementName()
                    , ClassName.get(viewField.getTypeMirror()), viewField.getResId());
        }

        if (methodList.size() > 0) {
            injectMethod.addStatement("$A listener", TypeUtil.ANDROID_VIEW);
        }
        for (OnClickMethod method : methodList) {
            TypeSpec.Builder typeSpec = TypeSpec.anonymousClassBuilder("");
            typeSpec.addSuperinterface(TypeUtil.ANDROID_VIEW_ONCLICKLISTENER);
            typeSpec.addMethod(MethodSpec.methodBuilder("onClick")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(TypeUtil.ANDROID_VIEW, "view")
                    .returns(TypeName.VOID)
                    .addStatement("host.$L", method.getmMethodName()).build())
                    .build();
            injectMethod.addStatement("listener = $W", typeSpec);
            for (int id : method.getmResId()) {
                if (id > 0) {
                    injectMethod.addStatement("finder.findView(source,$Q).setOnClickListener(listener)", id);
                }
            }


        }

        String packName = getPackageName(mClassElement);
        String clsName = getClassName(mClassElement, packName);
        ClassName className = ClassName.get(packName, clsName);

        TypeSpec buildClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$Injector")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.INJECTOR, TypeName.get(mClassElement.asType())))
                .addMethod(injectMethod.build())
                .build();
        return JavaFile.builder(packName, buildClass).build();
    }

    private String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }
}
