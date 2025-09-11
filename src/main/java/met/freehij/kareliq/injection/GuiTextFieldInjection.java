package met.freehij.kareliq.injection;

import met.freehij.loader.annotation.Inject;
import met.freehij.loader.annotation.Injection;
import met.freehij.loader.util.InjectionHelper;

@Injection("GuiTextField")
public class GuiTextFieldInjection {
    @Inject(method = "textboxKeyTyped")
    public static void textboxKeyTyped(InjectionHelper helper) throws ClassNotFoundException {
        if (helper.getSelf().getField("isFocused").getBoolean()
                && helper.getSelf().getField("isEnabled").getBoolean()) {
            if ((char) helper.getArg(1) == 22) {
                String var3 = InjectionHelper.getClazz("GuiScreen").invoke("getClipboardString").getString();
                if (var3 == null) {
                    var3 = "";
                }

                String newText = helper.getSelf().getField("text").getString() + var3;
                int maxStringLength = helper.getSelf().getField("maxStringLength").getInt();
                if (newText.length() > maxStringLength) {
                    newText = newText.substring(0, maxStringLength);
                }

                helper.getSelf().setField("text", newText);
                helper.setCancelled(true);
            }
        }
    }
}
