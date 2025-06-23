package met.freehij.kareliq.module.misc;

import met.freehij.kareliq.module.Module;
import org.lwjgl.input.Keyboard;

public class FastBreak extends Module {
    public static final FastBreak INSTANCE = new FastBreak();

    public FastBreak() {
        super("FastBreak", Keyboard.KEY_H);
    }
}
