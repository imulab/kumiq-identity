package com.kumiq.identity.scim.task

import com.kumiq.identity.scim.path.ModificationUnit
import com.kumiq.identity.scim.resource.core.Resource
import com.kumiq.identity.scim.resource.group.Group
import com.kumiq.identity.scim.resource.misc.Schema
import com.kumiq.identity.scim.resource.user.User

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Context Base
 *
 * @param < T >
 */
abstract class ResourceOpContext<T extends Resource> {

    /**
     * Http servlet request
     */
    HttpServletRequest httpRequest

    /**
     * Http servlet response
     */
    HttpServletResponse httpResponse

    /**
     * Schema associated with this resource
     */
    Schema schema

    /**
     * The resource itself, could be used as output or input, based on the context type
     */
    T resource

    /**
     * Misc info for task communication
     */
    Map<String, Object> userInfo = [:]
}

/**
 * Generic Get context
 *
 * @param < T >
 */
abstract class GetContext<T extends Resource> extends ResourceOpContext<T> {

    String id

    Map<String, Object> data
}

/**
 * Generic Query Context
 *
 * @param < T >
 */
abstract class QueryContext<T extends Resource> extends ResourceOpContext<T> {

    String filter

    String sort

    boolean ascending

    List<T> resources
}

/**
 * Generic Create Context
 *
 * @param < T >
 */
abstract class CreateContext<T extends Resource> extends ResourceOpContext<T> {

}

/**
 * Generic Replace Context
 *
 * @param < T >
 */
abstract class ReplaceContext<T extends Resource> extends ResourceOpContext<T> {

    String id

    T originalCopy
}

/**
 * Generic Patch Context
 *
 * @param < T >
 */
abstract class PatchContext<T extends Resource> extends ReplaceContext<T> {

    List<ModificationUnit> modifications;
}

/**
 * Generic Delete Context
 *
 * @param < T >
 */
abstract class DeleteContext<T extends Resource> extends ResourceOpContext<T> {

    String id
}

// ~ User ==============================================================================================================

final class UserGetContext<T extends User> extends GetContext<T> {

}

final class UserQueryContext<T extends User> extends QueryContext<T> {

}

final class UserCreateContext<T extends User> extends CreateContext<T> {

}

final class UserReplaceContext<T extends User> extends ReplaceContext<T> {

}

final class UserPatchContext<T extends User> extends PatchContext<T> {

}

final class UserDeleteContext<T extends User> extends DeleteContext<T> {

}

// ~ Group =============================================================================================================

final class GroupGetContext<T extends Group> extends GetContext<T> {

}

final class GroupQueryContext<T extends Group> extends QueryContext<T> {

}

final class GroupCreateContext<T extends Group> extends CreateContext<T> {

}

final class GroupReplaceContext<T extends Group> extends ReplaceContext<T> {

}

final class GroupPatchContext<T extends Group> extends PatchContext<T> {

}

final class GroupDeleteContext<T extends Group> extends DeleteContext<T> {

}