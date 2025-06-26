package met.freehij.kareliq.module.client;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.ReflectionHelper;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Module {
    public static final ClickGui INSTANCE = new ClickGui();

    protected ClickGui() {
        super("ClickGui", Keyboard.KEY_RSHIFT, Category.CLIENT);
    }
    
    @Override
    public void toggle() {
        ReflectionHelper.mc_displayScreen(ReflectionHelper.newClickGUI());
    }
}
