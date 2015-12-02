package com.kumiq.identity.scim.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kumiq.identity.scim.resource.constant.ScimConstants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Weinan Qiu
 * @since 0.1.0
 */
public class JsonDateSerializer extends JsonSerializer<Date> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(ScimConstants.DATE_FORMAT);

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeString(dateFormat.format(value));
    }
}
