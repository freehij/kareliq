package met.freehij.loader.util.mappings.util;

public class MethodMapping {
    public final String method;
    public Class<?>[] params;
    public final String descriptor;

    public MethodMapping(String method, Class<?>[] params, String descriptor) {
        this.method = method;
        this.params = params;
        this.descriptor = descriptor;
    }
}