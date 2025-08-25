package met.freehij.kareliq.module.render;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import met.freehij.loader.util.InjectionHelper;
import org.lwjgl.input.Keyboard;

public class OreViewer extends Module {
    public static final OreViewer INSTANCE = new OreViewer();

    protected OreViewer() {
        super("OreViewer", Keyboard.KEY_NONE, Category.RENDER, new Setting[0]);
    }

    @Override
    public void toggle() {
        if (ClientMain.loaded) InjectionHelper.getMinecraft().getField("renderGlobal").invoke("loadRenderers");
        super.toggle();
    }
}
