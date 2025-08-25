package met.freehij.kareliq.injection;

import met.freehij.kareliq.ClientMain;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;
import met.freehij.loader.util.mappings.ClassMappings;
import met.freehij.loader.util.mappings.MethodMappings;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

@Injection(ClassMappings.MINECRAFT)
public class MinecraftInjection {
    @Inject(method = "lineIsCommand")
    public static void lineIsCommand(InjectionHelper helper) {
        String cmd = (String) helper.getArg(1);
        if (cmd.startsWith(";")) {
            helper.setReturnValue(ClientMain.handleCommand(cmd));
            helper.setCancelled(true);
        }
    }

    @Inject(method = "runTick")
    public static void runTick(InjectionHelper helper) throws ClassNotFoundException, InstantiationException, IllegalAccessException { //todo fix t key stuck in sp
        if (helper.getSelf().getField("currentScreen").get() == null && !((boolean) helper.getSelf().invoke("isMultiplayerWorld").get())) {
            Object keyBinding = ((Object[]) helper.getSelf().getField("gameSettings").getField("keyBindings").get())[8];
            if (Keyboard.isKeyDown((int) new Reflector(keyBinding.getClass(), keyBinding).getField("keyCode").get())) {
                helper.getSelf().invoke("displayGuiScreen", InjectionHelper.getClazz("GuiChat").getActualClass().newInstance());
            }
        }
    }
}
