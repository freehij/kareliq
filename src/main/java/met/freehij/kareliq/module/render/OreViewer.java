package met.freehij.kareliq.module.render;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class OreViewer extends Module {
    public static final OreViewer INSTANCE = new OreViewer();

    protected OreViewer() {
        super("OreViewer", Keys.KEY_X);
    }
}
