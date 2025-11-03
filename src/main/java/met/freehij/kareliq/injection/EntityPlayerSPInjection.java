package met.freehij.kareliq.injection;

import met.freehij.kareliq.module.combat.Aura;
import met.freehij.kareliq.module.movement.FastFall;
import met.freehij.kareliq.module.movement.Step;
import met.freehij.kareliq.module.player.NoFallDamage;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;
import org.lwjgl.input.Keyboard;

import java.util.List;

@Injection("EntityPlayerSP")
public class EntityPlayerSPInjection {
    @Inject(method = "onLivingUpdate")
    public static void onLivingUpdate(InjectionHelper helper) throws ClassNotFoundException {
        if (NoFallDamage.INSTANCE.isToggled()) helper.getSelf().setField("fallDistance", 0.F);
        if (Step.INSTANCE.isToggled()) helper.getSelf().setField("stepHeight", (float) Step.INSTANCE.getSettingByName("height").getDouble());
        if (helper.getSelf().getField("motionY").getDouble() < 0 && FastFall.INSTANCE.isToggled()) {
            if (FastFall.INSTANCE.getSettings()[0].getBoolean() && Keyboard.isKeyDown(InjectionHelper.getMinecraft().getField("gameSettings").getField("keyBindJump").getField("keyCode").getInt())) {

            } else {
                helper.getSelf().setField("motionY", -FastFall.INSTANCE.getSettings()[1].getDouble());
            }
        }
        if (!Aura.INSTANCE.isToggled()) return;
        List test = (List) InjectionHelper.getMinecraft().getField("theWorld").invoke("getEntitiesWithinAABBExcludingEntity",
                helper.getSelf().get(), helper.getSelf().getField("boundingBox").invoke("expand", 3.D, 3.D, 3.D).get()).get();
        Class<?> entityLiving = InjectionHelper.getClazz("EntityLiving").getActualClass();
        Class<?> animal = InjectionHelper.getClazz("EntityAnimal").getActualClass();
        Class<?> mob = InjectionHelper.getClazz("EntityMob").getActualClass();
        Class<?> player = InjectionHelper.getClazz("EntityPlayer").getActualClass();
        for (Object o : test) {
            if (entityLiving.isInstance(o)) {
                boolean swing = false;
                if (Aura.INSTANCE.getSettings()[1].getBoolean() && animal.isInstance(o)) {
                    InjectionHelper.getMinecraft().getField("playerController").invoke("attackEntity",
                            helper.getSelf().get(), o);
                    swing = true;
                }
                if (Aura.INSTANCE.getSettings()[0].getBoolean() && mob.isInstance(o)) {
                    InjectionHelper.getMinecraft().getField("playerController").invoke("attackEntity",
                            helper.getSelf().get(), o);
                    swing = true;
                }
                if (Aura.INSTANCE.getSettings()[2].getBoolean() && player.isInstance(o)) {
                    InjectionHelper.getMinecraft().getField("playerController").invoke("attackEntity",
                            helper.getSelf().get(), o);
                    swing = true;
                }
                if (swing && Aura.INSTANCE.getSettings()[3].getBoolean() && !(boolean) helper.getSelf().getField("isSwinging").get()) helper.getSelf().invoke("swingItem");
            }
        }
    }
}
