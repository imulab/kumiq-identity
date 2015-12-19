package com.kumiq.identity.scim.endpoint;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.misc.ServiceProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RestController
@RequestMapping("/ServiceProviderConfig")
public class ServiceProviderConfigEndpoints {

    @Autowired
    ResourceDatabase.ServiceProviderConfigDatabase database;

    @RequestMapping(method = RequestMethod.GET, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public ServiceProviderConfig retrieveServiceProviderConfig() {
        return database.find();
    }
}
