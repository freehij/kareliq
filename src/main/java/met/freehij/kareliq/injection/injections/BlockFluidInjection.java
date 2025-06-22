package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;

public class BlockFluidInjection extends ClassTransformerBase {
    public BlockFluidInjection() {
        super("rk", "d", "(Lxg;III)F", BlockInjection.BlockVisitor.class);
    }
}