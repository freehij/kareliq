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
        Field field = getField(minecraft, "a", "theMinecraft");
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
            Field renderGlobalField = getField(mc.getClass(), "g", "renderGlobal");
            Object renderGlobal = renderGlobalField.get(mc);
            getMethod(renderGlobal.getClass(), new Class[]{}, "a", "updateAllRenderers").invoke(renderGlobal);
            getMethod(renderGlobal.getClass(), new Class[]{}, "e", "loadRenderers").invoke(renderGlobal);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
	public static void FontRenderer_drawString(String txt, int x, int y, int color) {
		try {
            Object mc = getMinecraft();
            Field fontRendererFld = getField(mc.getClass(), "q", "fontRenderer");
            Object fontRenderer = fontRendererFld.get(mc);
            getMethod(fontRenderer.getClass(), new Class[]{String.class, int.class, int.class, int.class}, "a", "drawStringWithShadow").invoke(fontRenderer, txt, x, y, color);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}
	
	public static int ScaledResolution_getScaledWidth(Object scaledResolution) {
		try {
			return (int) getMethod(scaledResolution.getClass(), new Class[] {}, "a", "getScaledWidth").invoke(scaledResolution);
		} catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
	}
	
	public static int FontRenderer_getStringWidth(String s) {
		try {
			Object mc = getMinecraft();
            Field fontRendererFld = getField(mc.getClass(), "q", "fontRenderer");
            Object fontRenderer = fontRendererFld.get(mc);
			return (int) getMethod(fontRenderer.getClass(), new Class[] {String.class}, "a", "getStringWidth").invoke(fontRenderer, s);
		} catch (Exception exception) {
            exception.printStackTrace();
            return 0;
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