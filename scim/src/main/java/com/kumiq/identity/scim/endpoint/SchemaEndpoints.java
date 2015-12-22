package com.kumiq.identity.scim.endpoint;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@RestController
@RequestMapping("/Schemas")
public class SchemaEndpoints {

    @Autowired
    ResourceDatabase.SchemaDatabase database;

    @RequestMapping(method = RequestMethod.GET, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public List<Schema> retrieveAllSchemas() {
        List<Schema> allSchemas = database.findAll();
        allSchemas.removeIf(schema -> isSecretSchema(schema.getId()));
        if (CollectionUtils.isEmpty(allSchemas)) {
            throw ExceptionFactory.resourceNotFound(ScimConstants.RESOURCE_TYPE_SCHEMA, "*");
        }
        return allSchemas;
    }

    @RequestMapping(value = "/{schemaId:.+}", method = RequestMethod.GET, produces = ScimConstants.SCIM_CONTENT_TYPE)
    public Schema retrieveSchemaById(@PathVariable String schemaId) {
        if (isSecretSchema(schemaId))
            throw ExceptionFactory.resourceNotFound(ScimConstants.RESOURCE_TYPE_SCHEMA, schemaId);

        Optional<Schema> schema = database.findById(schemaId);
        if (!schema.isPresent())
            throw ExceptionFactory.resourceNotFound(ScimConstants.RESOURCE_TYPE_SCHEMA, schemaId);
        return schema.get();
    }

    private boolean isSecretSchema(String schemaId) {
        return ScimConstants.HINT_USER.equals(schemaId) || ScimConstants.HINT_GROUP.equals(schemaId);
    }
}
