package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;

public class WorldInjection extends ClassTransformerBase {
    public WorldInjection() {
        super("fb", "c", "(III)F", BlockInjection.BlockVisitor.class);
    }
}