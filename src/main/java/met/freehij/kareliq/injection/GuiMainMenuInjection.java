package met.freehij.kareliq.injection;

import met.freehij.kareliq.util.BackgroundUtils;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Injection("GuiMainMenu")
public class GuiMainMenuInjection {
    @Inject(method = "initGui")
    public static void initGui(InjectionHelper helper) throws ClassNotFoundException {
        ((List) helper.getSelf().getField("controlList").get()).add(
                InjectionHelper.getClazz("GuiButton").newInstance("IIIIILjava/lang/String;", 67, 2,
                        helper.getSelf().getField("height").getInt() - 22, 100, 20, "Main menu settings").get());
    }

    @Inject(method = "actionPerformed")
    public static void actionPerformed(InjectionHelper helper) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Object button = helper.getArg(1);
        if (new Reflector(button.getClass(), button).getField("id").getInt() == 67) {
            InjectionHelper.getMinecraft().invoke("displayGuiScreen", BackgroundUtils.guiEditMainMenu.newInstance());
        }
    }
}
