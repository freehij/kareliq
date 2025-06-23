package met.freehij.kareliq.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import met.freehij.kareliq.injection.ByteClassLoader;
import met.freehij.kareliq.utils.mappings.ClassMappings;
import met.freehij.kareliq.utils.mappings.MappingResolver;

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


	public static void mc_displayScreen(Object guiScreen) {
		try {
			Object mc = getMinecraft();
			
			Method displayGuiScreen = getMethod(mc.getClass(), new Class[] {Class.forName("net.minecraft.src.GuiScreen")}, "displayGuiScreen"); //no deobf support =<
			displayGuiScreen.invoke(mc, guiScreen);
		} catch (Exception exception) {
            exception.printStackTrace();
        }
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

	public static Object newClickGUI() {
		try {
			ClassWriter cw = new ClassWriter(0);
			String className = "met/liza/ClickGUI";
		    String guiscreen = MappingResolver.resolveClass(ClassMappings.GUI_SCREEN);
		    cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, className, null, guiscreen, null);
		    {
		    	MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		    	mv.visitVarInsn(Opcodes.ALOAD, 0);
		    	mv.visitMethodInsn(Opcodes.INVOKESPECIAL, guiscreen, "<init>", "()V", false);
		    	mv.visitInsn(Opcodes.RETURN);
		    	mv.visitMaxs(1, 1);
		    	mv.visitEnd();
		    }
		    
			{
			    MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "drawScreen", "(IIF)V", null, null);
			    mv.visitVarInsn(Opcodes.ALOAD, 0);
			    mv.visitVarInsn(Opcodes.ILOAD, 1);
			    mv.visitVarInsn(Opcodes.ILOAD, 2);
			    mv.visitVarInsn(Opcodes.FLOAD, 3);
			    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "met/freehij/kareliq/ClientMain", "ClickGUI_drawScreen", "(Ljava/lang/Object;IIF)V", false);
			    
			    mv.visitVarInsn(Opcodes.ALOAD, 0);
			    mv.visitVarInsn(Opcodes.ILOAD, 1);
			    mv.visitVarInsn(Opcodes.ILOAD, 2);
			    mv.visitVarInsn(Opcodes.FLOAD, 3);
			    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, guiscreen, "drawScreen", "(IIF)V", false);
			    mv.visitInsn(Opcodes.RETURN);
			    mv.visitMaxs(4, 4);
			    mv.visitEnd();
		    }
			
			{
			    MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "initGui", "()V", null, null);
			    mv.visitVarInsn(Opcodes.ALOAD, 0);
			    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "met/freehij/kareliq/ClientMain", "ClickGUI_initGui", "(Ljava/lang/Object;)V", false);
			    mv.visitInsn(Opcodes.RETURN);
			    mv.visitMaxs(1, 1);
			    mv.visitEnd();
		    }
			
			{
			    MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "actionPerformed", "(Lnet/minecraft/src/GuiButton;)V", null, null);
			    mv.visitVarInsn(Opcodes.ALOAD, 0);
			    mv.visitVarInsn(Opcodes.ALOAD, 1);
			    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "met/freehij/kareliq/ClientMain", "ClickGUI_actionPerformed", "(Ljava/lang/Object;Ljava/lang/Object;)V", false);
			    mv.visitInsn(Opcodes.RETURN);
			    mv.visitMaxs(2, 2);
			    mv.visitEnd();
		    }
			
		    cw.visitEnd();
		    byte[] classbytes = cw.toByteArray();
		    Class<?> clz = new ByteClassLoader(classbytes).findClass(className.replace("/", "."));
		    return clz.newInstance();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	


	public static Object createButton(int id, int x, int y, int wid, int hei, String name) {
		try {
			Class<?> gb = Class.forName("net.minecraft.src.GuiButton");
			return gb.getConstructor(int.class, int.class, int.class, int.class, int.class, String.class).newInstance(id, x, y, wid, hei, name);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	


	public static int GuiButton_id(Object butt) {
		try {
			return getField(butt.getClass(), "id").getInt(butt);
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static ArrayList GuiScreen_controlList(Object gs) {
		try {
            Field controlList = getField(gs.getClass(), "controlList");
            controlList.setAccessible(true);
           return (ArrayList) controlList.get(gs);
		} catch (Exception exception) {
            exception.printStackTrace();
            return null;
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