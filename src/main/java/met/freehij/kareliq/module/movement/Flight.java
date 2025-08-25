package met.freehij.kareliq.module.movement;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import org.lwjgl.input.Keyboard;

public class Flight extends Module {
    public static final Flight INSTANCE = new Flight();

    protected Flight() {
        super("Flight", Keyboard.KEY_NONE, Category.MOVEMENT, new Setting[0]);
    }
}
