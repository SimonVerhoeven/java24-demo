package dev.simonverhoeven.java24demo.finalized;

import java.lang.classfile.ClassModel;
import java.lang.classfile.MethodModel;
import java.lang.classfile.ClassFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;


///  JEP 484 - Class file API

public class ClassFileAPI {

    public static void main() throws Exception {
        final var stream = ClassFileAPI.class.getClassLoader().getResource("dev/simonverhoeven/java24demo/finalized/ClassFileAPI.class");
        final var classFilePath = Path.of(Objects.requireNonNull(stream).toURI());
        final var classBytes = Files.readAllBytes(classFilePath);
        final var classFile = ClassFile.of();
        final var classModel = classFile.parse(classBytes);

        listAllMethods(classModel);

        final var newClassModel = classModelWithoutMeaning(classFile, classModel);
        listAllMethods(newClassModel);
    }

    private static ClassModel classModelWithoutMeaning(ClassFile classFile, ClassModel classModel) {
        byte[] newBytes = classFile.build(classModel.thisClass().asSymbol(),
                classBuilder -> {
            for (final var element : classModel) {
                if (!(element instanceof MethodModel mm
                        && mm.methodName().stringValue().startsWith("meaning"))) {
                    classBuilder.with(element);
                }
            }
        });
        return classFile.parse(newBytes);
    }

    private static void listAllMethods(ClassModel classModel) {
        final var methods = classModel.methods();

        System.out.println("Methods:");
        methods.forEach(method -> System.out.println(method.methodName()));
        System.out.println("====================");
    }

    private String meaningOfLife() {
        return "42";
    }

}
