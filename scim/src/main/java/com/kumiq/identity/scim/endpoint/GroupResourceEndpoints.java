package com.kumiq.identity.scim.endpoint;

import com.kumiq.identity.scim.endpoint.support.PatchOpBody;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.group.ScimGroup;
import com.kumiq.identity.scim.service.OperationCentral;
import com.kumiq.identity.scim.task.GroupCreateContext;
import com.kumiq.identity.scim.task.GroupGetContext;
import com.kumiq.identity.scim.task.GroupQueryContext;
import com.kumiq.identity.scim.task.GroupReplaceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.kumiq.identity.scim.resource.constant.ScimConstants.SCIM_CONTENT_TYPE;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/Groups")
@SuppressWarnings("unchecked")
public class GroupResourceEndpoints {

    @Autowired
    OperationCentral operationCentral;

    @RequestMapping(value = "/{groupId}", method = RequestMethod.GET, produces = SCIM_CONTENT_TYPE)
    public Map retrieveGroupById(@PathVariable String groupId,
                                HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse) {
        GroupGetContext context = operationCentral.retrieveGroupById(groupId, httpServletRequest, httpServletResponse);
        return context.getData();
    }

    @RequestMapping(method = RequestMethod.GET, produces = SCIM_CONTENT_TYPE)
    public Map queryGroup(@RequestParam(value = "attributes", defaultValue = ScimConstants.DEFAULT_ATTR, required = false) String commaSeperatedAttributes,
                          @RequestParam(value = "startIndex", defaultValue = ScimConstants.DEFAULT_START_IDX, required = false) int startIndex,
                          @RequestParam(value = "count", defaultValue = ScimConstants.DEFAULT_COUNT, required = false) int count,
                          @RequestParam(value = "filter", defaultValue = ScimConstants.DEFAULT_FILTER, required = false) String filter,
                          @RequestParam(value = "sortBy", defaultValue = ScimConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                          @RequestParam(value = "sortOrder", defaultValue = ScimConstants.ORDER_ASC, required = false) String sortOrder,
                         HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse) {
        List<String> attributes = Arrays.asList(commaSeperatedAttributes.split(","));
        boolean ascending = ScimConstants.ORDER_ASC.equals(sortOrder);

        GroupQueryContext context = operationCentral.queryGroup(attributes, startIndex, count, filter, sortBy, ascending,
                httpServletRequest, httpServletResponse);
        context.getResults().put(ScimConstants.SCHEMAS, Arrays.asList(ScimConstants.URN_LIST_RESPONSE));
        return context.getResults();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = SCIM_CONTENT_TYPE, produces = SCIM_CONTENT_TYPE)
    public ScimGroup createGroup(@RequestBody ScimGroup resource,
                                 HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse) {
        GroupCreateContext context = operationCentral.createGroup(resource, httpServletRequest, httpServletResponse);
        return (ScimGroup) context.getResource();
    }

    @RequestMapping(value = "/{groupId}", method = RequestMethod.PUT, consumes = SCIM_CONTENT_TYPE, produces = SCIM_CONTENT_TYPE)
    public ScimGroup replaceGroup(@PathVariable String groupId,
                                  @RequestHeader(value = "If-Match") String version,
                                  @RequestBody ScimGroup resource,
                                  HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse) {
        GroupReplaceContext context = operationCentral.replaceGroup(groupId, resource, httpServletRequest, httpServletResponse);
        return (ScimGroup) context.getResource();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{groupId}", method = RequestMethod.PATCH, consumes = SCIM_CONTENT_TYPE, produces = SCIM_CONTENT_TYPE)
    public void patchGroup(@PathVariable String groupId,
                           @RequestHeader(value = "If-Match") String version,
                           @RequestBody PatchOpBody requestBody,
                           HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) {
        operationCentral.patchGroup(groupId, requestBody.getOperations(), httpServletRequest, httpServletResponse);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
    public void deleteGroup(@PathVariable String groupId,
                            @RequestHeader(value = "If-Match") String version,
                            HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse) {
        operationCentral.deleteGroup(groupId, httpServletRequest, httpServletResponse);
    }
}
