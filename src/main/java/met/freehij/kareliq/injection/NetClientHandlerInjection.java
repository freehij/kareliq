package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.combat.NoKnockBack;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;

@Injection("NetClientHandler")
public class NetClientHandlerInjection {
    @Inject(method = "func_6498_a")
    public static void handlePacket28(InjectionHelper helper) {
        if (NoKnockBack.INSTANCE.isToggled()) helper.setCancelled(true);
    }
}
