package com.chiclaim.butterknife.processor;

import com.chiclaim.butterknife.annotation.BindView;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.naming.Binding;
import javax.tools.JavaFileObject;

import static com.chiclaim.butterknife.processor.ViewBinding.createViewBind;
import static javafx.scene.input.KeyCode.T;

@SupportedAnnotationTypes("com.chiclaim.butterknife.annotation.BindView")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ButterKnifeProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(BindView.class);
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //printRoundEnvironment(roundEnv);
        System.out.println("=======================process()");
        parseRoundEnvironment(roundEnv);
        return true;
    }


    private void printSet(Set<? extends TypeElement> annotations) {
        for (TypeElement element : annotations) {
            printValue(element.getSimpleName());
        }
    }

    /**
     * just test
     */
    private void printRoundEnvironment(RoundEnvironment roundEnv) {
        Set<? extends Element> as = roundEnv.getElementsAnnotatedWith(BindView.class);
        for (Element element : as) {

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            printValue("========annotation 所在的类完整名称 " + enclosingElement.getQualifiedName());
            printValue("========annotation 所在类的类名 " + enclosingElement.getSimpleName());
            printValue("========annotation 所在类的父类 " + enclosingElement.getSuperclass());
            printValue("========element name " + element.getSimpleName());
            printValue("========element type " + element.asType());
            printValue("========annotation value " + element.getAnnotation(BindView.class).value());

            printValue("\n");
        }

        MethodSpec flux = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "greeting")
                .addStatement("this.$N = $N", "greeting", "greeting")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC)
                .addField(String.class, "greeting", Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(flux)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
                .build();

        try {
            //javaFile.writeTo(System.out);
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void printValue(Object obj) {
        System.out.println(obj);
    }


    public static void parseRoundEnvironment(RoundEnvironment roundEnv) {

        Map<TypeElement, BindClass> map = new LinkedHashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            //所在类名
            //String className = enclosingElement.getSimpleName().toString();
            //所在类全名
            //String qualifiedName = enclosingElement.getQualifiedName().toString();
            //注解的值
            int annotationValue = element.getAnnotation(BindView.class).value();

            BindClass bindClass = map.get(enclosingElement);
            if (bindClass == null) {
                bindClass = BindClass.createBindClass(enclosingElement);
                map.put(enclosingElement, bindClass);
            }
            String name = element.getSimpleName().toString();
            TypeName type = TypeName.get(enclosingElement.asType());
            ViewBinding viewBinding = ViewBinding.createViewBind(name, type, annotationValue);
            bindClass.addAnnotationField(viewBinding);
        }


        for (Map.Entry<TypeElement, BindClass> entry : map.entrySet()) {
            printValue("==========" + entry.getValue().getBindingClassName());
        }
    }


    //注解所在的类                 【element.getEnclosingElement()】
    //注解上的值, 用于findViewById 【element.getAnnotation(BindView.class).value()】
    //注解字段的类型,用于强转       【enclosingElement.asType()】


    //1,类模板
    // butterKnife是 T extends SimpleActivity 主要是为了兼容继承的个功能。
    /*
    public class MainActivity_Binding {
        public MainActivity_Binding(MainActivity target,View view) {
            target.text = (TextView)view.findViewById(id);
        }
    }
    */


}
