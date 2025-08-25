package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.world.FastBreak;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;

@Injection("PlayerControllerMP")
public class PlayerControllerMP {
    @Inject(method = "clickBlock")
    public static void clickBlock(InjectionHelper helper) {
        if (FastBreak.INSTANCE.isToggled()) helper.getSelf().setField("curBlockDamageMP", 1.0F);
    }
}
