package com.example;

import com.example.model.AdapterClass;
import com.example.model.BindAdapterHelp;
import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

@AutoService(Processor.class)
public class BindAdapterProceessor extends AbstractProcessor {
    private Elements elementUtils;
    private Filer mFilder;
    private Messager messager;
    private Map<String, AdapterClass> annotatedClassMap = new HashMap<>();


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    //向编辑器注册注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> type = new LinkedHashSet<>();
        type.add(BindAdapter.class.getCanonicalName());
        return type;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
        messager = processingEnvironment.getMessager();
        mFilder = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        annotatedClassMap.clear();
        try {
            processBindAdapter(roundEnvironment);

        } catch (Exception e) {
            error(e.getMessage());
        }

        for (AdapterClass mClass : annotatedClassMap.values()) {
            try {
                mClass.createAdapterInject().writeTo(mFilder);
                mClass.createAdapter().writeTo(mFilder);
            } catch (IOException e) {
                error(e.getMessage());
            }

        }

        return true;
    }

    private void processBindAdapter(RoundEnvironment roundEnvironment) {
        for (Element e : roundEnvironment.getElementsAnnotatedWith(BindAdapter.class)) {
            BindAdapter adapter = e.getAnnotation(BindAdapter.class);
            AdapterClass mBindAdapterClass = getAdapterClass(e);
            BindAdapterHelp help = new BindAdapterHelp(e, elementUtils);
            mBindAdapterClass.addAdapter(help);
        }

    }

    private AdapterClass getAdapterClass(Element e) {
        TypeElement parents = (TypeElement) e.getEnclosingElement();
        String parentsName = parents.getQualifiedName().toString();
        AdapterClass current = annotatedClassMap.get(parentsName);
        if (current == null) {
            current = new AdapterClass(parents, elementUtils);
            annotatedClassMap.put(parentsName, current);
        }
        return current;
    }

    private void error(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

}
