package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BlockInjection extends ClassTransformerBase {
    public static boolean lastToggleState = false;

    public BlockInjection() {
        super("un", "d", "(Lxg;III)F", BlockVisitor.class);
    }

    public static class BlockVisitor extends MethodVisitor {
        public BlockVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        /*
        roughly equivalent to:
        public float getBlockBrightness(IBlockAccess blockAccess, int x, int y, int z) {
            if (met.freehij.kareliq.module.render.MaxBright.INSTANCE.isToggled()) return 1.0F;
            ...
        }
        */
        @Override
        public void visitCode() {
            Label elseLabel = new Label();
            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    "met/freehij/kareliq/module/render/FullBright",
                    "INSTANCE",
                    "Lmet/freehij/kareliq/module/render/FullBright;"
            );
            mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "met/freehij/kareliq/module/render/FullBright",
                    "isToggled",
                    "()Z",
                    false
            );
            mv.visitJumpInsn(Opcodes.IFEQ, elseLabel);
            mv.visitInsn(Opcodes.FCONST_1);
            mv.visitInsn(Opcodes.FRETURN);
            mv.visitLabel(elseLabel);
            super.visitCode();
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            mv.visitMaxs(Math.max(maxStack, 2), Math.max(maxLocals, 6));
        }
    }
}