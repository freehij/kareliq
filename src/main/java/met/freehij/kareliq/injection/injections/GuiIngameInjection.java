package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import org.objectweb.asm.*;

public class GuiIngameInjection extends ClassTransformerBase {
    public GuiIngameInjection() {
        super("uk", "a", "(FZII)V", GuiIngameVisitor.class);
    }

    public static class GuiIngameVisitor extends MethodVisitor {
        private boolean hasReturn = false;
        private final Label startFinally = new Label();

        public GuiIngameVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
            mv.visitLabel(startFinally);
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                insertCustomCode();
                hasReturn = true;
            }
            mv.visitInsn(opcode);
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            if (!hasReturn) {
                insertCustomCode();
            }
            mv.visitMaxs(maxStack, maxLocals);
        }

        private void insertCustomCode() {
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitFieldInsn(Opcodes.GETFIELD, "uk", "g", "Lnet/minecraft/client/Minecraft;");
            super.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "q", "Lse;");
            super.visitLdcInsn("§6kareliq");
            super.visitIntInsn(Opcodes.SIPUSH, 2);
            super.visitIntInsn(Opcodes.SIPUSH, 2);
            super.visitLdcInsn(Integer.MAX_VALUE);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "se", "a", "(Ljava/lang/String;III)V", false);
            super.visitCode();
        }
    }
}

