package met.freehij.kareliq.injection;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;

public abstract class ClassTransformerBase implements ClassFileTransformer {
    private final String className;
    private final String methodName;
    private final String methodDescriptor;
    private final Class<? extends MethodVisitor> methodVisitor;

    public ClassTransformerBase(String className, String methodName, String methodDesc, Class<? extends MethodVisitor> methodVisitor) {
        this.className = className;
        this.methodName = methodName;
        this.methodDescriptor = methodDesc;
        this.methodVisitor = methodVisitor;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classFileBuffer) {
        if (className.equals(this.className)) {
            ClassReader reader = new ClassReader(classFileBuffer);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            ClassVisitor visitor = new ClassFinder(writer, this);
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }
        return null;
    }

    private static class ClassFinder extends ClassVisitor {
        private final ClassTransformerBase classTransformerBase;

        public ClassFinder(ClassVisitor classVisitor, ClassTransformerBase classTransformerBase) {
            super(Opcodes.ASM9, classVisitor);
            this.classTransformerBase = classTransformerBase;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                         String[] exceptions) {
            MethodVisitor methodVisitor1 = super.visitMethod(access, name, descriptor, signature, exceptions);
            if (name.equals(classTransformerBase.methodName) &&
                    descriptor.equals(classTransformerBase.methodDescriptor)) {
                try {
                    return classTransformerBase.methodVisitor.getConstructor(MethodVisitor.class)
                            .newInstance(methodVisitor1);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                         InvocationTargetException exception) {
                    System.out.println("Applying injection " + classTransformerBase.className + ";"
                            + classTransformerBase.methodName + ":" + classTransformerBase.methodDescriptor + " failed");
                    exception.printStackTrace();
                }
            }
            return methodVisitor1;
        }
    }
}