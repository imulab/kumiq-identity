package com.kumiq.identity.scim.utils;

import com.kumiq.identity.scim.resource.constant.ScimConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ExceptionFactory {

    // ~ Compilation ===================================================================================================

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

    // ~ Data access ===================================================================================================

    public static ResourceNotFoundException resourceNotFound(String resourceType, String resourceId) {
        return new ResourceNotFoundException(resourceType, resourceId);
    }

    public static ResourceNotFoundException userResourceNotFound(String userResourceId) {
        return new ResourceNotFoundException(ScimConstants.RESOURCE_TYPE_USER, userResourceId);
    }

    public static ResourceNotFoundException groupResourceNotFound(String groupResourceId) {
        return new ResourceNotFoundException(ScimConstants.RESOURCE_TYPE_GROUP, groupResourceId);
    }

    public static ResourceAlreadyExistsException userResourceAlreadyExists(Map<String, Object> conflict) {
        return new ResourceAlreadyExistsException(ScimConstants.RESOURCE_TYPE_USER, conflict);
    }

    public static ResourceAlreadyExistsException groupResourceAlreadyExists(Map<String, Object> conflict) {
        return new ResourceAlreadyExistsException(ScimConstants.RESOURCE_TYPE_GROUP, conflict);
    }

    public static ResourceAlreadyExistsException resourceAlreadyExists(String resourceType, String conflictId) {
        Map<String, Object> conflict = new HashMap<>();
        conflict.put("id", conflictId);
        return new ResourceAlreadyExistsException(resourceType, conflict);
    }

    protected static class ResourceAccessException extends RuntimeException {

        private final String resourceType;
        private String resourceId;

        public ResourceAccessException(String resourceType, String resourceId) {
            this.resourceType = resourceType;
            this.resourceId = resourceId;
        }

        public String getResourceType() {
            return resourceType;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }
    }

    public static class ResourceNotFoundException extends ResourceAccessException {

        public ResourceNotFoundException(String resourceType, String resourceId) {
            super(resourceType, resourceId);
        }
    }

    public static class ResourceAlreadyExistsException extends ResourceAccessException {

        private final Map<String, Object> conflict;

        public ResourceAlreadyExistsException(String resourceType, Map<String, Object> conflict) {
            super(resourceType, null);
            this.conflict = conflict;
        }

        public Map<String, Object> getConflict() {
            return conflict;
        }
    }

    // ~ generic =======================================================================================================

    public static ScimException fail(String message) {
        return new ScimException(message);
    }

    public static class ScimException extends RuntimeException {
        public ScimException(String message) {
            super(message);
        }
    }
}
