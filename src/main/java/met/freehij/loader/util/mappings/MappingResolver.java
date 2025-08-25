package met.freehij.loader.util.mappings;

import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class MappingResolver {
    public static String resolveClass(String[] entries) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        for (String entry : entries) {
            entry = entry.replace('.', '/');
            if (cl.getResource(entry + ".class") != null) return entry;
        }
        System.out.println("Class " + entries[1] + " not found");
        return null;
    }

    public static String resolveMethod(String className, String descriptor, String[] entries) {
        className = className.replace(".", "/");
        String resourcePath = className + ".class";
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            System.out.println("Class resource not found: " + resourcePath);
            return null;
        }

        try {
            ClassReader cr = new ClassReader(is);
            Set<String> methods = new HashSet<>();
            cr.accept(new ClassVisitor(Opcodes.ASM9) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc,
                                                 String signature, String[] exceptions) {
                    if (desc.equals(descriptor)) {
                        methods.add(name);
                    }
                    return null;
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            for (String entry : entries) {
                if (methods.contains(entry)) return entry;
            }
            System.out.println("Can't resolve method " + entries[1] + " with descriptor " + descriptor +
                    " in class " + className);
            return null;
        } catch (IOException e) {
            System.out.println("Error reading class: " + className + " - " + e.getMessage());
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {}
        }
    }

    public static String resolveField(String className, String descriptor, String[] entries) {
        className = className.replace(".", "/");
        String resourcePath = className + ".class";
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            System.out.println("Class resource not found: " + resourcePath);
            return null;
        }

        try {
            ClassReader cr = new ClassReader(is);
            Set<String> fields = new HashSet<>();
            cr.accept(new ClassVisitor(Opcodes.ASM9) {
                @Override
                public FieldVisitor visitField(int access, String name, String desc,
                                               String signature, Object value) {
                    if (desc.equals(descriptor)) {
                        fields.add(name);
                    }
                    return null;
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            for (String entry : entries) {
                if (fields.contains(entry)) {
                    return entry;
                }
            }

            System.out.println("Can't resolve field " + entries[1] + " with descriptor " + descriptor +
                    " in class " + className);
            return null;
        } catch (IOException e) {
            System.out.println("Error reading class: " + className + " - " + e.getMessage());
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }
}
