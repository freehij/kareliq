package met.freehij.kareliq.utils.mappings;

public class MappingUtils {
    public enum MappingType {
        MOJANG,
        MCP,
        ORNITHEMC
    }

    public static class ClassEntry {
        private final String className;
        private final MappingType mappingType;

        public ClassEntry(final String className, final MappingType mappingType) {
            this.className = className;
            this.mappingType = mappingType;
        }

        public String getClassName() {
            return className;
        }

        public MappingType getMappingType() {
            return mappingType;
        }
    }

    public static class MethodEntry {
        private final String methodName;
        private final MappingType mappingType;

        public MethodEntry(final String methodName, final MappingType mappingType) {
            this.methodName = methodName;
            this.mappingType = mappingType;
        }

        public String getMethodName() {
            return methodName;
        }

        public MappingType getMappingType() {
            return mappingType;
        }
    }
}
