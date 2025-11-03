package met.freehij.loader.util.mappings;

import java.util.HashMap;
import java.util.Map;

public class ClassMappings {
    private static final Map<String, String> classRefmap = new HashMap<>();
    public static final String MINECRAFT = "net/minecraft/client/Minecraft";

    public static void initRefmap() {
        add("MovementInputFromOptions",
                "net/minecraft/unmapped/C_5637855", "net/minecraft/src/MovementInputFromOptions", "ln");
        add("GuiIngame",
                "net/minecraft/unmapped/C_8651652", "net/minecraft/src/GuiIngame", "uk");
        add("FontRenderer",
                "net/minecraft/unmapped/C_3831727", "net/minecraft/src/FontRenderer", "se");
        add("EntityPlayer",
                "net/minecraft/unmapped/C_9590849", "net/minecraft/src/EntityPlayer", "gq");
        add("GameSettings",
                "net/minecraft/src/GameSettings", "kr");
        add("KeyBinding",
                "net/minecraft/src/KeyBinding", "px");
        add("GuiChat",
                "net/minecraft/src/GuiChat", "ga");
        add("GuiControls",
                "net/minecraft/src/GuiControls", "uj");
        add("GuiScreen",
                "net/minecraft/src/GuiScreen", "cy");
        add("ScaledResolution",
                "net/minecraft/src/ScaledResolution", "qm");
        add("Gui",
                "net/minecraft/src/Gui", "tw");
        add("IBlockAccess",
                "net/minecraft/src/IBlockAccess", "xg");
        add("Block",
                "net/minecraft/src/Block", "un");
        add("RenderGlobal",
                "net/minecraft/src/RenderGlobal", "m");
        add("BlockFluid",
                "net/minecraft/src/BlockFluid", "rk");
        add("World",
                "net/minecraft/src/World", "fb");
        add("MovementInput",
                "net/minecraft/src/MovementInput", "ui");
        add("EntityPlayerSP",
                "net/minecraft/src/EntityPlayerSP", "da");
        add("Entity",
                "net/minecraft/src/Entity", "si");
        add("EntityLiving",
                "net/minecraft/src/EntityLiving", "lo");
        add("Packet10Flying",
                "net/minecraft/src/Packet10Flying", "id");
        add("Packet",
                "net/minecraft/src/Packet", "ke");
        add("AxisAlignedBB",
                "net/minecraft/src/AxisAlignedBB", "eo");
        add("PlayerController",
                "net/minecraft/src/PlayerController", "nx");
        add("EntityAnimal",
                "net/minecraft/src/EntityAnimal", "bf");
        add("EntityMob",
                "net/minecraft/src/EntityMob", "gx");
        add("NetClientHandler",
                "net/minecraft/src/NetClientHandler", "mx");
        add("Packet28EntityVelocity",
                "net/minecraft/src/Packet28EntityVelocity", "gh");
        add("PlayerControllerMP",
                "net/minecraft/src/PlayerControllerMP", "xb");
        add("GuiTextField",
                "net/minecraft/src/GuiTextField", "rj");
        add("GuiMainMenu",
                "net/minecraft/src/GuiMainMenu", "fs");
        add("GuiButton",
                "net/minecraft/src/GuiButton", "ka");
        add("RenderGlobal",
                "net/minecraft/src/RenderGlobal", "m");
        add("Vec3D",
                "net/minecraft/src/Vec3D", "br");
        add("ICamera",
                "net/minecraft/src/ICamera", "ye");
        add("Session",
                "net/minecraft/src/Session", "gp");
        add("EntityClientPlayerMP",
                "net/minecraft/src/EntityClientPlayerMP", "tf");
    }

    private static void add(String reference, String... entries) {
        classRefmap.put(reference, MappingResolver.resolveClass(entries));
    }

    public static String get(String reference) {
        String className = classRefmap.get(reference);
        return className == null ? reference : className;
    }
}
