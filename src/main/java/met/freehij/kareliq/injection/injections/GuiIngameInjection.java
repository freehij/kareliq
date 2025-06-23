package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import org.objectweb.asm.*;

public class GuiIngameInjection extends ClassTransformerBase {
    public GuiIngameInjection(String className, String methodName, String descriptor) {
    	super(className, methodName, descriptor, GuiIngameVisitor.class);
    }

    public static class GuiIngameVisitor extends MethodVisitor {
        private boolean hasReturn = false;
        private boolean injected = false;

        public GuiIngameVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                if (!injected) {
                    insertCustomCode();
                    injected = true;
                }
                hasReturn = true;
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            if (!injected && !hasReturn) {
                insertCustomCode();
                injected = true;
            }
            super.visitMaxs(Math.max(maxStack, 10), Math.max(maxLocals, 11));
        }

        private void insertCustomCode() {
        	//ClientMain.renderGuiIngame(this);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 5);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "met/freehij/kareliq/ClientMain", "renderGuiIngame", "(Ljava/lang/Object;Ljava/lang/Object;)V",
                    false);
        }
    }
}