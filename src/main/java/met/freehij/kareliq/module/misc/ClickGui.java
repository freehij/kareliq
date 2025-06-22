package met.freehij.kareliq.module.misc;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class ClickGui extends Module {
    public static final ClickGui INSTANCE = new ClickGui();

    protected ClickGui() {
        super("ClickGui", Keys.KEY_RSHIFT);
    }
}
