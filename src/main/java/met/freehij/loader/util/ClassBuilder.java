package met.freehij.loader.util;

import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassBuilder {
    private final String className;
    private final String superClass;
    private final List<MethodOverride> overrides = new ArrayList<>();
    private final ClassLoader targetClassLoader;
    private static final AtomicInteger counter = new AtomicInteger();

    public ClassBuilder(ClassLoader targetClassLoader, String superClass) {
        this.targetClassLoader = targetClassLoader;
        this.superClass = superClass.replace('.', '/');
        this.className = "GeneratedClass$" + counter.getAndIncrement();
    }

    public ClassBuilder overrideMethod(
            String methodName,
            String descriptor,
            String handlerClass,
            String handlerMethod,
            boolean callSuper
    ) {
        overrides.add(new MethodOverride(methodName, descriptor, handlerClass, handlerMethod, callSuper));
        return this;
    }

    public Class<?> build() {
        try {
            byte[] bytecode = generateClassBytes();
            return defineClass(className.replace('/', '.'), bytecode, targetClassLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] generateClassBytes() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cw.visit(Opcodes.V1_8,
                Opcodes.ACC_PUBLIC,
                className,
                null,
                superClass,
                null);

        generateDefaultConstructor(cw);
        generateOverriddenMethods(cw);

        cw.visitEnd();
        return cw.toByteArray();
    }

    private void generateDefaultConstructor(ClassWriter cw) {
        MethodVisitor mv = cw.visitMethod(
                Opcodes.ACC_PUBLIC,
                "<init>",
                "()V",
                null,
                null
        );
        mv.visitCode();
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        mv.visitMethodInsn(
                Opcodes.INVOKESPECIAL,
                superClass,
                "<init>",
                "()V",
                false
        );
        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    private void generateOverriddenMethods(ClassWriter cw) {
        for (MethodOverride override : overrides) {
            Method original = new Method(override.methodName, override.descriptor);
            GeneratorAdapter mg = new GeneratorAdapter(
                    cw.visitMethod(
                            Opcodes.ACC_PUBLIC,
                            original.getName(),
                            original.getDescriptor(),
                            null,
                            null
                    ),
                    Opcodes.ACC_PUBLIC,
                    original.getName(),
                    original.getDescriptor()
            );

            mg.visitCode();

            Type[] argTypes = original.getArgumentTypes();

            if (override.callSuper) {
                mg.loadThis();
                for (int i = 0; i < argTypes.length; i++) {
                    mg.loadArg(i);
                }
                mg.visitMethodInsn(
                        Opcodes.INVOKESPECIAL,
                        superClass,
                        original.getName(),
                        original.getDescriptor(),
                        false
                );
            }

            mg.push(argTypes.length);
            mg.newArray(Type.getType(Object.class));

            for (int i = 0; i < argTypes.length; i++) {
                mg.dup();
                mg.push(i);
                mg.loadArg(i);
                boxType(mg, argTypes[i]);
                mg.arrayStore(Type.getType(Object.class));
            }

            mg.loadThis();
            mg.swap();
            mg.invokeStatic(
                    Type.getObjectType(override.handlerClass),
                    new Method(override.handlerMethod, "(Ljava/lang/Object;[Ljava/lang/Object;)" + original.getReturnType())
            );

            mg.returnValue();
            mg.endMethod();
        }
    }

    private void boxType(GeneratorAdapter mg, Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
                mg.invokeStatic(Type.getType(Boolean.class), new Method("valueOf", "(Z)Ljava/lang/Boolean;"));
                break;
            case Type.BYTE:
                mg.invokeStatic(Type.getType(Byte.class), new Method("valueOf", "(B)Ljava/lang/Byte;"));
                break;
            case Type.CHAR:
                mg.invokeStatic(Type.getType(Character.class), new Method("valueOf", "(C)Ljava/lang/Character;"));
                break;
            case Type.SHORT:
                mg.invokeStatic(Type.getType(Short.class), new Method("valueOf", "(S)Ljava/lang/Short;"));
                break;
            case Type.INT:
                mg.invokeStatic(Type.getType(Integer.class), new Method("valueOf", "(I)Ljava/lang/Integer;"));
                break;
            case Type.FLOAT:
                mg.invokeStatic(Type.getType(Float.class), new Method("valueOf", "(F)Ljava/lang/Float;"));
                break;
            case Type.LONG:
                mg.invokeStatic(Type.getType(Long.class), new Method("valueOf", "(J)Ljava/lang/Long;"));
                break;
            case Type.DOUBLE:
                mg.invokeStatic(Type.getType(Double.class), new Method("valueOf", "(D)Ljava/lang/Double;"));
                break;
        }
    }

    private Class<?> defineClass(String name, byte[] b, ClassLoader loader)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        java.lang.reflect.Method define = ClassLoader.class.getDeclaredMethod(
                "defineClass", String.class, byte[].class, int.class, int.class
        );
        define.setAccessible(true);
        return (Class<?>) define.invoke(loader, name, b, 0, b.length);
    }

    private static class MethodOverride {
        final String methodName;
        final String descriptor;
        final String handlerClass;
        final String handlerMethod;
        final boolean callSuper;

        MethodOverride(String methodName, String descriptor, String handlerClass, String handlerMethod, boolean callSuper) {
            this.methodName = methodName;
            this.descriptor = descriptor;
            this.handlerClass = handlerClass.replace('.', '/');
            this.handlerMethod = handlerMethod;
            this.callSuper = callSuper;
        }
    }
}