package met.freehij.kareliq.injection.injections;

import met.freehij.kareliq.injection.ClassTransformerBase;
import met.freehij.kareliq.injection.InjectionMain;
import met.freehij.kareliq.utils.mappings.ClassMappings;
import met.freehij.kareliq.utils.mappings.FieldMappings;
import met.freehij.kareliq.utils.mappings.MappingResolver;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class EntityPlayerSPInjection extends ClassTransformerBase {
    public EntityPlayerSPInjection(String className, String methodName, String descriptor) {
        super(className, methodName, descriptor, EntityPlayerSPVisitor.class);
    }

    public static class EntityPlayerSPVisitor extends MethodVisitor {
        public EntityPlayerSPVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM9, methodVisitor);
        }

        /*
        roughly equivalent to:
        if (met.freehij.kareliq.module.movement.Flight.INSTANCE.isToggled()) {
            this.onGround = true;
            this.motionY = 0D;
        }
        if (met.freehij.kareliq.module.movement.NoFallDamage.INSTANCE.isToggled()) {
            this.fallDistance = 0F;
        }
        */
        @Override
        public void visitCode() {
            String entityClass = MappingResolver.resolveClass(ClassMappings.ENTITY);

            Label elseLabel = new Label();
            mv.visitFieldInsn(Opcodes.GETSTATIC,
                    "met/freehij/kareliq/module/movement/Flight",
                    "INSTANCE",
                    "Lmet/freehij/kareliq/module/movement/Flight;");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "met/freehij/kareliq/module/movement/Flight",
                    "isToggled",
                    "()Z",
                    false);
            mv.visitJumpInsn(Opcodes.IFEQ, elseLabel);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitInsn(Opcodes.ICONST_1);
            mv.visitFieldInsn(Opcodes.PUTFIELD, InjectionMain.playerClass, MappingResolver.resolveField(
                    entityClass, "Z", FieldMappings.ON_GROUND), "Z");
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitInsn(Opcodes.DCONST_0);
            mv.visitFieldInsn(Opcodes.PUTFIELD, InjectionMain.playerClass, MappingResolver.resolveField(
                    entityClass, "D", FieldMappings.MOTION_Y), "D");
            mv.visitLabel(elseLabel);

            Label skipNoFallLabel = new Label();
            mv.visitFieldInsn(Opcodes.GETSTATIC,
                    "met/freehij/kareliq/module/movement/NoFallDamage",
                    "INSTANCE",
                    "Lmet/freehij/kareliq/module/movement/NoFallDamage;");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "met/freehij/kareliq/module/movement/NoFallDamage",
                    "isToggled",
                    "()Z",
                    false);
            mv.visitJumpInsn(Opcodes.IFEQ, skipNoFallLabel);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitInsn(Opcodes.FCONST_0);
            mv.visitFieldInsn(Opcodes.PUTFIELD, InjectionMain.playerClass,  MappingResolver.resolveField(
                    entityClass, "F", FieldMappings.FALL_DISTANCE), "F");
            mv.visitLabel(skipNoFallLabel);
            super.visitCode();
        }
    }
}