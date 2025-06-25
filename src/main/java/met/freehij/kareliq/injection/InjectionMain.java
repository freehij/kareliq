package met.freehij.kareliq.injection;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.injection.injections.*;
import met.freehij.kareliq.utils.ReflectionHelper;
import met.freehij.kareliq.utils.mappings.ClassMappings;
import met.freehij.kareliq.utils.mappings.MappingResolver;
import met.freehij.kareliq.utils.mappings.MethodMappings;

import java.lang.instrument.Instrumentation;

public final class InjectionMain {
    public static String blockClass;
    public static String playerClass;
    public static String movementInputClass;
    public static String worldClass;
    public static String boundingBoxClass;
    public static String getCollisionBBMethod;

    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
        ReflectionHelper.initHelper();
    	
    	String ingameClass = MappingResolver.resolveClass(ClassMappings.GUI_INGAME);
    	String renderGameOverlay = MappingResolver.resolveMethod(ingameClass, "(FZII)V",
                MethodMappings.RENDER_GAME_OVERLAY);
        instrumentation.addTransformer(new GuiIngameInjection(ingameClass, renderGameOverlay, "(FZII)V"));

        blockClass = MappingResolver.resolveClass(ClassMappings.BLOCK);
        String blockFluidClass = MappingResolver.resolveClass(ClassMappings.BLOCK_FLUID);
        String blockAccessInterface = MappingResolver.resolveClass(ClassMappings.BLOCK_ACCESS);
        String blockDescriptor = ("(L" + blockAccessInterface + ";III)F").replace(".", "/");
        String blockMethod = MappingResolver.resolveMethod(blockClass, blockDescriptor,
                MethodMappings.GET_BLOCK_BRIGHTNESS);
        instrumentation.addTransformer(new BlockInjection(blockClass, blockMethod, blockDescriptor));
        instrumentation.addTransformer(new BlockFluidInjection1(blockFluidClass, blockMethod, blockDescriptor));

        worldClass = MappingResolver.resolveClass(ClassMappings.WORLD);
        String getBrightness = MappingResolver.resolveMethod(worldClass, "(III)F", MethodMappings.GET_BRIGHTNESS);
        instrumentation.addTransformer(new WorldInjection(worldClass, getBrightness, "(III)F"));

        boundingBoxClass = MappingResolver.resolveClass(ClassMappings.BOUNDING_BOX);
        String bbDescriptor = "(L" + worldClass + ";III)L" + boundingBoxClass + ";";
        getCollisionBBMethod = MappingResolver.resolveMethod(blockFluidClass, bbDescriptor,
                MethodMappings.GET_COLLISION_BB);
        instrumentation.addTransformer(new BlockFluidInjection2(blockFluidClass, getCollisionBBMethod, bbDescriptor));

        movementInputClass = MappingResolver.resolveClass(ClassMappings.MOVEMENT_INPUT_FROM_OPTIONS);
        String movementInputMethod = MappingResolver.resolveMethod(movementInputClass, "(IZ)V",
                MethodMappings.CHECK_KEY_FOR_MOVEMENT_INPUT);
        instrumentation.addTransformer(new MovementInputFromOptionsInjection1(movementInputClass, movementInputMethod,
                "(IZ)V"));

        playerClass = MappingResolver.resolveClass(ClassMappings.ENTITY_PLAYER_SP);
        String updatePlayer = MappingResolver.resolveMethod(playerClass, "()V",
                MethodMappings.UPDATE_PLAYER_ACTION_STATE);
        instrumentation.addTransformer(new EntityPlayerSPInjection(playerClass, updatePlayer, "()V"));

        String playerControllerSP = MappingResolver.resolveClass(ClassMappings.PLAYER_CONTROLLER_SP);
        String playerControllerMP = MappingResolver.resolveClass(ClassMappings.PLAYER_CONTROLLER_MP);
        String clickBlock = MappingResolver.resolveMethod(playerControllerSP, "(IIII)V", MethodMappings.CLICK_BLOCK);
        instrumentation.addTransformer(new AnyPlayerControllerInjection(playerControllerSP, clickBlock));
        instrumentation.addTransformer(new AnyPlayerControllerInjection(playerControllerMP, clickBlock));

        instrumentation.addTransformer(new MinecraftInjection());

        ClientMain.startClient();
    }
}