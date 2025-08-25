package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.render.Brightness;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;

@Injection("World")
public class WorldInjection {
    @Inject(method = "getLightBrightness")
    public static void getLightBrightness(InjectionHelper helper) {
        if (Brightness.INSTANCE.isToggled()) {
            helper.setReturnValue((float) Brightness.INSTANCE.getSettings()[0].getDouble());
            helper.setCancelled(true);
        }
    }
}
