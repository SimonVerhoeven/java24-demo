package dev.simonverhoeven.java24demo.preview;

import java.util.stream.Stream;

import static java.lang.System.out;

// JEP 494: Module Import Declarations (Second Preview)

public class ModuleImport {

    public static void main(String[] args) {
        Stream.of(args).map(input -> input.toUpperCase()).forEach(out::println);
    }
}
