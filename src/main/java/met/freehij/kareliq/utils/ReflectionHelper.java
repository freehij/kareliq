package met.freehij.kareliq.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionHelper {
    /*
    roughly equivalent to:
    net.minecraft.client.Minecraft.theMinecraft;
    */
    private static Object getMinecraft() throws ClassNotFoundException, IllegalAccessException {
        Class<?> minecraft = Class.forName("net.minecraft.client.Minecraft");
        Field field = getField(minecraft, "a");
        field.setAccessible(true);
        return field.get(null);
    }

    /*
    roughly equivalent to:
    net.minecraft.client.Minecraft.theMinecraft.renderGlobal.updateAllRenderers();
    net.minecraft.client.Minecraft.theMinecraft.renderGlobal.loadRenderers();
    */
    public static void callUpdateRenderers() {
        try {
            Object mc = getMinecraft();
            Field renderGlobalField = getField(mc.getClass(), "g");
            Object renderGlobal = renderGlobalField.get(mc);
            getMethod(renderGlobal.getClass(), new Class[]{}, "a").invoke(renderGlobal);
            getMethod(renderGlobal.getClass(), new Class[]{}, "e").invoke(renderGlobal);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static Field getField(Class c, String... names) {
        for(String name : names) {
            try {
                return c.getDeclaredField(name);
            } catch (Exception ignored) {}
        }
        if(c.getSuperclass() != null) return getField(c.getSuperclass(), names);
        throw new RuntimeException("Failed to find a field with names "+ Arrays.toString(names));
    }

    public static Method getMethod(Class c, Class[] params, String... names) {
        for(String name : names) {
            try {
                return c.getDeclaredMethod(name, params);
            } catch (Exception ignored) {}
        }
        if(c.getSuperclass() != null) return getMethod(c.getSuperclass(), params, names);
        throw new RuntimeException("Failed to find a method with names "+Arrays.toString(names));
    }
}