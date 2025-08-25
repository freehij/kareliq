package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.render.Brightness;
import met.freehij.kareliq.module.world.WaterWalking;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;

import java.lang.reflect.InvocationTargetException;

@Injection("BlockFluid")
public class BlockFluidInjection {
    @Inject(method = "getBlockBrightness")
    public static void getBlockBrightness(InjectionHelper helper) {
        if (Brightness.INSTANCE.isToggled()) {
            helper.setReturnValue((float) Brightness.INSTANCE.getSettings()[0].getDouble());
            helper.setCancelled(true);
        }
    }

    @Inject(method = "getCollisionBoundingBoxFromPool")
    public static void getCollisionBoundingBoxFromPool(InjectionHelper helper) throws ClassNotFoundException {
        if (!WaterWalking.INSTANCE.isToggled()) return;
        Reflector player = InjectionHelper.getMinecraft().getField("thePlayer");
        if ((boolean) player.invoke("isInWater").get()) {
            player.invoke("jump");
            return;
        }
        helper.setReturnValue(InjectionHelper.getClazz("AxisAlignedBB").invoke("getCollisionBoundingBoxFromPool",
                (int) helper.getArg(2) + (double) helper.getSelf().getField("minX").get(),
                (int) helper.getArg(3) + (double) helper.getSelf().getField("minY").get(),
                (int) helper.getArg(4) + (double) helper.getSelf().getField("minZ").get(),
                (int) helper.getArg(2) + (double) helper.getSelf().getField("maxX").get(),
                (int) helper.getArg(3) + (double) helper.getSelf().getField("maxY").get(),
                (int) helper.getArg(4) + (double) helper.getSelf().getField("maxZ").get()).get());
        helper.setCancelled(true);
    }
}
