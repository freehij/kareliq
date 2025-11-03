package met.freehij.kareliq.module.player;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.module.Module;
import met.freehij.kareliq.module.Setting;
import met.freehij.kareliq.module.SettingButton;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;
import org.lwjgl.input.Keyboard;

public class OutOfBody extends Module {
    public static final OutOfBody INSTANCE = new OutOfBody();
    public static Double prevX, prevY, prevZ;

    protected OutOfBody() {
        super("OutOfBody", Keyboard.KEY_NONE, Category.PLAYER, new Setting[] {
                new SettingButton("ResetPosition", true, new Setting[0])
        });
    }

    @Override
    public void toggle() {
        super.toggle();
        if (!ClientMain.loaded) return;
        Reflector player = InjectionHelper.getMinecraft().getField("thePlayer");
        if (this.isToggled()) {
            prevX = player.getField("posX").getDouble();
            prevY = player.getField("posY").getDouble();
            prevZ = player.getField("posZ").getDouble();
        } else {
            if (prevX != null && prevY != null && prevZ != null) player.invoke("setPosition", prevX, prevY, prevZ);
        }
    }
}
