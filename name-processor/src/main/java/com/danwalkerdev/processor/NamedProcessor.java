package com.danwalkerdev.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.danwalkerdev.processor.Named")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class NamedProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamedProcessor.class);
    private static final String CLASS_NAME = "NamedFactory";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            // one of the other rounds?
            return false;
        }
        if (annotations.size() > 1) {
            LOGGER.info("Only a single annotation is supported but found more than 1");
            return false;
        }

        TypeElement element = annotations.iterator().next();
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(element);

        Map<Boolean, List<Element>> annotatedPartition = annotatedElements.stream()
                .collect(Collectors.partitioningBy(e -> e.getKind() == ElementKind.ENUM));

        for (Element e : annotatedPartition.get(false)) {
            LOGGER.warn(e.getSimpleName() + ": is not an enum but @Named annotation must be placed on an enum");
        }

        write(annotatedPartition.get(true));
        return true;
    }

    private void write(Collection<Element> elements) {
        if (elements.isEmpty()) {
            LOGGER.debug("No applicable classes annotated with @Named");
            return;
        }

        try {
            doWrite(elements);
        } catch (IOException e) {
            LOGGER.error("Unable to write DescriptionType source file. Compilation will likely fail...");
        }

    }

    private void doWrite(Collection<Element> elements) throws IOException {
        JavaFileObject file = processingEnv.getFiler().createSourceFile(CLASS_NAME);
        Elements elementUtils = processingEnv.getElementUtils();

        try (PrintWriter out = new PrintWriter(file.openWriter())) {
            out.println("package com.danwalkerdev.test;");
            out.println();

            elements.forEach(e -> out.println("import " + elementUtils.getPackageOf(e) + "." + e.getSimpleName() + ";"));

            out.println();
            out.println("public class " + CLASS_NAME + " {");

            elements.forEach(e -> writeMethod(e, out));

            out.println("}");

        }

    }

    private void writeMethod(Element e, PrintWriter out) {
        String methodTemplate = """
                  public static %1$s %2$s(String name) {
                    return %1$s.valueOf(name.toUpperCase());
                  }
                  
                """;
        Name name = e.getSimpleName();
        String methodName = String.valueOf(name.charAt(0)).toLowerCase() + name.toString().substring(1);

        out.print(String.format(methodTemplate,
                name,
                methodName));
    }
}

/*
true is returned in "process", and this means on the next "round" of processing these annotations are "claimed" and
no further processing is done. If false is returned, this is like saying "this processor recognises that annotation,
but cannot process it (maybe it's not this processor's job)." Then another processor that also accepts the same
annotation type can do something else. Probably the order is not guaranteed though, so you couldn't rely on some
final processor to handle certain exceptions. But you might be able to have a "class" processor, then a "method"
processor, if your annotation was applicable to different source element types.

TypeElement represents a class or interface in the compiler module. Specifically here, it is an interface
*/
