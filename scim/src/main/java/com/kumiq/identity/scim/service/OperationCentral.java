package com.kumiq.identity.scim.service;

import com.kumiq.identity.scim.path.ModificationUnit;
import com.kumiq.identity.scim.resource.group.ScimGroup;
import com.kumiq.identity.scim.resource.user.ScimUser;
import com.kumiq.identity.scim.task.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@Service
@SuppressWarnings("unchecked")
public class OperationCentral {

    @Resource(name = "getUserTaskChain") Task retrieveUserTasks;
    @Resource(name = "userQueryTaskChain") Task queryUserTasks;
    @Resource(name = "createUserTaskChain") Task createUserTasks;
    @Resource(name = "userReplaceTaskChain") Task replaceUserTasks;
    @Resource(name = "userPatchTaskChain") Task patchUserTasks;
    @Resource(name = "deleteUserTaskChain") Task deleteUserTasks;

    @Resource(name = "getGroupTaskChain") Task retrieveGroupTasks;
    @Resource(name = "groupQueryTaskChain") Task queryGroupTasks;
    @Resource(name = "createGroupTaskChain") Task createGroupTasks;
    @Resource(name = "groupReplaceTaskChain") Task replaceGroupTasks;
    @Resource(name = "groupPatchTaskChain") Task patchGroupTasks;
    @Resource(name = "deleteGroupTaskChain") Task deleteGroupTasks;

    public UserGetContext retrieveUserById(String userId,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        UserGetContext<ScimUser> context = new UserGetContext<>();
        context.setId(userId);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        retrieveUserTasks.perform(context);
        return context;
    }

    public GroupGetContext retrieveGroupById(String groupId,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {
        GroupGetContext<ScimGroup> context = new GroupGetContext<>();
        context.setId(groupId);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        retrieveGroupTasks.perform(context);
        return context;
    }

    public UserQueryContext queryUser(List<String> attributes,
                                      int startIndex,
                                      int count,
                                      String filter,
                                      String sortBy,
                                      boolean ascending,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        UserQueryContext<ScimUser> context = new UserQueryContext<>();
        context.setAttributes(attributes);
        context.setStartIndex(startIndex);
        context.setCount(count);
        context.setFilter(filter);
        context.setSort(sortBy);
        context.setAscending(ascending);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        queryUserTasks.perform(context);
        return context;
    }

    public GroupQueryContext queryGroup(List<String> attributes,
                                        int startIndex,
                                        int count,
                                        String filter,
                                        String sortBy,
                                        boolean ascending,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        GroupQueryContext<ScimGroup> context = new GroupQueryContext<>();
        context.setAttributes(attributes);
        context.setStartIndex(startIndex);
        context.setCount(count);
        context.setFilter(filter);
        context.setSort(sortBy);
        context.setAscending(ascending);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        queryGroupTasks.perform(context);
        return context;
    }

    public UserCreateContext createUser(ScimUser resource,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        UserCreateContext<ScimUser> context = new UserCreateContext<>();
        context.setResource(resource);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        createUserTasks.perform(context);
        return context;
    }

    public GroupCreateContext createGroup(ScimGroup resource,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        GroupCreateContext<ScimGroup> context = new GroupCreateContext<>();
        context.setResource(resource);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        createGroupTasks.perform(context);
        return context;
    }

    public UserReplaceContext replaceUser(String userId,
                                          ScimUser resource,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        UserReplaceContext<ScimUser> context = new UserReplaceContext<>();
        context.setId(userId);
        context.setResource(resource);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        replaceUserTasks.perform(context);
        return context;
    }

    public GroupReplaceContext replaceGroup(String groupId,
                                            ScimGroup resource,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        GroupReplaceContext<ScimGroup> context = new GroupReplaceContext<>();
        context.setId(groupId);
        context.setResource(resource);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        replaceGroupTasks.perform(context);
        return context;
    }

    public UserPatchContext patchUser(String userId,
                                      List<ModificationUnit> operations,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        UserPatchContext<ScimUser> context = new UserPatchContext<>();
        context.setId(userId);
        context.setModifications(operations);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        patchUserTasks.perform(context);
        return context;
    }

    public GroupPatchContext patchGroup(String groupId,
                                        List<ModificationUnit> operations,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        GroupPatchContext<ScimGroup> context = new GroupPatchContext<>();
        context.setId(groupId);
        context.setModifications(operations);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        patchGroupTasks.perform(context);
        return context;
    }

    public UserDeleteContext deleteUser(String userId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {
        UserDeleteContext context = new UserDeleteContext();
        context.setId(userId);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        deleteUserTasks.perform(context);
        return context;
    }

    public GroupDeleteContext deleteGroup(String groupId,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        GroupDeleteContext context = new GroupDeleteContext();
        context.setId(groupId);
        context.setHttpRequest(request);
        context.setHttpResponse(response);
        deleteGroupTasks.perform(context);
        return context;
    }
}
