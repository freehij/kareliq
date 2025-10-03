package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.render.WallHack;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.constant.At;
import met.freehij.loader.util.InjectionHelper;
import org.lwjgl.opengl.GL11;

@Injection("RenderGlobal")
public class RenderGlobalInjection {
    @Inject(method = "renderEntities")
    public static void renderEntities1(InjectionHelper helper) {
        if (WallHack.INSTANCE.isToggled()) GL11.glDepthRange(0, 0.1);
    }

    @Inject(method = "renderEntities", at = At.RETURN)
    public static void renderEntities2(InjectionHelper helper) {
        if (WallHack.INSTANCE.isToggled()) GL11.glDepthRange(0, 1);
    }
}
