package dev.simonverhoeven.java24demo.preview;

// JEP 488 - Primitive Types in Patterns, instanceof, and switch (Second Preview)

public class PrimitiveTypes {
    record ExamResults(int score){}

    // primitives are now supported pattern matching,
    // boolean, float, double and long are now also supported in switches
    public String determineGrade(ExamResults examResults) {
        return switch (examResults.score) {
            case int i when i >= 90 -> "A";
            case int i when i >= 80 -> "B";
            case int i when i >= 70 -> "C";
            case int i when i >= 60 -> "D";
            case int i when i >= 50 -> "E";
            case int i -> "Failed with a score of " + examResults.score;
        };
    }

    // We can now pattern match using primitive types, which makes switching to type patterns easier as well
    public void patterns() {
        int number = 0;

        if (number instanceof int num) {
            System.out.println(num);
        }
    }
}
