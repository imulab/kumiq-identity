package com.kumiq.identity.scim.service;

import com.kumiq.identity.scim.resource.user.ScimUser;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserCreateContext;
import com.kumiq.identity.scim.task.UserGetContext;
import com.kumiq.identity.scim.task.UserQueryContext;
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
}
