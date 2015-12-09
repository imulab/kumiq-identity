package com.kumiq.identity.scim.utils;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ExceptionFactory {

    public static PathCompiledToVoidException pathCompiledToVoid(String compilePath, String voidPath) {
        return new PathCompiledToVoidException(compilePath, voidPath);
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


}
