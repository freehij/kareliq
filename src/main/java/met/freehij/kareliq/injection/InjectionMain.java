package met.freehij.kareliq.injection;

import met.freehij.kareliq.ClientMain;
import met.freehij.kareliq.injection.injections.*;

import java.lang.instrument.Instrumentation;

public final class InjectionMain {
    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
        instrumentation.addTransformer(new GuiIngameInjection());
        instrumentation.addTransformer(new BlockInjection());
        instrumentation.addTransformer(new BlockFluidInjection());
        instrumentation.addTransformer(new WorldInjection());
        instrumentation.addTransformer(new MovementInputFromOptionsInjection());
        instrumentation.addTransformer(new EntityPlayerSPInjection());
        ClientMain.startClient();
    }
}
