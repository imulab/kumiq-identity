package com.kumiq.identity.scim.utils;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ExceptionFactory {

    public static PathCompiledToVoidException pathCompiledToVoid(String compilePath, String voidPath) {
        return new PathCompiledToVoidException(compilePath, voidPath);
    }

    public static PathCompilationException pathCompiledMissingAttribute(String compilePath, String rougePath) {
        return new PathCompilationMissingAttributeException(compilePath, rougePath);
    }

    public static PathCompilationNonExpandableException pathCompiledNotExpandable(String compiledPath, String notExpandablePath) {
        return new PathCompilationNonExpandableException(compiledPath, notExpandablePath);
    }

    protected static class PathCompilationException extends RuntimeException {

        private final String compilePath;

        public PathCompilationException(String compilePath) {
            this.compilePath = compilePath;
        }

        public String getCompilePath() {
            return compilePath;
        }
    }

    public static class PathCompiledToVoidException extends PathCompilationException {

        private final String voidPath;

        public PathCompiledToVoidException(String compilePath, String voidPath) {
            super(compilePath);
            this.voidPath = voidPath;
        }

        public String getVoidPath() {
            return voidPath;
        }
    }

    public static class PathCompilationMissingAttributeException extends PathCompilationException {

        private final String rougePath;

        public PathCompilationMissingAttributeException(String compilePath, String rougePath) {
            super(compilePath);
            this.rougePath = rougePath;
        }

        public String getRougePath() {
            return rougePath;
        }
    }

    public static class PathCompilationNonExpandableException extends PathCompilationException {
        private final String notExpandablePath;

        public PathCompilationNonExpandableException(String compilePath, String notExpandablePath) {
            super(compilePath);
            this.notExpandablePath = notExpandablePath;
        }

        public String getNotExpandablePath() {
            return notExpandablePath;
        }
    }
}
