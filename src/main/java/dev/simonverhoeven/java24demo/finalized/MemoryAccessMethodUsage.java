package dev.simonverhoeven.java24demo.finalized;

// JEP 498: Warn upon Use of Memory-Access Methods in sun.misc.Unsafe

import sun.misc.Unsafe;

public class MemoryAccessMethodUsage {

    public static void main() throws NoSuchFieldException, IllegalAccessException {
        final var unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        final var unsafe = (Unsafe) unsafeField.get(null);
        unsafe.allocateMemory(1024);
    }
}
