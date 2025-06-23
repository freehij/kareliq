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
import met.freehij.kareliq.utils.ReflectionHelper;

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
    
    public static void handleKeypress(int key, boolean pressed) {
    	if (pressed) {
            for (Module module : ClientMain.modules) {
                if (module.getKeyBind() == key) module.toggle();
            }
        }
    }

    /*
    roughly equivalent to:
    this.mc.fontRenderer.drawStringWithShadow();

    int i = 2;
    net.minecraft.src.ScaledResolution scaledResolution = new net.minecraft.src.ScaledResolution (
        this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight
    );
    for (met.freehij.kareliq.module.Module module : met.freehij.kareliq.ClientMain.modules) {
        if (module.isToggled()) {
            this.mc.fontRenderer.drawStringWithShadow(
                module.getName(),
                scaledResolution.getScaledWidth() - this.mc.fontRenderer.getStringWidth(module.getName()),
                i, Integer.MAX_VALUE
            );
            i += 10;
        }
    }
    */
    public static void renderGuiIngame(Object guiIngame, Object scaledResolution) {
    	ReflectionHelper.FontRenderer_drawString("§6kareliq", 2, 2, Integer.MAX_VALUE);
        if (!ModuleList.INSTANCE.isToggled()) return;
    	int i = 2;
    	for (Module module : ClientMain.modules) {
    		if(module.isToggled()) {
    			String s = module.getName();
    			int swid = ReflectionHelper.FontRenderer_getStringWidth(s);
    			int x = ReflectionHelper.ScaledResolution_getScaledWidth(scaledResolution) - swid;
    			ReflectionHelper.FontRenderer_drawString(s, x, i, Integer.MAX_VALUE); //used same method in bytecode as drawString, but the roughly equivalent to shows drawStringWithShadow?
    			i += 10;
    		}
    	}
    }
}
