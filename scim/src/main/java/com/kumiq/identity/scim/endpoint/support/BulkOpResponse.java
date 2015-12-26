package com.kumiq.identity.scim.endpoint.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class BulkOpResponse {

    @JsonProperty("schemas")
    private String[] schemas = new String[]{ScimConstants.URN_BULK_RESPONSE};

    @JsonProperty("Operations")
    private List<Operation> operations;

    public String[] getSchemas() {
        return schemas;
    }

    public void setSchemas(String[] schemas) {
        this.schemas = schemas;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public static class Operation {

        @JsonProperty("method")
        @JsonDeserialize(using = BulkOpRequest.HttpMethodJsonDeserializer.class)
        private HttpMethod method;

        @JsonProperty("bulkId")
        private String bulkId;

        @JsonProperty("version")
        private String version;

        @JsonProperty("location")
        private String location;

        @JsonProperty("response")
        private Object response;

        @JsonProperty("status")
        @JsonSerialize(using = HttpStatusJsonSerializer.class)
        private HttpStatus httpStatus;

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }

        public String getBulkId() {
            return bulkId;
        }

        public void setBulkId(String bulkId) {
            this.bulkId = bulkId;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Object getResponse() {
            return response;
        }

        public void setResponse(Object response) {
            this.response = response;
        }

        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        public void setHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
        }
    }

    public static class HttpStatusJsonSerializer extends JsonSerializer<HttpStatus> {
        @Override
        public void serialize(HttpStatus value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            jgen.writeNumber(value.value());
        }
    }
}
