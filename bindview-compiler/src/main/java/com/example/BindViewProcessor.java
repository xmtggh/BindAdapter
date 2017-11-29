package com.example;

import com.example.model.AnnotatedClass;
import com.example.model.BindViewField;
import com.example.model.OnClickMethod;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by ZQZN on 2017/4/13.
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {
    /**
     * 使用 Google 的
     * auto-service 库可以自动生成 META-INF/services/javax.annotation.processing.Processor 文件
     */
    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();

        try {
            progressBindView(roundEnv);
            progressBindOnclick(roundEnv);

        } catch (Exception e) {
            error(e.getMessage());
        }

        for (AnnotatedClass cls : mAnnotatedClassMap.values()) {
            try {
                cls.generateFinder().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

    private void progressBindOnclick(RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(OnClick.class)) {
            AnnotatedClass onMethodAnno = getAnnitatedClass(e);
            OnClickMethod method = new OnClickMethod(e);
            onMethodAnno.addMethod(method);
        }
    }


    //支持当前最低版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> type = new LinkedHashSet<>();
        type.add(BindView.class.getCanonicalName());
        type.add(OnClick.class.getCanonicalName());
        return type;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    public void progressBindView(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            AnnotatedClass annotatedClass = getAnnitatedClass(element);
            BindViewField field = new BindViewField(element);
            annotatedClass.addField(field);

        }

    }

    private AnnotatedClass getAnnitatedClass(Element element) {
        //获取父类信息
        TypeElement mTypeElement = (TypeElement) element.getEnclosingElement();
        String parentsName = mTypeElement.getQualifiedName().toString();
        AnnotatedClass ann = mAnnotatedClassMap.get(parentsName);
        if (ann == null) {
            ann = new AnnotatedClass(mTypeElement, mElementUtils);
            mAnnotatedClassMap.put(parentsName, ann);
        }
        return ann;
    }


}
