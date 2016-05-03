package ru.spbau.mit.options;

import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ldvsoft on 29.04.16.
 */
public class BenchmarkOptionItemsProcessor extends AbstractProcessor {
    private static final String INTERFACE_BASE_NAME = BenchmarkOption.class.getSimpleName();
    private static final String INTERFACE_NAME = BenchmarkOption.class.getName();
    private static final String PACKAGE_NAME = BenchmarkOption.class.getPackage().getName();
    private static final String GENERATED_BASE_NAME = INTERFACE_BASE_NAME + "Factory";

    private Elements elements;
    private Filer filer;
    private Messager messager;

    private TypeElement interfaceElement;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(BenchmarkOptionItem.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elements = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String, TypeElement> options = new HashMap<>();
        interfaceElement = elements.getTypeElement(INTERFACE_NAME);

        for (Element element : roundEnv.getElementsAnnotatedWith(BenchmarkOptionItem.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                error(element, "Only classes can be annotated with %s", BenchmarkOptionItem.class.getSimpleName());
                return true;
            }
            TypeElement typeElement = (TypeElement) element;
            BenchmarkOptionItem item = element.getAnnotation(BenchmarkOptionItem.class);
            String name = item.name();
            if (name.equals("")) {
                error(
                        element,
                        "Class %s has empty name is %s annotation", typeElement.getQualifiedName(),
                        BenchmarkOptionItem.class.getSimpleName()
                );
                return true;
            }
            if (!isValid(typeElement)) {
                //error printed
                return true;
            }

            options.put(name, typeElement);
        }

        if (options.isEmpty()) {
            return false;
        }

        try {
            TypeName mapTypeName = ParameterizedTypeName.get(
                    ClassName.get(HashMap.class),
                    ClassName.get(String.class),
                    ClassName.get(BenchmarkOption.class)
            );

            CodeBlock.Builder staticBlock = CodeBlock.builder();
            for (Map.Entry<String, TypeElement> entry : options.entrySet()) {
                staticBlock.addStatement("OPTIONS.put($S, new $T())", entry.getKey(), entry.getValue());
            }

            TypeSpec optionsClass = TypeSpec.classBuilder(GENERATED_BASE_NAME)
                    .addField(FieldSpec.builder(mapTypeName,
                            "OPTIONS",
                            Modifier.PUBLIC,
                            Modifier.STATIC,
                            Modifier.FINAL)
                            .initializer(CodeBlock.of("new $T()", mapTypeName))
                            .build())
                    .addStaticBlock(staticBlock.build())
                    .build();

            JavaFile.builder(PACKAGE_NAME, optionsClass)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
            error(null, e.getMessage());
            return true;
        }

        return false;
    }

    private boolean isValid(TypeElement typeElement) {
        if (!typeElement.getModifiers().contains(Modifier.PUBLIC)) {
            error(typeElement, "Class %s must be public", typeElement.getQualifiedName());
            return false;
        }
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            error(typeElement, "Class %s must not be abstract", typeElement.getQualifiedName());
            return false;
        }
        if (!typeElement.getInterfaces().contains(interfaceElement.asType())) {
            error(
                    typeElement,
                    "Class %s mut implement %s interface",
                    typeElement.getQualifiedName(),
                    INTERFACE_NAME
            );
            return false;
        }
        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement executableElement = (ExecutableElement) enclosed;
                if (executableElement.getParameters().size() == 0
                    && executableElement.getModifiers().contains(Modifier.PUBLIC)
                ) {
                    return true;
                }
            }
        }
        error(
                typeElement,
                "Class %s must has public constructor without parameters",
                typeElement.getQualifiedName()
        );
        return false;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
