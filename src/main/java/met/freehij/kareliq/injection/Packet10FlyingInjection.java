package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.player.NoFallDamage;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;

@Injection("Packet10Flying")
public class Packet10FlyingInjection {
    @Inject(method = "writePacketData")
    public static void writePacketData(InjectionHelper helper) {
        if (NoFallDamage.INSTANCE.isToggled()) helper.getSelf().setField("onGround", true);
    }
}
