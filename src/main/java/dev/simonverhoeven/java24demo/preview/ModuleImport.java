package dev.simonverhoeven.java24demo.preview;

import static java.lang.System.out;
import module java.base;

/// JEP 494: Module Import Declarations (Second Preview)

public class ModuleImport {

    public static void main(String[] args) {
        List<String> elements = List.of("One", "two", "THREE");
        elements.stream().map(String::toUpperCase).forEach(out::println);
    }
}
