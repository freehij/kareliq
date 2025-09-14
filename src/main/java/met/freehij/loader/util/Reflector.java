package met.freehij.loader.util;

import met.freehij.loader.util.mappings.FieldMappings;
import met.freehij.loader.util.mappings.MethodMappings;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reflector {
    private final HashMap<String, Reflector> fieldCaches = new HashMap<String, Reflector>();
    private final HashMap<String, Reflector> methodCaches = new HashMap<String, Reflector>();
    private final HashMap<String, Reflector> classCaches = new HashMap<String, Reflector>();
    
    private final Class<?> clazz;
    private final Object object;

    public Reflector(Class<?> clazz, Object object) {
        this.clazz = clazz;
        this.object = object;
    }

    public Object get() {
        return object;
    }

    public int getInt() {
        return (int) object;
    }

    public long getLong() {
        return (long) object;
    }

    public float getFloat() {
        return (float) object;
    }

    public double getDouble() {
        return (double) object;
    }

    public byte getByte() {
        return (byte) object;
    }

    public char getChar() {
        return (char) object;
    }

    public short getShort() {
        return (short) object;
    }

    public boolean getBoolean() {
        return (boolean) object;
    }

    public String getString() {
        return (String) object;
    }

    public Class<?> getActualClass() {
        return clazz;
    }

    public Reflector getField(String name) {
        Class<?> current = this.getActualClass();

        while (current != null) {
            String mappedName = FieldMappings.get(current.getName().replace(".", "/"), name);
            if (mappedName != null) {
                name = mappedName;
                break;
            }
            current = current.getSuperclass();
        }
        if (current == null) {
            throw new RuntimeException("No mapping found for " + name);
        }

        try {
            Field field = findField(current, name);
            field.setAccessible(true);
            Object value = field.get(object);
            return new Reflector(
                    value != null ? value.getClass() : field.getType(),
                    value
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field: " + name, e);
        }
    }

    public Reflector getFieldRaw(String name) {
        try {
            Field field = findField(this.getActualClass(), name);
            field.setAccessible(true);
            Object value = field.get(object);
            return new Reflector(
                    value != null ? value.getClass() : field.getType(),
                    value
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to get field: " + name, e);
        }
    }

    public void setField(String name, Object value) {
        Class<?> current = this.getActualClass();
        String originalName = name;

        while (current != null) {
            String mappedName = FieldMappings.get(current.getName().replace(".", "/"), name);
            if (mappedName != null) {
                name = mappedName;
                break;
            }
            current = current.getSuperclass();
        }
        if (current == null) {
            throw new RuntimeException("No mapping found for " + originalName);
        }

        try {
            Field field = findField(current, name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + name, e);
        }
    }

    public void setFieldRaw(String name, Object value) {
        try {
            Field field = findField(this.getActualClass(), name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + name, e);
        }
    }

    public Reflector invoke(String methodName, Object... args) {
        String descriptor = "";
        Class<?> current = this.getActualClass();

        while (current != null) {
            String[] mappedName = MethodMappings.get(current.getName().replace(".", "/"), methodName);
            if (mappedName != null) {
                methodName = mappedName[0];
                descriptor = mappedName[1];
                break;
            }
            current = current.getSuperclass();
        }
        if (current == null) {
            throw new RuntimeException("No mapping found for " + methodName);
        }

        try {
            return this.invokeRaw(methodName, parseDescriptor(descriptor), args);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Reflector invokeRaw(String methodName, Class<?>[] descriptor, Object... args) {
        try {
            Method method = findMethod(
                    object != null ? object.getClass() : clazz,
                    methodName,
                    descriptor
            );
            method.setAccessible(true);
            Object result = method.invoke(object, args);
            return new Reflector(
                    result != null ? result.getClass() : method.getReturnType(),
                    result
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke: " + methodName, e);
        }
    }

    public Reflector newInstance(String descriptor, Object... args) {
        try {
            return this.newInstanceRaw(parseDescriptor(descriptor), args);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Reflector newInstanceRaw(Class<?>[] descriptor, Object... args) {
        try {
            Object instance = this.getActualClass().getConstructor(descriptor).newInstance(args);
            return new Reflector(instance.getClass(), instance);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    
    private static Field findFieldInternal(Class<?> searchClass, String name) {
    	Class<?> current = searchClass;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldError("Field not found: " + name);
    }
    
    private static Method findMethodInternal(Class<?> searchClass, String name, Class<?>[] paramTypes) {
        Class<?> current = searchClass;
        while (current != null) {
            try {
                return current.getDeclaredMethod(name, paramTypes);
            } catch (NoSuchMethodException e) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchMethodError("Method not found: " + name);
    }
    
    private static final boolean ENABLE_CACHE = true;
    
    private static Field findField(Class<?> searchClass, String name) {
    	ClassCache cc;
    	if(ENABLE_CACHE) {
    		cc = ClassCache.get(searchClass);
    		Field f = cc.field(name);
    		if(f != null) return f;
    	}
    	
    	Field f = findFieldInternal(searchClass, name);
    	if(ENABLE_CACHE) {
    		cc.cache(name, f);
    	}
    	return f;
    }
    private static Method findMethod(Class<?> searchClass, String name, Class<?>[] paramTypes) {
    	ClassCache cc;
    	if(ENABLE_CACHE) {
    		cc = ClassCache.get(searchClass);
    		Method m = cc.method(name, paramTypes);
    		if(m != null) return m;
    	}
    	
    	Method m = findMethodInternal(searchClass, name, paramTypes);
    	if(ENABLE_CACHE) {
    		cc.cache(name, m, paramTypes);
    	}
    	return m;
    }

    public static Class<?>[] parseDescriptor(String descriptor) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        boolean isArray = false;
        for (int i = 0; i < descriptor.length(); i++) {
            switch (descriptor.charAt(i)) {
                case '[':
                    isArray = true;
                    break;
                case 'Z':
                    classes.add(isArray ? boolean[].class : boolean.class);
                    isArray = false;
                    break;
                case 'B':
                    classes.add(isArray ? byte[].class : byte.class);
                    isArray = false;
                    break;
                case 'C':
                    classes.add(isArray ? char[].class : char.class);
                    isArray = false;
                    break;
                case 'S':
                    classes.add(isArray ? short[].class : short.class);
                    isArray = false;
                    break;
                case 'I':
                    classes.add(isArray ? int[].class : int.class);
                    isArray = false;
                    break;
                case 'J':
                    classes.add(isArray ? long[].class : long.class);
                    isArray = false;
                    break;
                case 'F':
                    classes.add(isArray ? float[].class : float.class);
                    isArray = false;
                    break;
                case 'D':
                    classes.add(isArray ? double[].class : double.class);
                    isArray = false;
                    break;
                case 'L':
                    String className = "";
                    while (descriptor.charAt(i + 1) != ';') {
                        className += descriptor.charAt(i + 1);
                        i++;
                    }
                    className = className.replace("/", ".");
                    classes.add(isArray ? Array.newInstance(Class.forName(className),
                            0).getClass() : Class.forName(className));
                    isArray = false;
                    break;
                case ')':
                    return classes.toArray(new Class<?>[0]);

            }
        }
        return classes.toArray(new Class<?>[0]);
    }
}