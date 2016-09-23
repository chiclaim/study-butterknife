package com.chiclaim.butterknife.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * 保存注解上的相关信息,用于生成Binding类
 * Created by chiclaim on 2016/09/23
 */
final class BindClass {

    private TypeName targetTypeName;
    private ClassName bindingClassName;
    private boolean isFinal;
    /**
     * 一个类中可能有多处使用了注解
     */
    private List<ViewBinding> fields;

    private BindClass(TypeElement enclosingElement) {
        //asType 表示注解所在字段是什么类型(eg. Button TextView)
        TypeName targetType = TypeName.get(enclosingElement.asType());
        if (targetType instanceof ParameterizedTypeName) {
            targetType = ((ParameterizedTypeName) targetType).rawType;
        }
        String packageName = enclosingElement.getQualifiedName().toString();
        packageName = packageName.substring(0, packageName.lastIndexOf("."));
        //String className = enclosingElement.getQualifiedName().toString().substring(packageName.length() + 1).replace('.', '$');
        String className = enclosingElement.getSimpleName().toString();
        ClassName bindingClassName = ClassName.get(packageName, className + "_ViewBinding");

        boolean isFinal = enclosingElement.getModifiers().contains(Modifier.FINAL);
        this.targetTypeName = targetType;
        this.bindingClassName = bindingClassName;
        this.isFinal = isFinal;
        fields = new ArrayList<>();
    }

    static BindClass createBindClass(TypeElement enclosingElement) {
        return new BindClass(enclosingElement);
    }

    ClassName getBindingClassName() {
        return bindingClassName;
    }

    public void setBindingClassName(ClassName bindingClassName) {
        this.bindingClassName = bindingClassName;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public TypeName getTargetTypeName() {
        return targetTypeName;
    }

    public void setTargetTypeName(TypeName targetTypeName) {
        this.targetTypeName = targetTypeName;
    }

    public Collection<ViewBinding> getFields() {
        return fields;
    }

    public void setFields(List<ViewBinding> fields) {
        this.fields = fields;
    }


    void addAnnotationField(ViewBinding viewBinding) {
        fields.add(viewBinding);
    }

}
