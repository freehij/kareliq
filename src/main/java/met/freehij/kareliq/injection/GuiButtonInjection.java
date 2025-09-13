package met.freehij.kareliq.injection;

import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;
import met.freehij.loader.util.mappings.ClassMappings;

@Injection("Gui")
public class GuiButtonInjection {
    public static byte buttonMode = 0;

    @Inject(method = "drawTexturedModalRect")
    public static void drawTexturedModalRect(InjectionHelper helper) {
        if (buttonMode == 0) return;
        if (helper.getSelf().get().getClass().getName().replace(".", "/").equals(ClassMappings.get("GuiButton"))) {
            Reflector reflector = new Reflector(helper.getSelf().get().getClass(), helper.getSelf().get());
            int xPosition = reflector.getField("xPosition").getInt();
            int yPosition = reflector.getField("yPosition").getInt();
            helper.getSelf().invoke("drawRect",
                    xPosition, yPosition,
                    xPosition + reflector.getField("width").getInt(), yPosition + reflector.getField("height").getInt(),
                    Integer.MIN_VALUE);
            helper.setCancelled(true);
        }
    }
}
