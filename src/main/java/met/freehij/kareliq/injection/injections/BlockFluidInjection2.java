package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import met.freehij.kareliq.injection.InjectionMain;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BlockFluidInjection2 extends ClassTransformerBase {
    public BlockFluidInjection2(String className, String methodName, String descriptor) {
        super(className, methodName, descriptor, BlockFluidVisitor.class);
    }

    public static class BlockFluidVisitor extends MethodVisitor {
        public BlockFluidVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
            mv.visitFieldInsn(
                    Opcodes.GETSTATIC,
                    "met/freehij/kareliq/module/movement/LiquidWalk",
                    "INSTANCE",
                    "Lmet/freehij/kareliq/module/movement/LiquidWalk;");
            mv.visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "met/freehij/kareliq/module/movement/LiquidWalk",
                    "isToggled",
                    "()Z",
                    false);
            Label elseLabel = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, elseLabel);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitVarInsn(Opcodes.ILOAD, 2);
            mv.visitVarInsn(Opcodes.ILOAD, 3);
            mv.visitVarInsn(Opcodes.ILOAD, 4);
            mv.visitMethodInsn(
                    Opcodes.INVOKESPECIAL,
                    InjectionMain.blockClass,
                    InjectionMain.getCollisionBBMethod,
                    "(L" + InjectionMain.worldClass + ";III)L" + InjectionMain.boundingBoxClass + ";",
                    false);
            mv.visitInsn(Opcodes.ARETURN);
            mv.visitLabel(elseLabel);
            super.visitCode();
        }
    }
}