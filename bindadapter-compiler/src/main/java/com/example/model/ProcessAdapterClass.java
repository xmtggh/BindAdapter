package com.example.model;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 每一个注解生成一个adapter
 * Created by Administrator on 2017/4/16 0016.
 */

public class ProcessAdapterClass {
    private TypeElement mTypeElement;
    private List<BindAdapterHelp> mBindAdapterHelpList;
    private Elements elementUtils;
    private List<JavaFile> mFiles ;

    public void addAdapter(BindAdapterHelp adapter) {
        mBindAdapterHelpList.add(adapter);
    }

    public ProcessAdapterClass(TypeElement mTypeElement, Elements elementUtils) {
        this.mTypeElement = mTypeElement;
        this.elementUtils = elementUtils;
        mBindAdapterHelpList = new ArrayList<>();
        mFiles = new ArrayList<>();
    }


    /**
     * 用于生产绑定adapter的模板
     * 1.生成findviewByID 代码
     * 2.recyclerview 绑定 adapter
     */
    /*public JavaFile createAdapterInject() {
               MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("inject");
        injectMethod.addAnnotation(Override.class);
        injectMethod.addModifiers(Modifier.PUBLIC);
        injectMethod.returns(TypeName.VOID);
        injectMethod.addParameter(TypeName.get(mTypeElement.asType()), "host");
        injectMethod.addParameter(TypeName.OBJECT, "source");
        injectMethod.addParameter(ClassTypeUtil.FINDER, "finder");
        injectMethod.addParameter(ParameterizedTypeName.get(ClassTypeUtil.LIST, getClassType()), "data");
        injectMethod.addParameter(TypeName.INT, "id");

        injectMethod.addStatement("host.$N= ($T)(finder.findView(source,$L))", mBindAdapterHelp.getElementName()
                , ClassName.get(mBindAdapterHelp.getTypeMirror()), mBindAdapterHelp.getmResId());
        injectMethod.addStatement("host.$N.setAdapter(new $L(host,data,id))", mBindAdapterHelp.getElementName(), getName() + "Adapter");
        String packName = getPackageName(mTypeElement);
        String clsName = getClassName(mTypeElement, packName);
        ClassName className = ClassName.get(packName, clsName);

        TypeSpec buildClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$AdapterInject")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassTypeUtil.ADAPTERINJECTOR, TypeName.get(mTypeElement.asType()), getClassType()))
                .addMethod(injectMethod.build())
                .build();

        return JavaFile.builder(packName, buildClass).build();

    }*/

    /**
     * 用于生产adapter的模板
     * 1.构造函数的生成
     * 2.onBindViewHolder 的生成
     * 3.onCreateViewHolder 绑定view的生成
     *
     * @return
     */
    public List<JavaFile> createAdapter() {
        for (BindAdapterHelp currentHelp:mBindAdapterHelpList){
            mFiles.add(getJavaFile(currentHelp));
        }
        return mFiles;
    }

    private JavaFile getJavaFile(BindAdapterHelp mBindAdapterHelp) {
        /**
         * onBindViewHolder 方法的构造
         * <code>
         *      @Override
         *      public void onBindViewHolder(BindingViewHolder holder, int position) {
         *      holder.getBinding().setVariable(dataBindingId, getItem(position));;
         *      }
         * </code>
         */
        MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("onBindViewHolder");
        injectMethod.addAnnotation(Override.class);
        injectMethod.addModifiers(Modifier.PUBLIC);
        injectMethod.returns(TypeName.VOID);
        injectMethod.addParameter(ClassTypeUtil.ONCREATERHOLDVIEW, "holder");
        injectMethod.addParameter(TypeName.INT, "position");
        injectMethod.addStatement("holder.getBinding().setVariable(dataBindingId, getItem(position))");
        /**
         * adapter 的构造方法
         * <code>
         *
         *      public PersionAdapter(Context context, List<T> list, int id) {
         *         super(context,list,id);
         *      }
         * </code>
         */
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
        constructor.addModifiers(Modifier.PUBLIC);
        constructor.addParameter(TypeName.get(mTypeElement.asType()), "context");
        constructor.addParameter(ParameterizedTypeName.get(ClassTypeUtil.LIST, getClassType(mBindAdapterHelp)), "list");
        constructor.addParameter(TypeName.INT, "id");
        constructor.addStatement("super(context,list,id)");
        /**
         * 加载item的资源文件
         * <code>
         *
         *      @Override
         *      public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         *      return new BindingViewHolder(inflate(id,parent));
         *      }
         * </code>
         */
        MethodSpec.Builder onCreateViewHolder = MethodSpec.methodBuilder("onCreateViewHolder");
        onCreateViewHolder.returns(ClassTypeUtil.ONCREATERHOLDVIEW);
        onCreateViewHolder.addAnnotation(Override.class);
        onCreateViewHolder.addModifiers(Modifier.PUBLIC);
        onCreateViewHolder.addParameter(ClassTypeUtil.VIEW_GROUP, "parent");
        onCreateViewHolder.addParameter(TypeName.INT, "viewType");
        int layoutId = mBindAdapterHelp.getLayout();
        onCreateViewHolder.addStatement("return new BindingViewHolder(inflate($L,parent))", layoutId);


        String packName = getPackageName(mTypeElement);
        /**
         * 主类的构造
         * <code>
         *
         *      public class {NAME}Adapter extends AbRecyclerViewDBAdapter<T>
         *
         *
         * </code>
         */
        TypeSpec buildClass = TypeSpec.classBuilder(captureName(mBindAdapterHelp.getElementName() + "Adapter"))
                .addModifiers(Modifier.PUBLIC)
                .superclass(ParameterizedTypeName.get(ClassTypeUtil.BASE_ADAPTER, getClassType(mBindAdapterHelp)))
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

    public ClassName getClassType(BindAdapterHelp mBindAdapterHelp) {
        return ClassName.get(mBindAdapterHelp.getaClass().substring(0, mBindAdapterHelp.getaClass().lastIndexOf(".")), mBindAdapterHelp.getaClass().substring(mBindAdapterHelp.getaClass().lastIndexOf(".") + 1, mBindAdapterHelp.getaClass().length()));
    }

    public String getName(BindAdapterHelp mBindAdapterHelp) {
        return mBindAdapterHelp.getaClass().substring(mBindAdapterHelp.getaClass().lastIndexOf(".") + 1, mBindAdapterHelp.getaClass().length());
    }


    /**
     * 首字母大写
     * @param name
     * @return
     */
    public static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return  name;

    }

}
