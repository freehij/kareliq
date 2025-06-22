package met.freehij.kareliq.module.render;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.utils.Keys;

public class ModuleList extends Module {
    public static final ModuleList INSTANCE = new ModuleList();

    protected ModuleList() {
        super("ModuleList", Keys.KEY_Y);
    }
}
