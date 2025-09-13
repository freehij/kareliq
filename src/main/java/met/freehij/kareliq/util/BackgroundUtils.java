package met.freehij.kareliq.util;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.injection.GuiButtonInjection;
import met.freehij.loader.util.ClassBuilder;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;
import met.freehij.loader.util.mappings.ClassMappings;
import met.freehij.loader.util.mappings.MethodMappings;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

public class BackgroundUtils {
    public static String fileName = "/gui/background.png";
    public static int textureId = 0;
    public static Class<?> guiEditMainMenu;

    private static Reflector backgroundPath;
    private static String errorMessage = null;

    public static void generateClass() {
        String guiScreen = ClassMappings.get("GuiScreen");
        guiEditMainMenu = new ClassBuilder(BackgroundUtils.class.getClassLoader(), guiScreen)
                .overrideMethod(MethodMappings.get(guiScreen, "initGui")[0], "()V",
                        "met/freehij/kareliq/util/BackgroundUtils", "initGui", false)
                .overrideMethod(MethodMappings.get(guiScreen, "onGuiClosed")[0], "()V",
                        "met/freehij/kareliq/util/BackgroundUtils", "onGuiClosed", false)
                .overrideMethod(MethodMappings.get(guiScreen, "actionPerformed")[0],
                        "(L" + ClassMappings.get("GuiButton") +";)V", "met/freehij/kareliq/util/BackgroundUtils",
                        "actionPerformed", false)
                .overrideMethod(MethodMappings.get(guiScreen, "drawScreen")[0], "(IIF)V",
                        "met/freehij/kareliq/util/BackgroundUtils", "drawScreen", false)
                .overrideMethod(MethodMappings.get(guiScreen, "mouseClicked")[0], "(III)V",
                        "met/freehij/kareliq/util/BackgroundUtils", "mouseClicked", true)
                .overrideMethod(MethodMappings.get(guiScreen, "keyTyped")[0], "(CI)V",
                        "met/freehij/kareliq/util/BackgroundUtils", "keyTyped", true)
                .overrideMethod(MethodMappings.get(guiScreen, "updateScreen")[0], "()V",
                        "met/freehij/kareliq/util/BackgroundUtils", "updateScreen", false)
                .build();
    }

    public static void initGui(Object instance, Object[] args) throws ClassNotFoundException {
        Keyboard.enableRepeatEvents(true);
        Reflector reflector = new Reflector(instance.getClass(), instance);
        List controlList = (List) reflector.getField("controlList").get();
        int height = reflector.getField("height").getInt();
        int width = reflector.getField("width").getInt();
        controlList.add(InjectionHelper.getClazz("GuiButton").newInstance("IIIIILjava/lang/String;", 0, 2, height - 22,
                52, 20, "Back").get());
        controlList.add(InjectionHelper.getClazz("GuiButton").newInstance("IIIIILjava/lang/String;", 1, width / 2 - 100,
               height / 4 + 132, 98, 20, "Default").get());
        controlList.add(InjectionHelper.getClazz("GuiButton").newInstance("IIIIILjava/lang/String;", 2, width / 2 + 2,
                height / 4 + 132, 98, 20, "Apply").get());
        controlList.add(InjectionHelper.getClazz("GuiButton").newInstance("IIIIILjava/lang/String;", 3, width - 152,
                height - 22, 150, 20, "Button style: " + buttonMode()).get());
        backgroundPath = InjectionHelper.getClazz("GuiTextField").newInstance(
                "L" + ClassMappings.get("GuiScreen") + ";L" + ClassMappings.get("FontRenderer")
                        + ";IIIILjava/lang/String;", instance,
                InjectionHelper.getMinecraft().getField("fontRenderer").get(), width / 2 - 175, height / 4 + 58, 350,
                20, fileName);
        backgroundPath.invoke("setMaxStringLength", Integer.MAX_VALUE);
    }

    public static void onGuiClosed(Object instance, Object[] args) {
        Keyboard.enableRepeatEvents(false);
    }

    public static void actionPerformed(Object instance, Object[] args) {
        errorMessage = null;
        Reflector reflector = new Reflector(args[0].getClass(), args[0]);
        switch (reflector.getField("id").getInt()) {
            case 0:
                InjectionHelper.getMinecraft().invoke("displayGuiScreen", (Object) null);
                break;
            case 1:
                fileName = "/gui/background.png";
                backgroundPath.invoke("setText", fileName);
                break;
            case 2:
                String prev = fileName;
                fileName = backgroundPath.invoke("getText").getString();
                if (!loadTexture()) {
                    fileName = prev;
                    errorMessage = "§cImage does not exist/format not supported.";
                    break;
                }
                InjectionHelper.getMinecraft().invoke("displayGuiScreen", (Object) null);
                break;
            case 3:
                ClientMain.saveBackgroundPath();
                GuiButtonInjection.buttonMode = (byte) (GuiButtonInjection.buttonMode == 0 ? 1 : 0);
                reflector.setField("displayString", "Button style: " + buttonMode());
                break;
        }
    }

    public static void drawScreen(Object instance, Object[] args) {
        Reflector reflector = new Reflector(instance.getClass(), instance);
        reflector.invoke("drawDefaultBackground");
        backgroundPath.invoke("drawTextBox");
        InjectionHelper.getMinecraft().getField("fontRenderer").invoke("drawStringWithShadow", errorMessage,
                reflector.getField("width").getInt()/ 2 - 175, reflector.getField("height").getInt() / 2 - 100,
                0xffffff);
        for (Object obj : (List) reflector.getField("controlList").get()) {
            new Reflector(obj.getClass(), obj).invoke("drawButton", InjectionHelper.getMinecraft().get(), args[0], args[1]);
        }
    }

    public static void mouseClicked(Object instance, Object[] args) {
        backgroundPath.invoke("mouseClicked", args[0], args[1], args[2]);
    }

    public static void keyTyped(Object instance, Object[] args) {
        backgroundPath.invoke("textboxKeyTyped", args[0], args[1]);
    }

    public static void updateScreen(Object instance, Object[] args) {
        backgroundPath.invoke("updateCursorCounter");
    }

    static String buttonMode() {
        return GuiButtonInjection.buttonMode == 0 ? "Vanilla" : "Custom";
    }

    public static boolean loadTexture() {
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            if (image == null) return false;

            int width = image.getWidth();
            int height = image.getHeight();
            int[] pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);

            ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }
            buffer.flip();

            textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            ClientMain.saveBackgroundPath();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
