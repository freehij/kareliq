package met.freehij.kareliq;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.combat.Aura;
import met.freehij.kareliq.module.combat.NoKnockBack;
import met.freehij.kareliq.module.misc.ClickGui;
import met.freehij.kareliq.module.misc.FastBreak;
import met.freehij.kareliq.module.movement.NoFallDamage;
import met.freehij.kareliq.module.movement.Flight;
import met.freehij.kareliq.module.movement.LiquidWalk;
import met.freehij.kareliq.module.render.FullBright;
import met.freehij.kareliq.module.render.ModuleList;
import met.freehij.kareliq.module.render.OreViewer;

import java.util.ArrayList;
import java.util.List;

public class ClientMain {
    public static Module[] modules;

    public static void startClient() {
        List<Module> moduleList = new ArrayList<>();
        moduleList.add(NoFallDamage.INSTANCE);
        moduleList.add(NoKnockBack.INSTANCE);
        moduleList.add(FastBreak.INSTANCE);
        moduleList.add(ModuleList.INSTANCE);
        moduleList.add(OreViewer.INSTANCE);
        moduleList.add(FullBright.INSTANCE);
        moduleList.add(LiquidWalk.INSTANCE);
        moduleList.add(ClickGui.INSTANCE);
        moduleList.add(Flight.INSTANCE);
        moduleList.add(Aura.INSTANCE);

        modules = moduleList.toArray(new Module[0]);
    }
}
