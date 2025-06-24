package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MovementInputFromOptionsInjection1 extends ClassTransformerBase {
    public MovementInputFromOptionsInjection1(String className, String methodName, String descriptor) {
        super(className, methodName, descriptor, MovementInputVisitor.class);
    }

    public static class MovementInputVisitor extends MethodVisitor {
        public MovementInputVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
        	//ClientMain.handleKeypress(key, pressed);
        	mv.visitVarInsn(Opcodes.ILOAD, 1);
        	mv.visitVarInsn(Opcodes.ILOAD, 2);
        	mv.visitMethodInsn(Opcodes.INVOKESTATIC, "met/freehij/kareliq/ClientMain", "handleKeypress", "(IZ)V", false);
            super.visitCode();
        }
    }
}
