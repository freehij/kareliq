package met.freehij.kareliq.module.render;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.ReflectionHelper;
import org.lwjgl.input.Keyboard;

public class FullBright extends Module {
    public static final FullBright INSTANCE = new FullBright();

    protected FullBright() {
        super("FullBright", Keyboard.KEY_B);
    }

    @Override
    public void toggle() {
        ReflectionHelper.callUpdateRenderers();
        super.toggle();
    }
}
