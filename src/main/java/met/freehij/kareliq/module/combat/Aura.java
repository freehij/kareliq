package met.freehij.kareliq.module.combat;

import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import met.freehij.kareliq.module.SettingButton;
import org.lwjgl.input.Keyboard;

public class Aura extends Module {
    public static final Aura INSTANCE = new Aura();

    protected Aura() {
        super("Aura", Keyboard.KEY_NONE, Category.COMBAT, new Setting[] {
                new SettingButton("Mobs", true, new Setting[0]),
                new SettingButton("Animals", true, new Setting[0]),
                new SettingButton("Players", true, new Setting[0]),
                new SettingButton("Swing", false, new Setting[0]),
        });
    }
}
