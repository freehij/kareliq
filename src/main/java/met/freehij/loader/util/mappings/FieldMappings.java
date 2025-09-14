package met.freehij.loader.util.mappings;

import java.util.HashMap;
import java.util.Map;

public class FieldMappings {
    private static final Map<String, String> fieldRefmap = new HashMap<>();

    public static void initRefmap() {
        add(ClassMappings.MINECRAFT, "theMinecraft", "Lnet/minecraft/client/Minecraft;",
                "f_7951283", "theMinecraft", "a");
        add(ClassMappings.MINECRAFT, "fontRenderer", "L" + ClassMappings.get("FontRenderer") + ";",
                "f_0426313", "fontRenderer", "q");
        add(ClassMappings.get("MovementInputFromOptions"), "movementKeyStates", "[Z",
                "movementKeyStates", "f");
        add(ClassMappings.get("MovementInputFromOptions"), "gameSettings", "L" + ClassMappings.get("GameSettings") + ";",
                "gameSettings", "g");
        add(ClassMappings.get("GameSettings"), "keyBindings", "[L" + ClassMappings.get("KeyBinding") + ";",
                "keyBindings", "w");
        add(ClassMappings.get("KeyBinding"), "keyCode", "I",
                "keyCode", "b");
        add(ClassMappings.MINECRAFT, "currentScreen", "L" + ClassMappings.get("GuiScreen") + ";",
                "currentScreen", "r");
        add(ClassMappings.MINECRAFT, "gameSettings", "L" + ClassMappings.get("GameSettings") + ";",
                "gameSettings", "z");
        add(ClassMappings.MINECRAFT, "displayWidth", "I",
                "displayWidth", "d");
        add(ClassMappings.MINECRAFT, "displayHeight", "I",
                "displayHeight", "e");
        add(ClassMappings.get("GameSettings"), "showDebugInfo", "Z",
                "showDebugInfo", "B");
        add(ClassMappings.MINECRAFT, "ingameGUI", "L" + ClassMappings.get("GuiIngame") + ";",
                "ingameGUI", "v");
        add(ClassMappings.MINECRAFT, "renderGlobal", "L" + ClassMappings.get("RenderGlobal") + ";",
                "renderGlobal", "g");
        add(ClassMappings.get("MovementInput"), "sneak", "Z",
                "sneak", "e");
        add(ClassMappings.MINECRAFT, "thePlayer", "L" + ClassMappings.get("EntityPlayerSP") + ";",
                "thePlayer", "h");
        add(ClassMappings.get("Entity"), "onGround", "Z",
                "onGround", "aX");
        add(ClassMappings.get("Entity"), "motionY", "D",
                "motionY", "aQ");
        add(ClassMappings.get("Packet10Flying"), "onGround", "Z",
                "onGround", "g");
        add(ClassMappings.MINECRAFT, "theWorld", "L" + ClassMappings.get("World") + ";",
                "theWorld", "f");
        add(ClassMappings.get("Entity"), "boundingBox", "L" + ClassMappings.get("AxisAlignedBB") + ";",
                "boundingBox", "aW");
        add(ClassMappings.MINECRAFT, "playerController", "L" + ClassMappings.get("PlayerController") + ";",
                "playerController", "c");
        add(ClassMappings.get("EntityPlayer"), "isSwinging", "Z",
                "isSwinging", "j");
        add(ClassMappings.get("Entity"), "fallDistance", "F",
                "fallDistance", "bk");
        add(ClassMappings.get("Block"), "blockID", "I",
                "blockID", "bn");
        add(ClassMappings.get("Block"), "minX", "D",
                "minX", "bs");
        add(ClassMappings.get("Block"), "minY", "D",
                "minY", "bt");
        add(ClassMappings.get("Block"), "minZ", "D",
                "minZ", "bu");
        add(ClassMappings.get("Block"), "maxX", "D",
                "maxX", "bv");
        add(ClassMappings.get("Block"), "maxY", "D",
                "maxY", "bw");
        add(ClassMappings.get("Block"), "maxZ", "D",
                "maxZ", "bx");
        add(ClassMappings.get("PlayerControllerMP"), "curBlockDamageMP", "F",
                "curBlockDamageMP", "f");
        add(ClassMappings.get("GuiTextField"), "isEnabled", "Z",
                "isEnabled", "b");
        add(ClassMappings.get("GuiTextField"), "isFocused", "Z",
                "isFocused", "a");
        add(ClassMappings.get("GuiTextField"), "text", "Ljava/lang/String;",
                "text", "h");
        add(ClassMappings.get("GuiTextField"), "maxStringLength", "I",
                "maxStringLength", "i");
        add(ClassMappings.get("GuiScreen"), "height", "I",
                "height", "d");
        add(ClassMappings.get("GuiScreen"), "width", "I",
                "width", "c");
        add(ClassMappings.get("GuiScreen"), "controlList", "Ljava/util/List;",
                "controlList", "e");
        add(ClassMappings.get("GuiButton"), "id", "I",
                "id", "f");
        add(ClassMappings.get("GuiButton"), "height", "I",
                "height", "b");
        add(ClassMappings.get("GuiButton"), "width", "I",
                "width", "a");
        add(ClassMappings.get("GuiButton"), "yPosition", "I",
                "yPosition", "d");
        add(ClassMappings.get("GuiButton"), "xPosition", "I",
                "xPosition", "c");
        add(ClassMappings.get("Entity"), "stepHeight", "F",
                "stepHeight", "bp");
        add(ClassMappings.get("GuiButton"), "displayString", "Ljava/lang/String;",
                "displayString", "e");
        add(ClassMappings.get("GameSettings"), "keyBindSneak", "L" + ClassMappings.get("KeyBinding") + ";",
                "keyBindSneak", "v");
    }

    private static void add(String classReference, String fieldReference, String descriptor, String... entries) {
        fieldRefmap.put(classReference + ":" + fieldReference, MappingResolver.resolveField(classReference, descriptor,
                entries));
    }

    public static String get(String classReference, String fieldReference) {
        return fieldRefmap.get(classReference + ":" + fieldReference);
    }
}
