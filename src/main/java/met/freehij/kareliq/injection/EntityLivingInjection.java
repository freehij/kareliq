package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.combat.NoKnockBack;
import met.freehij.kareliq.module.movement.Flight;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;

@Injection("EntityLiving")
public class EntityLivingInjection {
    @Inject(method = "jump")
    public static void jump(InjectionHelper helper) {
        if (Flight.INSTANCE.isToggled()) helper.setCancelled(true);
    }
}
