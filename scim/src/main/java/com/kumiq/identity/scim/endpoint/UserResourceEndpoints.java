package com.kumiq.identity.scim.endpoint;

import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.user.ScimUser;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.service.OperationCentral;
import com.kumiq.identity.scim.task.UserCreateContext;
import com.kumiq.identity.scim.task.UserGetContext;
import com.kumiq.identity.scim.task.UserQueryContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/Users")
@SuppressWarnings("unchecked")
public class UserResourceEndpoints {

    @Autowired
    OperationCentral operationCentral;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public Map retrieveUserById(@PathVariable String userId,
                                HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse) {
        UserGetContext context = operationCentral.retrieveUserById(userId, httpServletRequest, httpServletResponse);
        return context.getData();
    }

    @RequestMapping(method = RequestMethod.GET, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public Map queryUser(@RequestParam(value = "attributes", defaultValue = ScimConstants.DEFAULT_ATTR, required = false) String commaSeperatedAttributes,
                         @RequestParam(value = "startIndex", defaultValue = ScimConstants.DEFAULT_START_IDX, required = false) int startIndex,
                         @RequestParam(value = "count", defaultValue = ScimConstants.DEFAULT_COUNT, required = false) int count,
                         @RequestParam(value = "filter", defaultValue = ScimConstants.DEFAULT_FILTER, required = false) String filter,
                         @RequestParam(value = "sortBy", defaultValue = ScimConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                         @RequestParam(value = "sortOrder", defaultValue = ScimConstants.ORDER_ASC, required = false) String sortOrder,
                         HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) {
        List<String> attributes = Arrays.asList(commaSeperatedAttributes.split(","));
        boolean ascending = ScimConstants.ORDER_ASC.equals(sortOrder);

        UserQueryContext context = operationCentral.queryUser(attributes, startIndex, count, filter, sortBy, ascending,
                httpServletRequest, httpServletResponse);
        context.getResults().put(ScimConstants.SCHEMAS, Arrays.asList(ScimConstants.URN_LIST_RESPONSE));
        return context.getResults();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = ScimConstants.SCIM_CONTENT_TYPE, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public ScimUser createUser(@RequestBody ScimUser resource,
                          HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) {
        UserCreateContext context = operationCentral.createUser(resource, httpServletRequest, httpServletResponse);
        return (ScimUser) context.getResource();
    }
}
