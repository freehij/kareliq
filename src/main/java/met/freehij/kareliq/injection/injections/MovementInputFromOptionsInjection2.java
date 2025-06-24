package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import met.freehij.kareliq.injection.InjectionMain;
import met.freehij.kareliq.utils.mappings.FieldMappings;
import met.freehij.kareliq.utils.mappings.MappingResolver;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MovementInputFromOptionsInjection2 extends ClassTransformerBase {
    public MovementInputFromOptionsInjection2(String className, String methodName, String descriptor) {
        super(className, methodName, descriptor, MovementInputVisitor.class);
    }

    public static class MovementInputVisitor extends MethodVisitor {
        public MovementInputVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        /*
        should be roughly equivalent to:
        if (met.freehij.kareliq.module.movement.GuiWalk.INSTANCE.isToggled()) {
            this.movementKeyStates = met.freehij.kareliq.utils.ReflectionHelper.updatedMovementKeyStates(this.movementKeyStates);
        }
        */
        @Override
        public void visitCode() {
            String movementKeyStates = MappingResolver.resolveField(InjectionMain.movementInputClass, "[Z",
                    FieldMappings.MOVEMENT_KEY_STATES);

            Label skipLabel = new Label();
            mv.visitFieldInsn(Opcodes.GETSTATIC,
                    "met/freehij/kareliq/module/movement/GuiWalk",
                    "INSTANCE",
                    "Lmet/freehij/kareliq/module/movement/GuiWalk;"
            );
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "met/freehij/kareliq/module/movement/GuiWalk",
                    "isToggled",
                    "()Z",
                    false
            );
            mv.visitJumpInsn(Opcodes.IFEQ, skipLabel);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD,
                    InjectionMain.movementInputClass,
                    movementKeyStates,
                    "[Z"
            );
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "met/freehij/kareliq/utils/ReflectionHelper",
                    "updateMovementKeyStates",
                    "([Z)[Z",
                    false
            );
            mv.visitFieldInsn(Opcodes.PUTFIELD,
                    InjectionMain.movementInputClass,
                    movementKeyStates,
                    "[Z"
            );
            mv.visitLabel(skipLabel);
            super.visitCode();
        }
    }
}