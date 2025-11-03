package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.render.Brightness;
import met.freehij.kareliq.module.world.NoClip;
import met.freehij.kareliq.module.world.WaterWalking;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;
import met.freehij.loader.util.mappings.ClassMappings;
import met.freehij.loader.util.mappings.FieldMappings;
import met.freehij.loader.util.mappings.MethodMappings;
import met.freehij.loader.util.mappings.util.MethodMapping;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

@Injection("BlockFluid")
public class BlockFluidInjection {
    private static Reflector axisAlignedBBReflector;
    private static final String thePlayer = FieldMappings.get(ClassMappings.MINECRAFT, "thePlayer");
    private static final String gameSettings = FieldMappings.get(ClassMappings.MINECRAFT, "gameSettings");
    private static final String keyBindSneak = FieldMappings.get(ClassMappings.get("GameSettings"), "keyBindSneak");
    private static final String keyCode = FieldMappings.get(ClassMappings.get("KeyBinding"), "keyCode");
    private static final String fallDistance = FieldMappings.get(ClassMappings.get("Entity"), "fallDistance");
    private static final String isInWater = MethodMappings.get(ClassMappings.get("Entity"), "isInWater").method;
    private static final String jump = MethodMappings.get(ClassMappings.get("EntityPlayer"), "jump").method;
    private static final String minX = FieldMappings.get(ClassMappings.get("Block"), "minX");
    private static final String minY = FieldMappings.get(ClassMappings.get("Block"), "minY");
    private static final String minZ = FieldMappings.get(ClassMappings.get("Block"), "minZ");
    private static final String maxX = FieldMappings.get(ClassMappings.get("Block"), "maxX");
    private static final String maxY = FieldMappings.get(ClassMappings.get("Block"), "maxY");
    private static final String maxZ = FieldMappings.get(ClassMappings.get("Block"), "maxZ");

    @Inject(method = "getBlockBrightness")
    public static void getBlockBrightness(InjectionHelper helper) {
        if (Brightness.INSTANCE.isToggled()) {
            helper.setReturnValue((float) Brightness.INSTANCE.getSettings()[0].getDouble());
            helper.setCancelled(true);
        }
    }

    @Inject(method = "getCollisionBoundingBoxFromPool")
    public static void getCollisionBoundingBoxFromPool(InjectionHelper helper) throws ClassNotFoundException {
        if (NoClip.INSTANCE.isToggled()) return;
        if (!WaterWalking.INSTANCE.isToggled()) return;
        Reflector player = InjectionHelper.getMinecraft().getFieldRaw(thePlayer);
        if (Keyboard.isKeyDown(InjectionHelper.getMinecraft()
                .getFieldRaw(gameSettings)
                .getFieldRaw(keyBindSneak)
                .getFieldRaw(keyCode).getInt())) {
            return;
        }
        if (player.getFieldRaw(fallDistance).getFloat() > 2.F) return;
        if ((boolean) player.invokeRaw(isInWater, new Class[0]).get()) {
            player.invokeRaw(jump, new Class[0]);
            return;
        }
        if (axisAlignedBBReflector == null) axisAlignedBBReflector = InjectionHelper.getClazz("AxisAlignedBB");
        helper.setReturnValue(axisAlignedBBReflector.invoke("getBoundingBoxFromPool",
                (int) helper.getArg(2) + (double) helper.getSelf().getFieldRaw(minX).get(),
                (int) helper.getArg(3) + (double) helper.getSelf().getFieldRaw(minY).get(),
                (int) helper.getArg(4) + (double) helper.getSelf().getFieldRaw(minZ).get(),
                (int) helper.getArg(2) + (double) helper.getSelf().getFieldRaw(maxX).get(),
                (int) helper.getArg(3) + (double) helper.getSelf().getFieldRaw(maxY).get(),
                (int) helper.getArg(4) + (double) helper.getSelf().getFieldRaw(maxZ).get()).get());
        helper.setCancelled(true);
    }
}
