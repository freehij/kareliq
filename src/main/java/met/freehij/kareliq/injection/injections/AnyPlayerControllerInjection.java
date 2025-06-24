package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AnyPlayerControllerInjection extends ClassTransformerBase {
    public AnyPlayerControllerInjection(String className, String methodName) {
        super(className, methodName, "(IIII)V", PlayerControllerSPVisitor.class);
    }

    public static class PlayerControllerSPVisitor extends MethodVisitor {
        public PlayerControllerSPVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ILOAD, 1);
            mv.visitVarInsn(Opcodes.ILOAD, 2);
            mv.visitVarInsn(Opcodes.ILOAD, 3);
            mv.visitVarInsn(Opcodes.ILOAD, 4);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "met/freehij/kareliq/utils/ReflectionHelper",
                    "awtysm_method", "(Ljava/lang/Object;IIII)V", false);
            super.visitCode();
        }
    }
}