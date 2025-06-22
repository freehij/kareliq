package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import org.objectweb.asm.*;

public class GuiIngameInjection extends ClassTransformerBase {
    public GuiIngameInjection() {
        super("uk", "a", "(FZII)V", GuiIngameVisitor.class);
    }

    public static class GuiIngameVisitor extends MethodVisitor {
        private boolean hasReturn = false;
        private boolean injected = false;
        private final Label startFinally = new Label();

        public GuiIngameVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitCode();
            mv.visitLabel(startFinally);
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

        /*
        roughly equivalent to:
        this.mc.fontRenderer.drawStringWithShadow("§6kareliq", 2, 2, Integer.MAX_VALUE);

        int i = 2;
        net.minecraft.src.ScaledResolution scaledResolution = new net.minecraft.src.ScaledResolution (
            this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight
        );
        for (met.freehij.kareliq.module.Module module : met.freehij.kareliq.ClientMain.modules) {
            if (module.isToggled()) {
                this.mc.fontRenderer.drawStringWithShadow(
                    module.getName(),
                    scaledResolution.getScaledWidth() - this.mc.fontRenderer.getStringWidth(module.getName()),
                    i, Integer.MAX_VALUE
                );
                i += 10;
            }
        }
        */
        private void insertCustomCode() {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, "uk", "g", "Lnet/minecraft/client/Minecraft;");
            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "q", "Lse;");
            mv.visitLdcInsn("§6kareliq");
            mv.visitInsn(Opcodes.ICONST_2);
            mv.visitInsn(Opcodes.ICONST_2);
            mv.visitLdcInsn(Integer.MAX_VALUE);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "se", "a", "(Ljava/lang/String;III)V", false);

            mv.visitFieldInsn(Opcodes.GETSTATIC, "met/freehij/kareliq/module/render/ModuleList", "INSTANCE", "Lmet/freehij/kareliq/module/render/ModuleList;");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "met/freehij/kareliq/module/render/ModuleList", "isToggled", "()Z", false);
            Label skipModuleList = new Label();
            mv.visitJumpInsn(Opcodes.IFEQ, skipModuleList);
            mv.visitTypeInsn(Opcodes.NEW, "qm");
            mv.visitInsn(Opcodes.DUP);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, "uk", "g", "Lnet/minecraft/client/Minecraft;");
            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "z", "Lkr;");
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, "uk", "g", "Lnet/minecraft/client/Minecraft;");
            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "d", "I");
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, "uk", "g", "Lnet/minecraft/client/Minecraft;");
            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "e", "I");
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "qm", "<init>", "(Lkr;II)V", false);
            mv.visitVarInsn(Opcodes.ASTORE, 6);
            mv.visitInsn(Opcodes.ICONST_2);
            mv.visitVarInsn(Opcodes.ISTORE, 5);
            mv.visitFieldInsn(Opcodes.GETSTATIC, "met/freehij/kareliq/ClientMain", "modules", "[Lmet/freehij/kareliq/module/Module;");
            mv.visitVarInsn(Opcodes.ASTORE, 7);
            mv.visitVarInsn(Opcodes.ALOAD, 7);
            mv.visitInsn(Opcodes.ARRAYLENGTH);
            mv.visitVarInsn(Opcodes.ISTORE, 8);
            mv.visitInsn(Opcodes.ICONST_0);
            mv.visitVarInsn(Opcodes.ISTORE, 9);
            Label loopCondition = new Label();
            Label loopEnd = new Label();
            Label nextModule = new Label();
            mv.visitLabel(loopCondition);
            mv.visitVarInsn(Opcodes.ILOAD, 9);
            mv.visitVarInsn(Opcodes.ILOAD, 8);
            mv.visitJumpInsn(Opcodes.IF_ICMPGE, loopEnd);
            mv.visitVarInsn(Opcodes.ALOAD, 7);
            mv.visitVarInsn(Opcodes.ILOAD, 9);
            mv.visitInsn(Opcodes.AALOAD);
            mv.visitVarInsn(Opcodes.ASTORE, 10);
            mv.visitVarInsn(Opcodes.ALOAD, 10);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "met/freehij/kareliq/module/Module", "isToggled", "()Z", false);
            mv.visitJumpInsn(Opcodes.IFEQ, nextModule);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, "uk", "g", "Lnet/minecraft/client/Minecraft;");
            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "q", "Lse;");
            mv.visitVarInsn(Opcodes.ALOAD, 10);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "met/freehij/kareliq/module/Module", "getName", "()Ljava/lang/String;", false);
            mv.visitVarInsn(Opcodes.ALOAD, 6);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "qm", "a", "()I", false);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, "uk", "g", "Lnet/minecraft/client/Minecraft;");
            mv.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/Minecraft", "q", "Lse;");
            mv.visitVarInsn(Opcodes.ALOAD, 10);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "met/freehij/kareliq/module/Module", "getName", "()Ljava/lang/String;", false);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "se", "a", "(Ljava/lang/String;)I", false);
            mv.visitInsn(Opcodes.ISUB);
            mv.visitInsn(Opcodes.ICONST_2);
            mv.visitInsn(Opcodes.ISUB);
            mv.visitVarInsn(Opcodes.ILOAD, 5);
            mv.visitLdcInsn(Integer.MAX_VALUE);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "se", "a", "(Ljava/lang/String;III)V", false);
            mv.visitIincInsn(5, 10);
            mv.visitLabel(nextModule);
            mv.visitIincInsn(9, 1);
            mv.visitJumpInsn(Opcodes.GOTO, loopCondition);
            mv.visitLabel(loopEnd);
            mv.visitLabel(skipModuleList);
        }
    }
}