package com.kumiq.identity.scim.task.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumiq.identity.scim.path.ModificationUnit;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.task.PatchContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.TypeUtils;
import com.kumiq.identity.scim.utils.ValueUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Convert modifications value structures such as list and map to actual model classes defined in schema
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings("unchecked")
public class FormalizeModificationValueTask<T extends Resource> implements Task<PatchContext<T>> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void perform(PatchContext<T> context) {
        Assert.notNull(context.getSchema());

        context.getModifications().forEach(modificationUnit -> {
            if (ModificationUnit.Operation.REMOVE.equals(modificationUnit.getOperation()))
                return;

            String queryFreePath = eraseQueryComponent(modificationUnit.getPath());
            Schema.Attribute attribute = context.getSchema().findAttributeByPath(queryFreePath);
            if (attribute == null)
                return;

            modificationUnit.setValue(formalizeValue(modificationUnit.getValue(), attribute));
        });
    }

    private Object formalizeValue(Object value, Schema.Attribute attribute) {
        if (!ScimConstants.ATTR_TYPES_COMPLEX.equals(attribute.getType()))
            return value;

        if (attribute.isMultiValued()) {
            if (TypeUtils.isCollection(value)) {
                List<Object> array = new ArrayList<>();
                TypeUtils.asList(value).forEach(object -> array.add(formalizeNonListComplexValue(object, attribute.getElementClazz())));
                return array;
            } else {
                return formalizeNonListComplexValue(value, attribute.getElementClazz());
            }
        } else {
            return formalizeNonListComplexValue(value, attribute.getClazz());
        }
    }

    private Object formalizeNonListComplexValue(Object value, Class clazz) {
        try {
            String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, clazz);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String eraseQueryComponent(String path) {
        return Arrays.asList(path.split("\\."))
                .stream()
                .map(s -> s.replaceAll("\\[.*\\]", ""))
                .collect(Collectors.joining("."));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(objectMapper);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
