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

    public static class MethodFieldEntry {
        private final String name;
        private final MappingType mappingType;

        public MethodFieldEntry(final String name, final MappingType mappingType) {
            this.name = name;
            this.mappingType = mappingType;
        }

        public String getName() {
            return name;
        }

        public MappingType getMappingType() {
            return mappingType;
        }
    }
}
