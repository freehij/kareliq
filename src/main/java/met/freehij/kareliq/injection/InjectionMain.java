package met.freehij.kareliq.injection;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.injection.injections.*;
import met.freehij.kareliq.utils.mappings.ClassMappings;
import met.freehij.kareliq.utils.mappings.MappingResolver;
import met.freehij.kareliq.utils.mappings.MethodMappings;

import java.lang.instrument.Instrumentation;

public final class InjectionMain {
    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
        //instrumentation.addTransformer(new GuiIngameInjection());

        String blockClass = MappingResolver.resolveClass(ClassMappings.BLOCK);
        String blockFluidClass = MappingResolver.resolveClass(ClassMappings.BLOCK_FLUID);
        String blockAccessInterface = MappingResolver.resolveClass(ClassMappings.BLOCK_ACCESS);
        String blockDescriptor = ("(L" + blockAccessInterface + ";III)F").replace(".", "/");
        String blockMethod = MappingResolver.resolveMethod(blockClass, blockDescriptor,
                MethodMappings.GET_BLOCK_BRIGHTNESS);
        instrumentation.addTransformer(new BlockInjection(blockClass, blockMethod, blockDescriptor));
        instrumentation.addTransformer(new BlockFluidInjection(blockFluidClass, blockMethod, blockDescriptor));

        //instrumentation.addTransformer(new WorldInjection());

        String movementInputClass = MappingResolver.resolveClass(ClassMappings.MOVEMENT_INPUT_FROM_OPTIONS);
        String movementInputMethod = MappingResolver.resolveMethod(movementInputClass, "(IZ)V", MethodMappings.RENDER_GAME_OVERLAY);
        instrumentation.addTransformer(new MovementInputFromOptionsInjection(movementInputClass, movementInputMethod, "(IZ)V"));

        //instrumentation.addTransformer(new EntityPlayerSPInjection());
        ClientMain.startClient();
    }
}