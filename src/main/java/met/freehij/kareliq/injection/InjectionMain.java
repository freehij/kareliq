package met.freehij.kareliq.injection;

import met.freehij.kareliq.injection.injections.GuiIngameInjection;

import java.lang.instrument.Instrumentation;

public final class InjectionMain {
    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
        instrumentation.addTransformer(new GuiIngameInjection());
    }
}
