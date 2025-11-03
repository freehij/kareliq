package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.player.OutOfBody;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;

@Injection("EntityClientPlayerMP")
public class EntityClientPlayerMPInjection {
    @Inject(method = "onUpdateMP")
    public static void onUpdateMP(InjectionHelper helper) {
        if (OutOfBody.INSTANCE.isToggled()) {
            helper.setCancelled(true);
        }
    }
}
