package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.player.NoPush;
import met.freehij.kareliq.module.world.NoClip;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;

@Injection("Entity")
public class EntityInjection {
    @Inject(method = "isEntityInsideOpaqueBlock")
    public static void isEntityInsideOpaqueBlock(InjectionHelper helper) {
        if (NoClip.INSTANCE.isToggled()) {
            helper.setReturnValue(false);
            helper.setCancelled(true);
        }
    }

    @Inject(method = "handleWaterMovement")
    public static void handleWaterMovement(InjectionHelper helper) {
        if (NoClip.INSTANCE.isToggled()) {
            helper.setReturnValue(false);
            helper.setCancelled(true);
        }
    }

    @Inject(method = "applyEntityCollision")
    public static void applyEntityCollision(InjectionHelper helper) {
        if (NoPush.INSTANCE.isToggled()) {
            helper.setCancelled(true);
        }
    }
}
