package met.freehij.kareliq.injection;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.module.movement.Flight;
import met.freehij.kareliq.module.movement.GuiWalk;
import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.constant.At;
import met.freehij.loader.util.InjectionHelper;
import met.freehij.loader.util.Reflector;
import org.lwjgl.input.Keyboard;

@Injection("MovementInputFromOptions")
public class MovementInputFromOptionsInjection {
    @Inject(method = "checkKeyForMovementInput", at = At.HEAD)
    public static void checkKeyForMovementInput(InjectionHelper helper) {
        if ((boolean) helper.getArg(2)) ClientMain.handleKeypress((int) helper.getArg(1));
    }

    @Inject(method = "updatePlayerMoveState", at = At.HEAD)
    public static void updatePlayerMoveState1(InjectionHelper helper) {
        boolean[] movementKeyStates = (boolean[]) helper.getSelf().getField("movementKeyStates").get();
        if (GuiWalk.INSTANCE.isToggled()) {
            boolean disableWalk = false;
            try {
                Class<?> currentScreen = InjectionHelper.getMinecraft().getField("currentScreen").getActualClass();
                Class<?> guiChatClass = InjectionHelper.getClazz("GuiChat").getActualClass();
                Class<?> guiControlsClass = InjectionHelper.getClazz("GuiControls").getActualClass();
                if (currentScreen.equals(guiChatClass) ||
                        currentScreen.equals(guiControlsClass)) {
                    disableWalk = true;
                }
            } catch (Exception ignored) {
            }
            Object[] keyBindings = (Object[]) helper.getSelf().getField("gameSettings").getField("keyBindings").get();
            for (int i = 0; i < movementKeyStates.length; i++) {
                Reflector keyBinding = new Reflector(keyBindings[i].getClass(), keyBindings[i]);
                movementKeyStates[i] = !disableWalk && Keyboard.isKeyDown((int) keyBinding.getField("keyCode").get());
            }
            boolean left = movementKeyStates[1];
            boolean back = movementKeyStates[2];
            movementKeyStates[1] = back;
            movementKeyStates[2] = left;
        }
        if (Flight.INSTANCE.isToggled()) {
            Reflector player = InjectionHelper.getMinecraft().getField("thePlayer");
            player.setField("onGround", true);
            player.setField("motionY", 0.0D);
            if (movementKeyStates[4]) {
                player.setField("motionY", 0.2D);
            } else if (movementKeyStates[5]) {
                player.setField("motionY", -0.2D);
            }
        }
    }

    @Inject(method = "updatePlayerMoveState", at = At.RETURN)
    public static void updatePlayerMoveState2(InjectionHelper helper) {
        if (Flight.INSTANCE.isToggled()) {
            helper.getSelf().setField("sneak", false);
        }
    }
}
