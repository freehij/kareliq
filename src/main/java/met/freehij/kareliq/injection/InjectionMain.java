package met.freehij.kareliq.injection;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.injection.injections.*;
import met.freehij.kareliq.utils.mappings.ClassMappings;
import met.freehij.kareliq.utils.mappings.MappingResolver;
import met.freehij.kareliq.utils.mappings.MethodMappings;

import java.lang.instrument.Instrumentation;

public final class InjectionMain {
    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
    	String ingameClass = MappingResolver.resolveClass(ClassMappings.GUI_INGAME);
    	String renderGameOverlay = MappingResolver.resolveMethod(ingameClass, "(FZII)V",
                MethodMappings.RENDER_GAME_OVERLAY);
        instrumentation.addTransformer(new GuiIngameInjection(ingameClass, renderGameOverlay, "(FZII)V"));

        String blockClass = MappingResolver.resolveClass(ClassMappings.BLOCK);
        String blockFluidClass = MappingResolver.resolveClass(ClassMappings.BLOCK_FLUID);
        String blockAccessInterface = MappingResolver.resolveClass(ClassMappings.BLOCK_ACCESS);
        String blockDescriptor = ("(L" + blockAccessInterface + ";III)F").replace(".", "/");
        String blockMethod = MappingResolver.resolveMethod(blockClass, blockDescriptor,
                MethodMappings.GET_BLOCK_BRIGHTNESS);
        instrumentation.addTransformer(new BlockInjection(blockClass, blockMethod, blockDescriptor));
        instrumentation.addTransformer(new BlockFluidInjection(blockFluidClass, blockMethod, blockDescriptor));

        String worldClass = MappingResolver.resolveClass(ClassMappings.WORLD);
        String getBrightness = MappingResolver.resolveMethod(worldClass, "(III)F", MethodMappings.GET_BRIGHTNESS);
        instrumentation.addTransformer(new WorldInjection(worldClass, getBrightness, "(III)F"));

        String movementInputClass = MappingResolver.resolveClass(ClassMappings.MOVEMENT_INPUT_FROM_OPTIONS);
        String movementInputMethod = MappingResolver.resolveMethod(movementInputClass, "(IZ)V",
                MethodMappings.CHECK_KEY_FOR_MOVEMENT_INPUT);
        instrumentation.addTransformer(new MovementInputFromOptionsInjection(movementInputClass, movementInputMethod, "(IZ)V"));

        String playerClass = MappingResolver.resolveClass(ClassMappings.ENTITY_PLAYER_SP);
        String updatePlayer = MappingResolver.resolveMethod(playerClass, "()V", MethodMappings.UPDATE_PLAYER_ACTION_STATE);
        instrumentation.addTransformer(new EntityPlayerSPInjection(playerClass, updatePlayer, "()V"));
        ClientMain.startClient();
    }
}