package com.kumiq.identity.scim.endpoint.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class BulkOpRequest {

    @JsonProperty("failOnErrors")
    private Integer failOnErrors = Integer.MAX_VALUE;

    @JsonProperty("Operations")
    private List<Operation> operations;

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public Integer getFailOnErrors() {
        return failOnErrors;
    }

    public void setFailOnErrors(Integer failOnErrors) {
        this.failOnErrors = failOnErrors;
    }

    public static class Operation {

        @JsonProperty("method")
        @JsonDeserialize(using = HttpMethodJsonDeserializer.class)
        private HttpMethod method;

        @JsonProperty("bulkId")
        private String bulkId;

        @JsonProperty("version")
        private String version;

        @JsonProperty("path")
        private String path;

        @JsonProperty("data")
        private Map jsonData;

        public HttpMethod getMethod() {
            return method;
        }

        public void setMethod(HttpMethod method) {
            this.method = method;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
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

        public Map getJsonData() {
            return jsonData;
        }

        public void setJsonData(Map jsonData) {
            this.jsonData = jsonData;
        }
    }

    public static class HttpMethodJsonDeserializer extends JsonDeserializer<HttpMethod> {
        @Override
        public HttpMethod deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return HttpMethod.valueOf(jp.getText().toUpperCase());
        }
    }
}
