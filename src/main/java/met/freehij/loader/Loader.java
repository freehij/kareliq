package met.freehij.loader;

import met.freehij.kareliq.ClientMain;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.constant.At;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.mappings.ClassMappings;
import met.freehij.loader.util.mappings.FieldMappings;
import met.freehij.loader.util.mappings.MethodMappings;
import org.objectweb.asm.*;

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Loader {
    private static final Map<String, List<InjectionPoint>> injectionPoints = new HashMap<>();
    private static final String INJECTION_PACKAGE = "met.freehij.kareliq.injection";

    public static void premain(String args, Instrumentation inst) {
        ClassMappings.initRefmap();
        MethodMappings.initRefmap();
        FieldMappings.initRefmap();
        scanForInjections();
        inst.addTransformer(new MixinTransformer(), true);
        ClientMain.startClient();
    }

    private static void scanForInjections() {
        String packagePath = INJECTION_PACKAGE.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("jar")) {
                    processJarResource(resource, packagePath, classLoader);
                } else {
                    processFileResource(resource, classLoader);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processJarResource(URL jarUrl, String packagePath, ClassLoader classLoader) {
        try {
            JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
            try (JarFile jar = jarConnection.getJarFile()) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(packagePath) && name.endsWith(".class") && !entry.isDirectory()) {
                        String className = name.replace('/', '.').substring(0, name.length() - 6);
                        processInjectionClass(className, classLoader);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFileResource(URL fileUrl, ClassLoader classLoader) {
        try {
            File dir = new File(fileUrl.toURI());
            if (!dir.isDirectory()) return;

            for (File file : dir.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".class")) {
                    String className = INJECTION_PACKAGE + '.' + file.getName().replace(".class", "");
                    processInjectionClass(className, classLoader);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void processInjectionClass(String className, ClassLoader classLoader) {
        try {
            Class<?> clazz = classLoader.loadClass(className);
            Injection injection = clazz.getAnnotation(Injection.class);
            if (injection == null) return;

            String target = ClassMappings.get(injection.value());
            for (Method method : clazz.getDeclaredMethods()) {
                Inject inject = method.getAnnotation(Inject.class);
                if (inject == null) continue;

                String[] methodInfo = MethodMappings.get(target, inject.method());
                injectionPoints.computeIfAbsent(target, k -> new ArrayList<>())
                        .add(new InjectionPoint(
                                target,
                                methodInfo[0],
                                methodInfo[1],
                                inject.at(),
                                clazz.getName().replace('.', '/'),
                                method.getName()
                        ));
            }
        } catch (ClassNotFoundException ignored) {
        }
    }

    private static Class<?> loadClass(String fullName) {
        try {
            return Class.forName(fullName, false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            ClassLoader ctxLoader = Thread.currentThread().getContextClassLoader();
            if (ctxLoader != null) try {
                return Class.forName(fullName, false, ctxLoader);
            } catch (ClassNotFoundException ignored) {}
            return null;
        }
    }

    static class InjectionPoint {
        final String targetClass;
        final String methodName;
        final String descriptor;
        final At location;
        final String handlerClass;
        final String handlerMethod;

        InjectionPoint(String targetClass, String methodName, String descriptor,
                       At location, String handlerClass, String handlerMethod) {
            this.targetClass = targetClass;
            this.methodName = methodName;
            this.descriptor = descriptor;
            this.location = location;
            this.handlerClass = handlerClass;
            this.handlerMethod = handlerMethod;
        }
    }

    static class MixinTransformer implements ClassFileTransformer {
        @Override
        public byte[] transform(ClassLoader l, String className, Class<?> c,
                                ProtectionDomain d, byte[] buffer) {
            if (!injectionPoints.containsKey(className)) return null;

            ClassReader cr = new ClassReader(buffer);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
            cr.accept(new InjectionClassVisitor(cw, className), 0);
            return cw.toByteArray();
        }
    }

    static class InjectionClassVisitor extends ClassVisitor {
        private final String className;

        InjectionClassVisitor(ClassVisitor cv, String className) {
            super(Opcodes.ASM9, cv);
            this.className = className;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc,
                                         String sig, String[] ex) {
            MethodVisitor mv = super.visitMethod(access, name, desc, sig, ex);
            List<InjectionPoint> points = injectionPoints.get(className);
            if (points == null) return mv;

            for (InjectionPoint point : points) {
                if (point.methodName.equals(name) && point.descriptor.equals(desc)) {
                    mv = new InjectionMethodVisitor(mv, access, desc, point);
                }
            }
            return mv;
        }
    }

    static class InjectionMethodVisitor extends MethodVisitor {
        private final InjectionPoint injection;
        private final int methodAccess;
        private final String methodDesc;
        private boolean hasReturned;
        private boolean inInjection;

        InjectionMethodVisitor(MethodVisitor mv, int access, String desc, InjectionPoint injection) {
            super(Opcodes.ASM9, mv);
            this.injection = injection;
            this.methodAccess = access;
            this.methodDesc = desc;
        }

        @Override public void visitCode() {
            super.visitCode();
            if (injection.location == At.HEAD) injectHelper();
        }

        @Override public void visitInsn(int opcode) {
            if (inInjection) {
                super.visitInsn(opcode);
                return;
            }

            if (injection.location == At.RETURN && isReturn(opcode)) {
                injectHelper();
                super.visitInsn(opcode);
                hasReturned = true;
            } else {
                super.visitInsn(opcode);
                if (isReturn(opcode)) hasReturned = true;
            }
        }

        @Override public void visitEnd() {
            if (injection.location == At.TAIL && !hasReturned) injectHelper();
            super.visitEnd();
        }

        @Override
        public void visitMaxs(int maxStack, int maxLocals) {
            super.visitMaxs(Math.max(maxStack, 10), Math.max(maxLocals, 101));
        }

        private boolean isReturn(int opcode) {
            return (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN);
        }

        private void injectHelper() {
            if (inInjection) return;
            inInjection = true;
            generateHelperCall(this, methodAccess, methodDesc, injection);
            inInjection = false;
        }
    }

    static void generateHelperCall(MethodVisitor mv, int access, String desc, InjectionPoint injection) {
        mv.visitTypeInsn(Opcodes.NEW, "met/freehij/loader/util/InjectionHelper");
        mv.visitInsn(Opcodes.DUP);
        mv.visitLdcInsn(Type.getObjectType(injection.targetClass));
        boolean isStatic = (access & Opcodes.ACC_STATIC) != 0;
        mv.visitVarInsn(isStatic ? Opcodes.ACONST_NULL : Opcodes.ALOAD, 0);

        Type[] argTypes = Type.getArgumentTypes(desc);
        mv.visitIntInsn(Opcodes.BIPUSH, argTypes.length);
        mv.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");

        int varIndex = isStatic ? 0 : 1;
        for (int i = 0; i < argTypes.length; i++) {
            mv.visitInsn(Opcodes.DUP);
            mv.visitIntInsn(Opcodes.BIPUSH, i);
            loadAndBoxArgument(mv, varIndex, argTypes[i]);
            varIndex += argTypes[i].getSize();
        }

        mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
                "met/freehij/loader/util/InjectionHelper", "<init>",
                "(Ljava/lang/Class;Ljava/lang/Object;[Ljava/lang/Object;)V", false);

        mv.visitVarInsn(Opcodes.ASTORE, 100);
        mv.visitVarInsn(Opcodes.ALOAD, 100);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                injection.handlerClass, injection.handlerMethod,
                "(Lmet/freehij/loader/util/InjectionHelper;)V", false);

        Label continueLabel = new Label();
        mv.visitVarInsn(Opcodes.ALOAD, 100);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "met/freehij/loader/util/InjectionHelper", "isCancelled", "()Z", false);
        mv.visitJumpInsn(Opcodes.IFEQ, continueLabel);

        Type returnType = Type.getReturnType(desc);
        if (returnType == Type.VOID_TYPE) {
            mv.visitInsn(Opcodes.RETURN);
        } else {
            mv.visitVarInsn(Opcodes.ALOAD, 100);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "met/freehij/loader/util/InjectionHelper", "getReturnValue", "()Ljava/lang/Object;", false);
            unbox(mv, returnType);
            mv.visitInsn(returnType.getOpcode(Opcodes.IRETURN));
        }

        mv.visitLabel(continueLabel);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }


    private static void loadAndBoxArgument(MethodVisitor mv, int index, Type type) {
        mv.visitVarInsn(type.getOpcode(Opcodes.ILOAD), index);
        switch (type.getSort()) {
            case Type.BOOLEAN: box(mv, "java/lang/Boolean", "(Z)Ljava/lang/Boolean;"); break;
            case Type.BYTE: box(mv, "java/lang/Byte", "(B)Ljava/lang/Byte;"); break;
            case Type.CHAR: box(mv, "java/lang/Character", "(C)Ljava/lang/Character;"); break;
            case Type.SHORT: box(mv, "java/lang/Short", "(S)Ljava/lang/Short;"); break;
            case Type.INT: box(mv, "java/lang/Integer", "(I)Ljava/lang/Integer;"); break;
            case Type.FLOAT: box(mv, "java/lang/Float", "(F)Ljava/lang/Float;"); break;
            case Type.LONG: box(mv, "java/lang/Long", "(J)Ljava/lang/Long;"); break;
            case Type.DOUBLE: box(mv, "java/lang/Double", "(D)Ljava/lang/Double;"); break;
        }
        mv.visitInsn(Opcodes.AASTORE);
    }

    private static void box(MethodVisitor mv, String owner, String descriptor) {
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, owner, "valueOf", descriptor, false);
    }

    private static void unbox(MethodVisitor mv, Type type) {
        switch (type.getSort()) {
            case Type.BOOLEAN:
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Boolean");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);
                break;
            case Type.BYTE:
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Byte");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Byte", "byteValue", "()B", false);
                break;
            case Type.CHAR:
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Character");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Character", "charValue", "()C", false);
                break;
            case Type.SHORT:
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Short");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Short", "shortValue", "()S", false);
                break;
            case Type.INT:
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Integer");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Integer", "intValue", "()I", false);
                break;
            case Type.FLOAT:
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Float");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Float", "floatValue", "()F", false);
                break;
            case Type.LONG:
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Long");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Long", "longValue", "()J", false);
                break;
            case Type.DOUBLE:
                mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Double");
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Double", "doubleValue", "()D", false);
                break;
            case Type.ARRAY:
            case Type.OBJECT:
                mv.visitTypeInsn(Opcodes.CHECKCAST, type.getInternalName());
                break;
        }
    }
}