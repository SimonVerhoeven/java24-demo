package dev.simonverhoeven.java24demo.preview.scopedvalues;

// JEP 487 - Scoped Values (Fourth preview)

public class Scoper {
    public final static ScopedValue<String> NAME = ScopedValue.newInstance();

    public static void main() {
        ScopedValue.where(NAME, "Continuum Consulting NV").run(() -> Greeter.greet());
        ScopedValue.where(NAME, "BEJUG").run(() -> Greeter.greet());
    }

}
