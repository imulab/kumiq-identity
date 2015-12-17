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

    public static ResourceVersionMismatchException userResourceVersionMismatch(String userResourceId, String accessETag, String actualETag) {
        return new ResourceVersionMismatchException(ScimConstants.RESOURCE_TYPE_USER, userResourceId, accessETag, actualETag);
    }

    public static ResourceVersionMismatchException groupResourceVersionMismatch(String userResourceId, String accessETag, String actualETag) {
        return new ResourceVersionMismatchException(ScimConstants.RESOURCE_TYPE_GROUP, userResourceId, accessETag, actualETag);
    }

    public static ResourceTooManyException tooManyUsers(int capacity, int count) {
        return new ResourceTooManyException(ScimConstants.RESOURCE_TYPE_USER, null, capacity, count);
    }

    public static ResourceTooManyException tooManyGroups(int capacity, int count) {
        return new ResourceTooManyException(ScimConstants.RESOURCE_TYPE_GROUP, null, capacity, count);
    }

    public static ResourceUniquenessViolatedException userPathNotUnique(String path, String resourceId) {
        return new ResourceUniquenessViolatedException(ScimConstants.RESOURCE_TYPE_USER, resourceId, path);
    }

    public static ResourceUniquenessViolatedException groupPathNotUnique(String path, String resourceId) {
        return new ResourceUniquenessViolatedException(ScimConstants.RESOURCE_TYPE_GROUP, resourceId, path);
    }

    public static ResourceAttributeAbsentException userAttributeAbsent(String path, String resourceId) {
        return new ResourceAttributeAbsentException(ScimConstants.RESOURCE_TYPE_USER, resourceId, path);
    }

    public static ResourceAttributeAbsentException groupAttributeAbsent(String path, String resourceId) {
        return new ResourceAttributeAbsentException(ScimConstants.RESOURCE_TYPE_GROUP, resourceId, path);
    }

    public static ResourceReferenceViolatedException userReferenceViolated(String path, String resourceId) {
        return new ResourceReferenceViolatedException(ScimConstants.RESOURCE_TYPE_USER, resourceId, path);
    }

    public static ResourceReferenceViolatedException groupReferenceViolated(String path, String resourceId) {
        return new ResourceReferenceViolatedException(ScimConstants.RESOURCE_TYPE_GROUP, resourceId, path);
    }

    public static ResourceImmutabilityViolatedException userImmutabilityViolated(String path, String resourceId) {
        return new ResourceImmutabilityViolatedException(ScimConstants.RESOURCE_TYPE_USER, resourceId, path);
    }

    public static ResourceImmutabilityViolatedException groupImmutabilityViolated(String path, String resourceId) {
        return new ResourceImmutabilityViolatedException(ScimConstants.RESOURCE_TYPE_GROUP, resourceId, path);
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

    public static class ResourceVersionMismatchException extends ResourceAccessException {

        private final String accessETag;
        private final String actualETag;

        public ResourceVersionMismatchException(String resourceType, String resourceId, String accessETag, String actualETag) {
            super(resourceType, resourceId);
            this.accessETag = accessETag;
            this.actualETag = actualETag;
        }

        public String getAccessETag() {
            return accessETag;
        }

        public String getActualETag() {
            return actualETag;
        }
    }

    public static class ResourceTooManyException extends ResourceAccessException {

        private final int capacity;
        private final int actualCount;

        public ResourceTooManyException(String resourceType, String resourceId, int capacity, int actualCount) {
            super(resourceType, resourceId);
            this.capacity = capacity;
            this.actualCount = actualCount;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getActualCount() {
            return actualCount;
        }
    }

    public static class ResourceUniquenessViolatedException extends ResourceAccessException {

        private final String path;

        public ResourceUniquenessViolatedException(String resourceType, String resourceId, String path) {
            super(resourceType, resourceId);
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static class ResourceAttributeAbsentException extends ResourceAccessException {

        private final String requiredPath;

        public ResourceAttributeAbsentException(String resourceType, String resourceId, String requiredPath) {
            super(resourceType, resourceId);
            this.requiredPath = requiredPath;
        }

        public String getRequiredPath() {
            return requiredPath;
        }
    }

    public static class ResourceReferenceViolatedException extends ResourceAccessException {

        private final String path;

        public ResourceReferenceViolatedException(String resourceType, String resourceId, String path) {
            super(resourceType, resourceId);
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static class ResourceImmutabilityViolatedException extends ResourceAccessException {

        private String violatedPath;

        public ResourceImmutabilityViolatedException(String resourceType, String resourceId, String violatedPath) {
            super(resourceType, resourceId);
            this.violatedPath = violatedPath;
        }

        public String getViolatedPath() {
            return violatedPath;
        }

        public void setViolatedPath(String violatedPath) {
            this.violatedPath = violatedPath;
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
