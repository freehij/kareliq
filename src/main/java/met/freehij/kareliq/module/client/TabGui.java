package met.freehij.kareliq.module.client;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import org.lwjgl.input.Keyboard;

public class TabGui extends Module {
    public static final TabGui INSTANCE = new TabGui();

    public TabGui() {
        super("TabGui", Keyboard.KEY_M, Category.CLIENT, new Setting[0]);
    }
}
