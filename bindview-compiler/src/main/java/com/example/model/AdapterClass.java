package com.example.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Parameterizable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 每一个注解生成一个adapter
 * Created by Administrator on 2017/4/16 0016.
 */

public class AdapterClass {
    private TypeElement mTypeElement;
    private BindAdapterHelp mBindAdapter;
    private Elements elementUtils;

    public void addAdapter(BindAdapterHelp adapter) {
        mBindAdapter = adapter;
    }

    public AdapterClass(TypeElement mTypeElement, Elements elementUtils) {
        this.mTypeElement = mTypeElement;
        this.elementUtils = elementUtils;
    }


    public JavaFile createAdapterInject() {
        MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("inject");
        injectMethod.addAnnotation(Override.class);
        injectMethod.addModifiers(Modifier.PUBLIC);
        injectMethod.returns(TypeName.VOID);
        injectMethod.addParameter(TypeName.get(mTypeElement.asType()), "host");
        injectMethod.addParameter(TypeName.OBJECT, "source");
        injectMethod.addParameter(TypeUtil.FINDER, "finder");
        injectMethod.addParameter(ParameterizedTypeName.get(TypeUtil.LIST, getClassType()), "data");
        injectMethod.addParameter(TypeName.INT,"id");
        injectMethod.addStatement("host.$N= ($T)(finder.findView(source,$L))", mBindAdapter.getElementName()
                , ClassName.get(mBindAdapter.getTypeMirror()), mBindAdapter.getmResId());
        injectMethod.addStatement("host.$N.setAdapter(new $L(host,data,id))", mBindAdapter.getElementName(), getName() + "Adapter");

        String packName = getPackageName(mTypeElement);
        String clsName = getClassName(mTypeElement, packName);
        ClassName className = ClassName.get(packName, clsName);

        TypeSpec buildClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$AdapterInject")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.ADAPTERINJECTOR, TypeName.get(mTypeElement.asType()), getClassType()))
                .addMethod(injectMethod.build())
                .build();
        return JavaFile.builder(packName, buildClass).build();

    }

    public JavaFile createAdapter() {
        MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("onBindViewHolder");
        injectMethod.addAnnotation(Override.class);
        injectMethod.addModifiers(Modifier.PUBLIC);
        injectMethod.returns(TypeName.VOID);
        injectMethod.addParameter(TypeUtil.ONCREATERHOLDVIEW, "holder");
        injectMethod.addParameter(TypeName.INT, "position");
//        injectMethod.addStatement("holder.getBinding().setVariable($W.item, getItem(position))",TypeUtil.BR);
//        injectMethod.addStatement("")
        injectMethod.addStatement("holder.getBinding().setVariable(dataBindingId, getItem(position));");
//        injectMethod.addCode("holder.getBinding().setVariable(BR.item, getItem(position));");

        //添加构造方法
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
        constructor.addModifiers(Modifier.PUBLIC);
        constructor.addParameter(TypeName.get(mTypeElement.asType()), "context");
        constructor.addParameter(ParameterizedTypeName.get(TypeUtil.LIST, getClassType()), "list");
        constructor.addParameter(TypeName.INT, "id");
        constructor.addStatement("super(context,list,id)");
//        constructor.addCode("super(context,list)");
        //绑定item layout
        MethodSpec.Builder onCreateViewHolder = MethodSpec.methodBuilder("onCreateViewHolder");
        onCreateViewHolder.returns(TypeUtil.ONCREATERHOLDVIEW);
        onCreateViewHolder.addAnnotation(Override.class);
        onCreateViewHolder.addModifiers(Modifier.PUBLIC);
        onCreateViewHolder.addParameter(TypeUtil.VIEW_GROUP, "parent");
        onCreateViewHolder.addParameter(TypeName.INT, "viewType");
        int layoutId = mBindAdapter.getLayout();
//        onCreateViewHolder.addStatement("return new BindingViewHolder(inflate(" + layoutId + ",parent))");
        onCreateViewHolder.addStatement("return new BindingViewHolder(inflate($L,parent))", layoutId);


        String packName = getPackageName(mTypeElement);
        String clsName = getClassName(mTypeElement, packName);
        ClassName className = ClassName.get(packName, clsName);

        TypeSpec buildClass = TypeSpec.classBuilder(getName() + "Adapter")
                .addModifiers(Modifier.PUBLIC)
//                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.ADAPTERINJECTOR, TypeName.get(mTypeElement.asType()), getClassType()))
                .superclass(ParameterizedTypeName.get(TypeUtil.BASE_ADAPTER, getClassType()))
                .addMethod(constructor.build())
                .addMethod(injectMethod.build())
                .addMethod(onCreateViewHolder.build())
                .build();
        return JavaFile.builder(packName, buildClass).build();

    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    public ClassName getClassType() {
        return ClassName.get(mBindAdapter.getaClass().substring(0, mBindAdapter.getaClass().lastIndexOf(".")), mBindAdapter.getaClass().substring(mBindAdapter.getaClass().lastIndexOf(".") + 1, mBindAdapter.getaClass().length()));
    }

    public String getName() {
        return mBindAdapter.getaClass().substring(mBindAdapter.getaClass().lastIndexOf(".") + 1, mBindAdapter.getaClass().length());
    }


}
