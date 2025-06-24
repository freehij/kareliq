package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import met.freehij.kareliq.utils.mappings.MappingResolver;
import met.freehij.kareliq.utils.mappings.MethodMappings;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MinecraftInjection extends ClassTransformerBase {
    public MinecraftInjection() {
        super("net/minecraft/client/Minecraft",
                MappingResolver.resolveMethod(
                        "net/minecraft/client/Minecraft",
                        "(Ljava/lang/String;)Z",
                        MethodMappings.IS_LINE_COMMAND
                ),
                "(Ljava/lang/String;)Z", MinecraftVisitor.class);
    }

    public static class MinecraftVisitor extends MethodVisitor {
        public MinecraftVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        @Override
        public void visitCode() {
            this.visitVarInsn(Opcodes.ALOAD, 1);
            this.visitVarInsn(Opcodes.ALOAD, 1);
            this.visitMethodInsn(
                    Opcodes.INVOKESTATIC,
                    "met/freehij/kareliq/ClientMain",
                    "handleCommand",
                    "(Ljava/lang/String;)Z",
                    false
            );
            Label continueLabel = new Label();
            this.visitJumpInsn(Opcodes.IFEQ, continueLabel);
            this.visitInsn(Opcodes.ICONST_1);
            this.visitInsn(Opcodes.IRETURN);
            this.visitLabel(continueLabel);
            super.visitCode();
        }
    }
}
