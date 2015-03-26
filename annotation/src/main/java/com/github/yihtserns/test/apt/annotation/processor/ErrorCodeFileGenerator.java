package com.github.yihtserns.test.apt.annotation.processor;

import com.github.yihtserns.test.apt.annotation.MsgTemplate;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.StandardLocation;

/**
 *
 * @author yihtserns
 */
@SupportedAnnotationTypes("com.github.yihtserns.test.apt.annotation.MsgTemplate")
public class ErrorCodeFileGenerator extends AbstractProcessor {

    private List<Element> originatingElements = new ArrayList<Element>();
    private Properties errorCode2MsgTemplate = new Properties();

    @Override
    public boolean process(
            Set<? extends TypeElement> annotations,
            RoundEnvironment env) {

        for (VariableElement el : ElementFilter.fieldsIn(env.getElementsAnnotatedWith(MsgTemplate.class))) {
            originatingElements.add(el);

            MsgTemplate msgTemplate = el.getAnnotation(MsgTemplate.class);
            errorCode2MsgTemplate.setProperty(String.valueOf(el.getConstantValue()), msgTemplate.value());
        }

        if (env.processingOver()) {
            Location[] locations = {
                // Here will get copied into jar
                StandardLocation.CLASS_OUTPUT,
                // Here won't get copied into jar - don't know how to make it work with/without special configuration
                // but here will show up in IDE (NetBeans), which will be helpful for dev
                StandardLocation.SOURCE_OUTPUT
            };
            for (Location location : locations) {
                Writer writer = null;
                try {
                    FileObject errorCodeFile = processingEnv.getFiler().createResource(
                            location,
                            "",
                            "errorCode.properties",
                            originatingElements.toArray(new Element[originatingElements.size()]));
                    writer = errorCodeFile.openWriter();

                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generating errorCode.properties [" + errorCodeFile.toUri() + "]");
                    errorCode2MsgTemplate.store(writer, null);
                } catch (IOException ex) {
                    // Should not happen?/
                    throw new RuntimeException(ex);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                            // Should not happen?/
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }

        return true;
    }

}
