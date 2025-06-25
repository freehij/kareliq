package met.freehij.kareliq.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.injection.InjectionMain;
import met.freehij.kareliq.module.misc.FastBreak;
import met.freehij.kareliq.utils.mappings.FieldMappings;
import met.freehij.kareliq.utils.mappings.MethodMappings;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import met.freehij.kareliq.injection.ByteClassLoader;
import met.freehij.kareliq.utils.mappings.ClassMappings;
import met.freehij.kareliq.utils.mappings.MappingResolver;

public class ReflectionHelper {
	private static String guiScreenClass;
	private static String displayGuiScreenMethod;
	private static String drawScreen;
	private static String initGui;
	private static String guiButtonClass;
	private static String buttonId;
	private static String controlListField;
	private static String actionPerformed;
	private static String sendBlockRemovedMethod;
	private static String packetClass;
	private static String addToSendQueueMethod;
	private static String packet14Class;
    private static String gameSettingsField;
	private static String keyBindingsField;
	private static String keyCodeField;
	private static String guiChatClass;
	private static String guiControlsClass;

    public static void initHelper() {
		guiScreenClass = MappingResolver.resolveClass(ClassMappings.GUI_SCREEN);
		displayGuiScreenMethod = MappingResolver.resolveMethod("net/minecraft/client/Minecraft",
				"(L" + guiScreenClass + ";)V", MethodMappings.DISPLAY_SCREEN);
		drawScreen = MappingResolver.resolveMethod(guiScreenClass, "(IIF)V", MethodMappings.DRAW_SCREEN);
		initGui = MappingResolver.resolveMethod(guiScreenClass, "()V", MethodMappings.INIT_GUI);
		guiButtonClass = MappingResolver.resolveClass(ClassMappings.GUI_BUTTON);
		buttonId = MappingResolver.resolveField(guiButtonClass, "I", FieldMappings.BUTTON_ID);
		controlListField = MappingResolver.resolveField(guiScreenClass, "Ljava/util/List;", FieldMappings.CONTROL_LIST);
		actionPerformed = MappingResolver.resolveMethod(guiScreenClass, "(L" + guiButtonClass + ";)V",
				MethodMappings.ACTION_PERFORMED);
        String playerControllerClass = MappingResolver.resolveClass(ClassMappings.PLAYER_CONTROLLER);
		sendBlockRemovedMethod = MappingResolver.resolveMethod(playerControllerClass, "(IIII)Z",
				MethodMappings.SEND_BLOCK_REMOVED);
		packetClass = MappingResolver.resolveClass(ClassMappings.PACKET);
        String netClientHandlerClass = MappingResolver.resolveClass(ClassMappings.NET_CLIENT_HANDLER);
		addToSendQueueMethod = MappingResolver.resolveMethod(netClientHandlerClass, "(L" + packetClass + ";)V", MethodMappings.ADD_TO_SEND_QUEUE);
		packet14Class = MappingResolver.resolveClass(ClassMappings.PACKET14);
		String gameSettingsClass = MappingResolver.resolveClass(ClassMappings.GAME_SETTINGS);
		gameSettingsField = MappingResolver.resolveField("net/minecraft/client/Minecraft",
				"L" + gameSettingsClass + ";", FieldMappings.GAME_SETTINGS);
        String keyBindingClass = MappingResolver.resolveClass(ClassMappings.KEY_BINDING);
		keyBindingsField = MappingResolver.resolveField(gameSettingsClass, "[L" + keyBindingClass + ";",
				FieldMappings.KEY_BINDINGS);
		keyCodeField = MappingResolver.resolveField(keyBindingClass, "I", FieldMappings.KEY_CODE);
		guiChatClass = MappingResolver.resolveClass(ClassMappings.GUI_CHAT);
		guiControlsClass = MappingResolver.resolveClass(ClassMappings.GUI_CONTROLS);
	}

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

			Method displayGuiScreen = getMethod(mc.getClass(), new Class[] {Class.forName(guiScreenClass.replace("/", "."))},
					displayGuiScreenMethod);
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
            getMethod(fontRenderer.getClass(), new Class[]{String.class, int.class, int.class, int.class}, "a",
					"drawStringWithShadow").invoke(fontRenderer, txt, x, y, color);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}

	public static int ScaledResolution_getScaledWidth(Object scaledResolution) {
		try {
			return (int) getMethod(scaledResolution.getClass(), new Class[] {}, "a",
					"getScaledWidth").invoke(scaledResolution);
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
			return (int) getMethod(fontRenderer.getClass(), new Class[] {String.class}, "a",
					"getStringWidth").invoke(fontRenderer, s);
		} catch (Exception exception) {
            exception.printStackTrace();
            return 0;
        }
	}

	public static Object newClickGUI() {
		try {
			ClassWriter cw = new ClassWriter(0);
			String className = "met/liza/ClickGUI";
		    cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC, className, null, guiScreenClass, null);
		    {
		    	MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		    	mv.visitVarInsn(Opcodes.ALOAD, 0);
		    	mv.visitMethodInsn(Opcodes.INVOKESPECIAL, guiScreenClass, "<init>", "()V", false);
		    	mv.visitInsn(Opcodes.RETURN);
		    	mv.visitMaxs(1, 1);
		    	mv.visitEnd();
		    }

			{
			    MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, drawScreen, "(IIF)V", null, null);
			    mv.visitVarInsn(Opcodes.ALOAD, 0);
			    mv.visitVarInsn(Opcodes.ILOAD, 1);
			    mv.visitVarInsn(Opcodes.ILOAD, 2);
			    mv.visitVarInsn(Opcodes.FLOAD, 3);
			    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "met/freehij/kareliq/ClientMain", "ClickGUI_drawScreen",
						"(Ljava/lang/Object;IIF)V", false);

			    mv.visitVarInsn(Opcodes.ALOAD, 0);
			    mv.visitVarInsn(Opcodes.ILOAD, 1);
			    mv.visitVarInsn(Opcodes.ILOAD, 2);
			    mv.visitVarInsn(Opcodes.FLOAD, 3);
			    mv.visitMethodInsn(Opcodes.INVOKESPECIAL, guiScreenClass, drawScreen, "(IIF)V", false);
			    mv.visitInsn(Opcodes.RETURN);
			    mv.visitMaxs(4, 4);
			    mv.visitEnd();
		    }

			{
			    MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, initGui, "()V", null, null);
			    mv.visitVarInsn(Opcodes.ALOAD, 0);
			    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "met/freehij/kareliq/ClientMain", "ClickGUI_initGui",
						"(Ljava/lang/Object;)V", false);
			    mv.visitInsn(Opcodes.RETURN);
			    mv.visitMaxs(1, 1);
			    mv.visitEnd();
		    }

			{
			    MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, actionPerformed,
						"(L" + guiButtonClass + ";)V", null, null);
			    mv.visitVarInsn(Opcodes.ALOAD, 0);
			    mv.visitVarInsn(Opcodes.ALOAD, 1);
			    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "met/freehij/kareliq/ClientMain", "ClickGUI_actionPerformed",
						"(Ljava/lang/Object;Ljava/lang/Object;)V", false);
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
			Class<?> gb = Class.forName(guiButtonClass.replace("/", "."));
			return gb.getConstructor(int.class, int.class, int.class, int.class, int.class,
					String.class).newInstance(id, x, y, wid, hei, name);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static int GuiButton_id(Object butt) {
		try {
			return getField(butt.getClass(), buttonId).getInt(butt);
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static ArrayList GuiScreen_controlList(Object gs) {
		try {
            Field controlList = getField(gs.getClass(), controlListField);
            controlList.setAccessible(true);
           return (ArrayList) controlList.get(gs);
		} catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
	}

	public static void updateMovementKeyStates(Object player) throws ClassNotFoundException,
			IllegalAccessException, NoSuchFieldException { //TODO: fix key stuck for pausing guis
		Object mc = getMinecraft();

		Object currentScreen = getField(mc.getClass(), "currentScreen", "r").get(mc);
		if (currentScreen != null) {
			Class<?> guiChat = Class.forName(guiChatClass.replace("/", "."));
			Class<?> guiControls = Class.forName(guiControlsClass.replace("/", "."));
			if (currentScreen.getClass().isAssignableFrom(guiChat)
					|| currentScreen.getClass().isAssignableFrom(guiControls)) {
				return;
			}
		}

		Object gameSettings = getField(mc.getClass(), gameSettingsField).get(mc);
		Object[] keyBindings = (Object[]) getField(gameSettings.getClass(), keyBindingsField).get(gameSettings);
		boolean[] tspmo = new boolean[] {
				Keyboard.isKeyDown(keyBindings[0].getClass().getDeclaredField(keyCodeField).getInt(keyBindings[0])),
				Keyboard.isKeyDown(keyBindings[2].getClass().getDeclaredField(keyCodeField).getInt(keyBindings[2])),
				Keyboard.isKeyDown(keyBindings[1].getClass().getDeclaredField(keyCodeField).getInt(keyBindings[1])),
				Keyboard.isKeyDown(keyBindings[3].getClass().getDeclaredField(keyCodeField).getInt(keyBindings[3])),
				Keyboard.isKeyDown(keyBindings[4].getClass().getDeclaredField(keyCodeField).getInt(keyBindings[4])),
				Keyboard.isKeyDown(keyBindings[5].getClass().getDeclaredField(keyCodeField).getInt(keyBindings[5])),
				false, false, false, false //goofy ahh notch making it 10 for absolutely no fucking reason
		};

		Object movementInput = getField(player.getClass(), "movementInput", "a").get(player);
		Field movementKeyStates = getField(Class.forName(InjectionMain.movementInputClass.replace("/", ".")),
				"movementKeyStates", "f");
		movementKeyStates.setAccessible(true);
		movementKeyStates.set(movementInput, tspmo);
	}

	public static void awtysm_method(Object playerController, int x, int y, int z, int side) {
		if (!FastBreak.INSTANCE.isToggled()) return;
		try {
			playerController.getClass().getDeclaredMethod(sendBlockRemovedMethod, int.class, int.class, int.class, int.class).invoke(
					playerController, x, y, z, side);
			sendBlockRemovedPacket(x, y, z, side, 0);
			sendBlockRemovedPacket(x, y, z, side, 2);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static void sendBlockRemovedPacket(int x, int y, int z, int side, int state) {
		try {
			Object mc = getMinecraft();
			Object sendQueue = getMethod(mc.getClass(), new Class[0], "getSendQueue", "func_20001_q", "s").invoke(mc);
			if (sendQueue != null) {
				Method m = getMethod(sendQueue.getClass(), new Class[]{Class.forName(packetClass.replace("/", "."))},
						addToSendQueueMethod);
				m.invoke(sendQueue, Class.forName(packet14Class.replace("/", ".")).getConstructor(int.class, int.class,
						int.class, int.class, int.class).newInstance(state, x, y, z, side));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setButtonText(Object button, String text) { //TODO: dont forget about this method existing
		try {
			getField(button.getClass(), "displayString", "e").set(button, text);
		} catch (Exception ignored) {}
	}

	public static Object createBoundingBox() { //TODO: and this one too
		return new Object();
	}

	public static void addChatMessage(String message) {
		addChatMessage(message, false);
	}

	public static void addChatMessage(String message, boolean prefix) {
		try {
			Object mc = getMinecraft();
			Object inGameGui = getField(mc.getClass(), "ingameGUI", "v").get(mc);
			getMethod(inGameGui.getClass(), new Class[]{String.class}, "func_22064_c", "c").invoke(inGameGui,
					(prefix ? "[" + ClientMain.name + "§f] " : "") + message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void gui_drawRect(Object gui, int x1, int y1, int x2, int y2, int color) {
		try {
			Method drawRect = getMethod(gui.getClass(), new Class[]{int.class,int.class,int.class,int.class,int.class},
					"drawRect","a");
			drawRect.setAccessible(true);
			drawRect.invoke(gui, x1, y1, x2, y2, color);
		} catch (Exception ex) {
			ex.printStackTrace();
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