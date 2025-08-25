package met.freehij.kareliq.module.render;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import met.freehij.kareliq.module.SettingSlider;
import met.freehij.loader.util.InjectionHelper;
import org.lwjgl.input.Keyboard;

public class Brightness extends Module {
    public static final Brightness INSTANCE = new Brightness();

    protected Brightness() {
        super("Brightness", Keyboard.KEY_NONE, Category.RENDER, new Setting[] {
                new SettingSlider("Value", 1.D, 0.0D, 1.D, 0.1D)
        });
    }

    @Override
    public void toggle() {
        if (ClientMain.loaded) InjectionHelper.getMinecraft().getField("renderGlobal").invoke("loadRenderers");
        super.toggle();
    }

    @Override
    public void onSettingChange(Setting setting) {
        if (ClientMain.loaded) InjectionHelper.getMinecraft().getField("renderGlobal").invoke("loadRenderers");
    }
}
