package met.freehij.kareliq.module.render;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;
import met.freehij.kareliq.utils.ReflectionHelper;

public class FullBright extends Module {
    public static final FullBright INSTANCE = new FullBright();

    protected FullBright() {
        super("FullBright", Keys.KEY_B);
    }

    @Override
    public void toggle() {
        ReflectionHelper.callUpdateRenderers();
        super.toggle();
    }
}
