package met.freehij.loader.util;

import met.freehij.loader.util.mappings.ClassMappings;

public class InjectionHelper {
    private final Class<?> targetClass;
    private final Object instance;
    private final Object[] args;
    private boolean cancelled = false;
    private Object returnValue = null;

    public InjectionHelper(Class<?> targetClass, Object instance, Object[] args) {
        this.targetClass = targetClass;
        this.instance = instance;
        this.args = args;
    }

    public Object getArg(int index) {
        if (index < 1 || index > args.length) {
            throw new IndexOutOfBoundsException("Invalid argument index: " + index);
        }
        return args[index - 1];
    }

    public Reflector getClazz() {
        return new Reflector(targetClass, null);
    }

    public static Reflector getClazz(String className) throws ClassNotFoundException {
        return getClazzRaw(ClassMappings.get(className));
    }

    public static Reflector getClazzRaw(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className.replace("/", "."));
        return new Reflector(clazz, null);
    }

    public Reflector getSelf() {
        return new Reflector(targetClass, instance);
    }

    public static Reflector getMinecraft() {
        try {
            return getClazzRaw("net/minecraft/client/Minecraft").getField("theMinecraft");
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Cannot find game class");
        }
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setReturnValue(Object value) {
        this.returnValue = value;
    }

    public Object getReturnValue() {
        return returnValue;
    }
}