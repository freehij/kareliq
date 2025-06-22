package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MovementInputFromOptionsInjection extends ClassTransformerBase {
    public MovementInputFromOptionsInjection() {
        super("ln", "a", "(IZ)V", MovementInputVisitor.class);
    }

    public static class MovementInputVisitor extends MethodVisitor {
        public MovementInputVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
            /*
            roughly equivalent to:
            public void checkKeyForMovementInput(int key, boolean pressed) {
                if (pressed) {
                    for (met.freehij.kareliq.module.Module module : met.freehij.kareliq.ClientMain.modules) {
                        if (module.getKeyBind() == key) module.toggle();
                    }
                }
                ...
            }
            */
            Label skipBlockLabel = new Label();
            Label loopStartLabel = new Label();
            Label loopConditionLabel = new Label();
            Label nextIterationLabel = new Label();

            mv.visitVarInsn(Opcodes.ILOAD, 2);
            mv.visitJumpInsn(Opcodes.IFEQ, skipBlockLabel);

            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    "met/freehij/kareliq/ClientMain",
                    "modules",
                    "[Lmet/freehij/kareliq/module/Module;"
            );
            mv.visitVarInsn(Opcodes.ASTORE, 3);

            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitVarInsn(Opcodes.ISTORE, 4);
            mv.visitJumpInsn(Opcodes.GOTO, loopConditionLabel);

            mv.visitLabel(loopStartLabel);
            mv.visitVarInsn(Opcodes.ALOAD, 3);
            mv.visitVarInsn(Opcodes.ILOAD, 4);
            mv.visitInsn(Opcodes.AALOAD);
            mv.visitVarInsn(Opcodes.ASTORE, 5);

            mv.visitVarInsn(Opcodes.ALOAD, 5);
            mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "met/freehij/kareliq/module/Module",
                    "getKeyBind",
                    "()I",
                    false
            );
            mv.visitVarInsn(Opcodes.ILOAD, 1);
            mv.visitJumpInsn(Opcodes.IF_ICMPNE, nextIterationLabel);

            mv.visitVarInsn(Opcodes.ALOAD, 5);
            mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "met/freehij/kareliq/module/Module",
                    "toggle",
                    "()V",
                    false
            );

            mv.visitLabel(nextIterationLabel);
            mv.visitIincInsn(4, 1);
            mv.visitLabel(loopConditionLabel);
            mv.visitVarInsn(Opcodes.ILOAD, 4);
            mv.visitVarInsn(Opcodes.ALOAD, 3);
            mv.visitInsn(Opcodes.ARRAYLENGTH);
            mv.visitJumpInsn(Opcodes.IF_ICMPLT, loopStartLabel);

            mv.visitLabel(skipBlockLabel);
            super.visitCode();
        }
    }
}
