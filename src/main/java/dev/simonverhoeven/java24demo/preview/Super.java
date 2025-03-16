package dev.simonverhoeven.java24demo.preview;

/// JEP 982 - Flexible Constructor Bodies (Third Preview)
public class Super extends Life{
    private static final int MEANING_OF_LIFE = 42;

    public Super(int meaningOfLife) {
        if (MEANING_OF_LIFE != meaningOfLife) {
            throw new IllegalArgumentException("Please read Hitchhiker's guide to the galaxy");
        }
        super(meaningOfLife);
    }

    public Super() {
        var meaningOfLife = MEANING_OF_LIFE;
        this(meaningOfLife);
    }

    void main() {
        new Super();
    }
}

class Life {
    int meaningOfLife;

    public Life(int meaningOfLife) {
        this.meaningOfLife = meaningOfLife;
    }
}