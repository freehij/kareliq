package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;

public class BlockFluidInjection extends ClassTransformerBase {
    public BlockFluidInjection(String className, String methodName, String descriptor) {
        super(className, methodName, descriptor, BlockInjection.BlockVisitor.class);
    }
}