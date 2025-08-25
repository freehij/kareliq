package met.freehij.kareliq.module.world;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import org.lwjgl.input.Keyboard;

public class WaterWalking extends Module {
    public static final WaterWalking INSTANCE = new WaterWalking();

    protected WaterWalking() {
        super("WaterWalking", Keyboard.KEY_NONE, Category.WORLD, new Setting[0]);
    }
}
